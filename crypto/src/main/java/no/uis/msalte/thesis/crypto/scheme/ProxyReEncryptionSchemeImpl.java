package no.uis.msalte.thesis.crypto.scheme;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.field.curve.CurveElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

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

		return mergeByteArraysToBase64String(c.getLeft().toBytes(), c
				.getRight().toBytes());
	}

	public String decrypt(String cipher, String destSecretKey) {
		byte[] bytes = Base64.getDecoder().decode(cipher);

		Element c1 = parameters.getGroup2().newElement();
		Element c2 = parameters.getGroup2().newElement();

		int offset = c1.setFromBytes(bytes, 0);
		c2.setFromBytes(bytes, offset);

		return decryptCipherText(new CipherText(c1, c2), destSecretKey);
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

		Element m = messageToElementInGroup2(message);
		Element dpk = base64StringToCurveElement(destPublicKey, g.getField());

		// get a random integer k from group Zq
		Element k = parameters.getGroupZq().newRandomElement().getImmutable();

		// c1 = Z^(ak), where a = destPublicKey
		// It is provable that Z^(ak) = e(a, g^k)
		Element c1 = parameters.getE().pairing(dpk, g.powZn(k));

		// c2 = m*Z^k
		Element c2 = m.mul(z.powZn(k));

		return new CipherText(c1, c2);
	}

	private String decryptCipherText(CipherText cipher, String destSecretKey) {
		Element dsk = base64StringToElement(destSecretKey,
				parameters.getGroupZq());

		// M = c2/c1^(1/a), where a = destSecretKey
		Element m = cipher.getRight().div(cipher.getLeft().powZn(dsk.invert()));

		return elementToPlainText(m);
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

		return elementToPlainText(m);
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

	public Element[] msgToElementsInGroup2(String message) {
		ArrayList<Element> elements = new ArrayList<Element>();
		Field<?> group2 = parameters.getGroup2();
		
		byte[] source = message.trim().getBytes();
		int lengthInBytes = group2.getLengthInBytes();
		
		if(source.length < lengthInBytes) {
			source = Arrays.copyOf(source, lengthInBytes);
		}
		
		int offset = 0;
		
		while(true) {
			Element e = group2.newElement();	
			offset = e.setFromBytes(source, offset);
			
			if(offset == 0) {
				break;
			}
			
			elements.add(e.getImmutable());
		}
		
		return elements.toArray(new Element[] {});
	}

	public Element[] messageToElementsInGroup2(String message) {
		int length = message.length();

		List<String> msgParts = splitMessageEqually(message, length);

		int byteLength = message.getBytes().length;
		int maxByteLength = parameters.getGroup2().getLengthInBytes();

		while (byteLength > maxByteLength) {
			length = length / 2;

			msgParts = splitMessageEqually(message, length);

			byteLength = msgParts.get(0).getBytes().length;
		}

		Element[] elements = new Element[msgParts.size()];
		Element e = null;

		for (int i = 0; i < msgParts.size(); i++) {
			byte[] b = msgParts.get(i).getBytes();
			b = Arrays.copyOf(b, maxByteLength);

			e = parameters.getGroup2().newElement();
			e.setFromBytes(b); // TODO use the return value here
			elements[i] = e.getImmutable();
		}

		return elements;
	}

	private static List<String> splitMessageEqually(String message, int length) {
		List<String> messageParts = new ArrayList<String>((message.length()
				+ length - 1)
				/ length);

		for (int start = 0; start < message.length(); start += length) {
			messageParts.add(message.substring(start,
					Math.min(message.length(), start + length)));
		}

		return messageParts;
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

		CurveElement<?> curveElement = (CurveElement<?>) field.newZeroElement();

		curveElement.setFromBytes(bytes);

		return curveElement.getImmutable();
	}

	private String elementToPlainText(Element element) {
		return new String(element.toBytes()).trim();
	}
}
