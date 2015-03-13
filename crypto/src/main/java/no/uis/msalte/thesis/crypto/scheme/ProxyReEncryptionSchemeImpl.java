package no.uis.msalte.thesis.crypto.scheme;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.field.curve.CurveElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

import no.uis.msalte.thesis.crypto.model.CipherText;

import org.apache.commons.lang.ArrayUtils;

public class ProxyReEncryptionSchemeImpl implements ProxyReEncryptionScheme {
	private ProxyReEncryptionParameters parameters;

	public ProxyReEncryptionSchemeImpl(ProxyReEncryptionParameters parameters) {
		this.parameters = parameters;
	}

	public String newSecretKey() {
		// SK = random element a in Zq
		Element sk = parameters.getGroupZq().newRandomElement().getImmutable();

		return elementToBase64String(sk);
	}

	public String newPublicKey(String secretKey) {
		Element sk = base64StringToElement(secretKey, parameters.getGroupZq());

		ElementPowPreProcessing g = parameters.getG().powable();

		// PK = g^a, where a = secret key
		Element pk = g.powZn(sk).getImmutable();

		return elementToBase64String(pk);
	}

	public String newReEncryptionKey(String srcSecretKey, String destPublicKey) {
		Element sk = base64StringToElement(srcSecretKey,
				parameters.getGroupZq());
		Element pk = base64StringToCurveElement(destPublicKey, parameters
				.getG().element().getField());

		// RK = (g^b)^1/a
		// a = source secret key
		// g^b = destination public key

		Element rk = pk.powZn(sk.invert()).getImmutable();

		return elementToBase64String(rk);
	}

	public String encrypt(String message, String destPublicKey) {
		CipherText cipherText = encryptToCipherText(message, destPublicKey);

		return mergeByteArraysToBase64String(cipherText.toByteArrays());
	}

	public String decrypt(String cipher, String destSecretKey) {
		CipherText cipherText = parseCipherStringAsCipherText(cipher,
				parameters.getGroup2());

		return decryptCipherTextToPlainText(cipherText, destSecretKey);
	}

	public String reEncrypt(String cipher, String reEncryptionKey) {
		CipherText cipherText = parseCipherStringAsCipherText(cipher,
				parameters.getGroup1());

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
				parameters.getGroup1());

		return decryptReEncryptableCipherTextToPlainText(cipherText,
				destSecretKey);
	}

	private CipherText encryptToCipherText(String message, String destPublicKey) {
		ElementPowPreProcessing g = parameters.getG().powable();

		Element pk = base64StringToCurveElement(destPublicKey, g.getField());

		// get a random integer k from group Zq
		Element k = parameters.getGroupZq().newRandomElement().getImmutable();

		// C = (lefts[], right)
		// leftN = m[i]*Z^k
		// right = Z^(ak), where a = destPublicKey
		// It is provable that Z^(ak) = e(a, g^k)

		Element right = parameters.getE().pairing(pk, g.powZn(k));
		Element[] m = messageToElementsInGroup2(message);

		return buildCipherTextFromMessage(m, right, k);
	}

	private String decryptCipherTextToPlainText(CipherText cipher,
			String destSecretKey) {
		Element sk = base64StringToElement(destSecretKey,
				parameters.getGroupZq());

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
		Element rk = base64StringToCurveElement(reEncryptionKey, parameters
				.getG().element().getField());

		// right = Z^(bk)
		// lefts = m[i]Z^k = unmodified

		// Z^(bk) = e(g^(ak), g^(b/a))
		// g^(ak) = right
		// g^(b/a) = reEncryptionKey

		// m[i]Z^k = lefts[i]

		Element newRight = parameters.getE().pairing(rk, cipher.getRight());

		cipher.setRight(newRight);

		return cipher;
	}

	private CipherText encryptReEncryptableToCipherText(String message,
			String destPublicKey) {
		ElementPowPreProcessing g = parameters.getG().powable();
		Element pk = base64StringToCurveElement(destPublicKey, g.getField());

		// get a random k from group Zq
		Element k = parameters.getGroupZq().newRandomElement().getImmutable();

		// C = (lefts[], right)
		// leftN = m[i]*Z^k
		// right = g^(ak)
		// It can be proven that g^(ak) = a^k, where a = destPublicKey

		Element[] m = messageToElementsInGroup2(message);
		Element right = pk.powZn(k);

		return buildCipherTextFromMessage(m, right, k);
	}

	private String decryptReEncryptableCipherTextToPlainText(CipherText cipher,
			String destSecretKey) {

		ElementPowPreProcessing g = parameters.getG().powable();
		Pairing e = parameters.getE();

		Element sk = base64StringToElement(destSecretKey,
				parameters.getGroupZq());

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
		ElementPowPreProcessing z = parameters.getZ().powable();

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

	private Element[] messageToElementsInGroup2(String message) {
		ArrayList<Element> elements = new ArrayList<Element>();
		Field<?> group2 = parameters.getGroup2();

		byte[] source = message.trim().getBytes();

		int messageLengthInBytes = source.length;
		int elementLengthInBytes = group2.getLengthInBytes();

		// Need to make sure that the message length is divisible
		// by the element length.
		if (messageLengthInBytes < elementLengthInBytes) {
			// If lesser, set message length equal to element length
			source = Arrays.copyOf(source, elementLengthInBytes);
		} else if (messageLengthInBytes > elementLengthInBytes) {
			// If greater, set message length to a multiple of element length
			int factor = messageLengthInBytes / elementLengthInBytes + 1;

			source = Arrays.copyOf(source, factor * elementLengthInBytes);
		}

		messageLengthInBytes = source.length;

		// Begin finding the elements in group 2
		// representing the message
		int offset = 0;
		while (true) {
			Element e = group2.newElement();
			offset += e.setFromBytes(source, offset);
			elements.add(e.getImmutable());

			if (offset >= messageLengthInBytes) {
				// Finished
				break;
			}
		}

		return elements.toArray(new Element[elements.size()]);
	}

	private CipherText parseCipherStringAsCipherText(String cipher,
			Field<?> rightSourceField) {
		byte[] source = Base64.getDecoder().decode(cipher);

		Element leftN = parameters.getGroup2().newElement();
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

	private String elementToBase64String(Element element) {
		return Base64.getEncoder().encodeToString(element.toBytes());
	}

	private String mergeByteArraysToBase64String(byte[]... arrays) {
		byte[] merged = arrays[0];

		for (int i = 1; i < arrays.length; i++) {
			merged = ArrayUtils.addAll(merged, arrays[i]);
		}

		return Base64.getEncoder().encodeToString(merged);
	}

	private Element base64StringToElement(String s, Field<?> field) {
		byte[] bytes = Base64.getDecoder().decode(s);

		Element element = field.newZeroElement();

		element.setFromBytes(bytes);

		return element.getImmutable();
	}

	private Element base64StringToCurveElement(String s, Field<?> field) {
		byte[] bytes = Base64.getDecoder().decode(s);

		CurveElement<?, ?> curveElement = (CurveElement<?, ?>) field
				.newZeroElement();

		curveElement.setFromBytes(bytes);

		return curveElement.getImmutable();
	}
}
