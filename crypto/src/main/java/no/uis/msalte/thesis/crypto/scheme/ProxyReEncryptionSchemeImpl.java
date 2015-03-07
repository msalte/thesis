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
		
		int n = c.getLefts().length;

		// Create a byte array representing the entire cipher text
		// - the first N elements represents the message's N left parts
		// - the last element represents the message's single right part
		byte[][] bytes = new byte[n + 1][1];

		for (int i = 0; i < n; i++) {
			bytes[i] = c.getLefts()[i].toBytes();
		}

		bytes[n] = c.getRight().toBytes();

		return mergeByteArraysToBase64String(bytes);
	}

	public String decrypt(String cipher, String destSecretKey) {
		byte[] bytes = Base64.getDecoder().decode(cipher);

		Element leftN = parameters.getGroup2().newElement();
		Element right = parameters.getGroup2().newElement();

		int elementByteLength = leftN.getLengthInBytes();
		int messageByteLength = bytes.length;

		CipherText cipherText = new CipherText();

		int offset = 0;

		while (messageByteLength - offset != elementByteLength) {
			offset += leftN.setFromBytes(bytes, offset);
			cipherText.appendLeft(leftN);
		}

		right.setFromBytes(bytes, offset);

		cipherText.setRight(right);

		return decryptCipherText(cipherText, destSecretKey);
	}

	public String reEncrypt(String cipher, String reEncryptionKey) {
		byte[] bytes = Base64.getDecoder().decode(cipher);

		Element c1 = parameters.getGroup1().newElement();
		Element c2 = parameters.getGroup2().newElement();

		int offset = c1.setFromBytes(bytes, 0);
		c2.setFromBytes(bytes, offset);

		CipherText c = reEncryptToCipherText(new CipherText(c1, c2),
				reEncryptionKey);

		return mergeByteArraysToBase64String(c.getLeft().toBytes(), c
				.getRight().toBytes());
	}

	public String encryptReEncryptable(String message, String destPublicKey) {
		CipherText c = encryptReEncryptableToCipherText(message, destPublicKey);

		return mergeByteArraysToBase64String(c.getLeft().toBytes(), c
				.getRight().toBytes());
	}

	public String decryptReEncryptable(String cipher, String destSecretKey) {
		byte[] bytes = Base64.getDecoder().decode(cipher);

		Element c1 = parameters.getGroup1().newElement();
		Element c2 = parameters.getGroup2().newElement();

		int offset = c1.setFromBytes(bytes, 0);
		c2.setFromBytes(bytes, offset);

		return decryptReEncryptableCipherText(new CipherText(c1, c2),
				destSecretKey);
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

		// left = m[i]*Z^k
		Element[] m = messageToMultipleElementsInGroup2(message);

		Element left = null;
		CipherText cipherText = null;

		for (int i = 0; i < m.length; i++) {
			left = m[i].mul(z.powZn(k));

			if (i == 0) {
				cipherText = new CipherText(left, right);
			} else {
				cipherText.appendLeft(left);
			}
		}

		return cipherText;
	}

	private String decryptCipherText(CipherText cipher, String destSecretKey) {
		Element dsk = base64StringToElement(destSecretKey,
				parameters.getGroupZq());

		String plaintext = "";

		// M = c1/c2^(1/a), where a = destSecretKey
		for (Element left : cipher.getLefts()) {
			Element m = left.div(cipher.getRight().powZn(dsk.invert()));

			plaintext += new String(m.toBytes());
		}

		return plaintext.trim();
	}

	private CipherText reEncryptToCipherText(CipherText cipher,
			String reEncryptionKey) {
		Element rek = base64StringToCurveElement(reEncryptionKey, parameters
				.getG().getElement().getField());

		// c1' = Z^(bk)
		// c2' = mZ^k = c2

		// Z^(bk) = e(g^(ak), g^(b/a))
		// g^(ak) = c1
		// g^(b/a) = reEncryptionKey

		// mZ^k = c2

		Element c1 = parameters.getE().pairing(rek, cipher.getLeft());
		Element c2 = cipher.getRight(); // unmodified

		return new CipherText(c1, c2);
	}

	private CipherText encryptReEncryptableToCipherText(String message,
			String destPublicKey) {
		ElementPowPreProcessing g = parameters.getG().getPowPreProcessing();
		ElementPowPreProcessing z = parameters.getZ().getPowPreProcessing();

		Element m = messageToElementInGroup2(message);
		Element dpk = base64StringToCurveElement(destPublicKey, g.getField());

		// get a random integer k from group Zq
		Element k = parameters.getGroupZq().newRandomElement().getImmutable();

		// c1 = g^(ak)
		// It can be proven that g^(ak) = a^k, where a = destPublicKey
		// c2 = m*Z^k

		Element c1 = dpk.powZn(k);
		Element c2 = m.mul(z.powZn(k));

		return new CipherText(c1, c2);
	}

	private String decryptReEncryptableCipherText(CipherText cipher,
			String destSecretKey) {
		ElementPowPreProcessing g = parameters.getG().getPowPreProcessing();
		Pairing e = parameters.getE();

		Element dsk = base64StringToElement(destSecretKey,
				parameters.getGroupZq());

		// M = c2/e(c1, g)^(1/a), where a = destSecretKey

		Element denominator = e
				.pairing(cipher.getLeft(), g.powZn(dsk.invert()));

		Element m = cipher.getRight().div(denominator);

		return new String(m.toBytes()).trim();
	}

	private Element messageToElementInGroup2(String message) {
		byte[] bytes = message.getBytes();

		Field<?> group2 = parameters.getGroup2();

		if (bytes.length > group2.getLengthInBytes()) {
			throw new IllegalArgumentException("Message is too long");
		}

		final Element result = group2.newElement();

		result.setFromBytes(bytes);

		return result.getImmutable();
	}

	public Element[] messageToMultipleElementsInGroup2(String message) {
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
