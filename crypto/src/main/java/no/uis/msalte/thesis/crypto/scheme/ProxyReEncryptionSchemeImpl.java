package no.uis.msalte.thesis.crypto.scheme;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;

import java.util.Base64;

import no.uis.msalte.thesis.crypto.model.CipherText;
import no.uis.msalte.thesis.crypto.util.ElementUtils;

import org.apache.commons.lang.ArrayUtils;

public class ProxyReEncryptionSchemeImpl implements ProxyReEncryptionScheme {
	private ProxyReEncryptionParams params;

	public ProxyReEncryptionSchemeImpl() {
		params = new ProxyReEncryptionParams();
		params.initialize();
	}

	public String newSecretKey() {
		// SK = random element a in Zq
		Element sk = params.getGroupZq().newRandomElement().getImmutable();

		return ElementUtils.elementToBase64String(sk);
	}

	public String newPublicKey(String secretKey) {
		Element sk = ElementUtils.base64StringToElement(secretKey,
				params.getGroupZq());

		ElementPowPreProcessing g = params.getG().powable();

		// PK = g^a, where a = secret key
		Element pk = g.powZn(sk).getImmutable();

		return ElementUtils.elementToBase64String(pk);
	}

	public String newReEncryptionKey(String srcSecretKey, String destPublicKey) {
		Element sk = ElementUtils.base64StringToElement(srcSecretKey,
				params.getGroupZq());
		Element pk = ElementUtils.base64StringToElement(destPublicKey, params
				.getG().element().getField());

		// RK = (g^b)^1/a
		// a = source secret key
		// g^b = destination public key

		Element rk = pk.powZn(sk.invert()).getImmutable();

		return ElementUtils.elementToBase64String(rk);
	}

	public String encrypt(String message, String destPublicKey) {
		CipherText cipherText = encryptToCipherText(message, destPublicKey);

		return mergeByteArraysToBase64String(cipherText.toByteArrays());
	}

	public String decrypt(String cipher, String destSecretKey) {
		CipherText cipherText = parseCipherStringAsCipherText(cipher,
				params.getGroup2());

		return decryptCipherTextToPlainText(cipherText, destSecretKey);
	}

	public String reEncrypt(String cipher, String reEncryptionKey) {
		CipherText cipherText = parseCipherStringAsCipherText(cipher,
				params.getGroup1());

		CipherText reEncryptedCipherText = reEncryptToCipherText(cipherText,
				reEncryptionKey);

		return mergeByteArraysToBase64String(reEncryptedCipherText
				.toByteArrays());
	}

	public String encryptReEncryptable(String message, String destPublicKey) {
		CipherText cipherText = encryptReEncryptableToCipherText(message,
				destPublicKey);

		return mergeByteArraysToBase64String(cipherText.toByteArrays());
	}

	public String decryptReEncryptable(String cipher, String destSecretKey) {
		CipherText cipherText = parseCipherStringAsCipherText(cipher,
				params.getGroup1());

		return decryptReEncryptableCipherTextToPlainText(cipherText,
				destSecretKey);
	}

	private CipherText encryptToCipherText(String message, String destPublicKey) {
		ElementPowPreProcessing g = params.getG().powable();

		Element pk = ElementUtils.base64StringToElement(destPublicKey,
				g.getField());

		// get a random integer k from group Zq
		Element k = params.getGroupZq().newRandomElement().getImmutable();

		// C = (lefts[], right)
		// leftN = m[i]*Z^k
		// right = Z^(ak), where a = destPublicKey
		// It is provable that Z^(ak) = e(a, g^k)

		Element right = params.getE().pairing(pk, g.powZn(k));
		Element[] m = ElementUtils.messageToElementsInGroup(message,
				params.getGroup2());

		return buildCipherTextFromMessage(m, right, k);
	}

	private String decryptCipherTextToPlainText(CipherText cipher,
			String destSecretKey) {
		Element sk = ElementUtils.base64StringToElement(destSecretKey,
				params.getGroupZq());

		String plaintext = "";

		// M = sum(lefts[i]/right^(1/a)), where a = destSecretKey
		for (Element leftN : cipher.getLefts()) {
			Element m = leftN.div(cipher.getRight().powZn(sk.invert()));

			plaintext += new String(m.toBytes());
		}

		return plaintext.trim();
	}

	private CipherText reEncryptToCipherText(CipherText cipher,
			String reEncryptionKey) {
		Element rk = ElementUtils.base64StringToElement(reEncryptionKey, params
				.getG().element().getField());

		// right = Z^(bk)
		// lefts = m[i]Z^k = unmodified

		// Z^(bk) = e(g^(ak), g^(b/a))
		// g^(ak) = right
		// g^(b/a) = reEncryptionKey

		// m[i]Z^k = lefts[i]

		Element newRight = params.getE().pairing(rk, cipher.getRight());

		cipher.setRight(newRight);

		return cipher;
	}

	private CipherText encryptReEncryptableToCipherText(String message,
			String destPublicKey) {
		ElementPowPreProcessing g = params.getG().powable();
		Element pk = ElementUtils.base64StringToElement(destPublicKey,
				g.getField());

		// get a random k from group Zq
		Element k = params.getGroupZq().newRandomElement().getImmutable();

		// C = (lefts[], right)
		// leftN = m[i]*Z^k
		// right = g^(ak)
		// It can be proven that g^(ak) = a^k, where a = destPublicKey

		Element[] m = ElementUtils.messageToElementsInGroup(message,
				params.getGroup2());
		Element right = pk.powZn(k);

		return buildCipherTextFromMessage(m, right, k);
	}

	private String decryptReEncryptableCipherTextToPlainText(CipherText cipher,
			String destSecretKey) {

		ElementPowPreProcessing g = params.getG().powable();
		Pairing e = params.getE();

		Element sk = ElementUtils.base64StringToElement(destSecretKey,
				params.getGroupZq());

		// M = sum(lefts[i]/e(right, g)^(1/a)), where a = destSecretKey
		Element divisor = e.pairing(cipher.getRight(), g.powZn(sk.invert()));

		String plaintext = "";

		for (Element leftN : cipher.getLefts()) {
			Element m = leftN.div(divisor);

			plaintext += new String(m.toBytes());
		}

		return plaintext.trim();
	}

	private CipherText buildCipherTextFromMessage(Element[] m, Element right,
			Element k) {
		ElementPowPreProcessing z = params.getZ().powable();

		CipherText cipherText = null;
		Element leftN = null;

		// leftN = m[i]*Z^k
		for (int i = 0; i < m.length; i++) {
			leftN = m[i].mul(z.powZn(k));

			boolean isFirstIteration = i == 0 && cipherText == null;

			if (isFirstIteration) {
				cipherText = new CipherText(leftN, right);
			} else {
				cipherText.appendLeft(leftN);
			}
		}

		return cipherText;
	}

	private CipherText parseCipherStringAsCipherText(String cipher,
			Field<?> rightSourceField) {
		byte[] source = Base64.getDecoder().decode(cipher);

		Element leftN = params.getGroup2().newElement();
		Element right = rightSourceField.newElement();

		int elementLengthInBytes = leftN.getLengthInBytes();
		int messageLengthInBytes = source.length;

		CipherText cipherText = new CipherText();

		int offset = 0;

		// Read the first N*elementLengthInBytes bytes
		// into the cipher text's lefts[] element array
		while (messageLengthInBytes - offset != elementLengthInBytes) {
			offset += leftN.setFromBytes(source, offset);
			cipherText.appendLeft(leftN);
		}

		// The final elementLengthInBytes bytes
		// should be set to the cipher text's right element
		right.setFromBytes(source, offset);

		cipherText.setRight(right);

		return cipherText;
	}

	private String mergeByteArraysToBase64String(byte[]... arrays) {
		byte[] merged = arrays[0];

		for (int i = 1; i < arrays.length; i++) {
			merged = ArrayUtils.addAll(merged, arrays[i]);
		}

		return Base64.getEncoder().encodeToString(merged);
	}
}
