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

		ElementPowPreProcessing g = parameters.getG().getPowPreProcessing();

		// PK = g^a, where a = secret key
		Element pk = g.powZn(sk).getImmutable();

		return elementToBase64String(pk);
	}

	public String newReEncryptionKey(String srcSecretKey, String destPublicKey) {
		Element ssk = base64StringToElement(srcSecretKey,
				parameters.getGroupZq());
		Element dpk = base64StringToCurveElement(destPublicKey, parameters
				.getG().getElement().getField());

		// RK = (g^b)^1/a
		// a = source secret key
		// g^b = destination public key

		Element rek = dpk.powZn(ssk.invert()).getImmutable();

		return elementToBase64String(rek);
	}

	public String encrypt(String message, String destPublicKey) {
		CipherText c = encryptToCipherText(message, destPublicKey);

		return mergeByteArraysToBase64String(c.toByteArrays());
	}

	public String decrypt(String cipher, String destSecretKey) {
		CipherText cipherText = cipherStringToCipherText(cipher,
				parameters.getGroup2());

		return decryptCipherText(cipherText, destSecretKey);
	}

	private CipherText cipherStringToCipherText(String cipher,
			Field<?> rightSourceField) {
		byte[] source = Base64.getDecoder().decode(cipher);

		Element leftN = parameters.getGroup2().newElement();
		Element right = rightSourceField.newElement();

		int elementLengthInBytes = leftN.getLengthInBytes();
		int messageLengthInBytes = source.length;

		CipherText cipherText = new CipherText();

		int offset = 0;

		while (messageLengthInBytes - offset != elementLengthInBytes) {
			offset += leftN.setFromBytes(source, offset);
			cipherText.appendLeft(leftN);
		}

		right.setFromBytes(source, offset);

		cipherText.setRight(right);

		return cipherText;
	}

	public String reEncrypt(String cipher, String reEncryptionKey) {
		CipherText cipherText = cipherStringToCipherText(cipher,
				parameters.getGroup1());

		CipherText reEncrypted = reEncryptToCipherText(cipherText, reEncryptionKey);

		return mergeByteArraysToBase64String(reEncrypted.toByteArrays());
	}

	public String encryptReEncryptable(String message, String destPublicKey) {
		CipherText c = encryptReEncryptableToCipherText(message, destPublicKey);

		return mergeByteArraysToBase64String(c.toByteArrays());
	}

	public String decryptReEncryptable(String cipher, String destSecretKey) {
		CipherText cipherText = cipherStringToCipherText(cipher,
				parameters.getGroup1());

		return decryptReEncryptableCipherText(cipherText, destSecretKey);
	}

	private CipherText encryptToCipherText(String message, String destPublicKey) {
		ElementPowPreProcessing g = parameters.getG().getPowPreProcessing();
		ElementPowPreProcessing z = parameters.getZ().getPowPreProcessing();

		Element dpk = base64StringToCurveElement(destPublicKey, g.getField());

		// get a random integer k from group Zq
		Element k = parameters.getGroupZq().newRandomElement().getImmutable();

		// C = (left[], right)

		// right = Z^(ak), where a = destPublicKey
		// It is provable that Z^(ak) = e(a, g^k)
		Element right = parameters.getE().pairing(dpk, g.powZn(k));

		// leftN = m[i]*Z^k
		Element[] m = messageToMultipleElementsInGroup2(message);

		Element leftN = null;
		CipherText cipherText = null;

		for (int i = 0; i < m.length; i++) {
			leftN = m[i].mul(z.powZn(k));

			boolean firstIteration = i == 0 && cipherText == null;

			if (firstIteration) {
				cipherText = new CipherText(leftN, right);
			} else {
				cipherText.appendLeft(leftN);
			}
		}

		return cipherText;
	}

	private String decryptCipherText(CipherText cipher, String destSecretKey) {
		Element dsk = base64StringToElement(destSecretKey,
				parameters.getGroupZq());

		String plaintext = "";

		// M = c1/c2^(1/a), where a = destSecretKey
		for (Element leftN : cipher.getLefts()) {
			Element m = leftN.div(cipher.getRight().powZn(dsk.invert()));

			plaintext += new String(m.toBytes());
		}

		return plaintext.trim();
	}

	private CipherText reEncryptToCipherText(CipherText cipher,
			String reEncryptionKey) {
		Element rek = base64StringToCurveElement(reEncryptionKey, parameters
				.getG().getElement().getField());

		// right = Z^(bk)
		// lefts = m[i]Z^k = lefts

		// Z^(bk) = e(g^(ak), g^(b/a))
		// g^(ak) = right
		// g^(b/a) = reEncryptionKey

		// m[i]Z^k = lefts[i]

		Element newRight = parameters.getE().pairing(rek, cipher.getRight());

		cipher.setRight(newRight);
		
		return cipher;
	}

	private CipherText encryptReEncryptableToCipherText(String message,
			String destPublicKey) {
		ElementPowPreProcessing g = parameters.getG().getPowPreProcessing();
		ElementPowPreProcessing z = parameters.getZ().getPowPreProcessing();

		Element dpk = base64StringToCurveElement(destPublicKey, g.getField());

		// get a random integer k from group Zq
		Element k = parameters.getGroupZq().newRandomElement().getImmutable();

		// C = (left[], right)

		// leftN = m[i]*Z^k
		// right = g^(ak)
		// It can be proven that g^(ak) = a^k, where a = destPublicKey

		Element[] m = messageToMultipleElementsInGroup2(message);

		Element leftN = null;
		Element right = dpk.powZn(k);

		CipherText cipherText = null;

		for (int i = 0; i < m.length; i++) {
			leftN = m[i].mul(z.powZn(k));

			boolean firstIteration = i == 0 && cipherText == null;

			if (firstIteration) {
				cipherText = new CipherText(leftN, right);
			} else {
				cipherText.appendLeft(leftN);
			}
		}

		return cipherText;
	}

	private String decryptReEncryptableCipherText(CipherText cipher,
			String destSecretKey) {

		ElementPowPreProcessing g = parameters.getG().getPowPreProcessing();
		Pairing e = parameters.getE();

		Element dsk = base64StringToElement(destSecretKey,
				parameters.getGroupZq());

		// M = leftN/e(right, g)^(1/a), where a = destSecretKey

		Element denominator = e.pairing(cipher.getRight(),
				g.powZn(dsk.invert()));

		String plaintext = "";

		for (Element leftN : cipher.getLefts()) {
			Element m = leftN.div(denominator);

			plaintext += new String(m.toBytes());
		}

		return plaintext.trim();
	}

	private Element[] messageToMultipleElementsInGroup2(String message) {
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

	private String elementToBase64String(Element element) {
		return Base64.getEncoder().encodeToString(element.toBytes());
	}

	private String mergeByteArraysToBase64String(byte[]... parts) {
		byte[] merged = parts[0];

		for (int i = 1; i < parts.length; i++) {
			merged = ArrayUtils.addAll(merged, parts[i]);
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
