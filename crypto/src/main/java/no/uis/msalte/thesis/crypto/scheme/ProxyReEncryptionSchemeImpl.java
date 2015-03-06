package no.uis.msalte.thesis.crypto.scheme;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.field.curve.CurveElement;

import java.util.Base64;

import no.uis.msalte.thesis.crypto.model.CipherText;

public class ProxyReEncryptionSchemeImpl implements ProxyReEncryptionScheme {
	private ProxyReEncryptionParameters parameters;

	public ProxyReEncryptionSchemeImpl(ProxyReEncryptionParameters parameters) {
		this.parameters = parameters;
	}

	public Element newSecretKey() {
		// SK = random element a in Zq
		return parameters.getGroupZq().newRandomElement().getImmutable();
	}

	public Element newPublicKey(Element secretKey) {
		// PK = g^a, where a = secret key
		return parameters.getG().getPowPreProcessing().powZn(secretKey)
				.getImmutable();
	}

	public Element newReEncryptionKey(Element srcSecretKey,
			Element destPublicKey) {
		// RK = (g^b)^1/a
		// a = source secret key
		// g^b = destination public key

		return destPublicKey.powZn(srcSecretKey.invert()).getImmutable();
	}

	public CipherText encrypt(String message, Element destPublicKey) {
		Element m = stringToElementInGroup2(message);

		// get a random integer k from group Zq
		Element k = parameters.getGroupZq().newRandomElement().getImmutable();

		// C[1] = Z^(ak), where a = destPublicKey
		// It is provable that Z^(ak) = e(a, g^k)
		Element c1 = parameters.getE().pairing(destPublicKey,
				parameters.getG().getPowPreProcessing().powZn(k));

		// C[2] = m*Z^k
		Element c2 = m.mul(parameters.getZ().getPowPreProcessing().powZn(k));

		return new CipherText(c1, c2);
	}

	public String decrypt(CipherText cipher, Element destSecretKey) {
		// M = C[2]/C[1]^(1/a), where a = destSecretKey
		Element m = cipher.getC2().div(
				cipher.getC1().powZn(destSecretKey.invert()));

		return elementToPlainText(m);
	}

	public CipherText reEncrypt(CipherText cipher, Element reEncryptionKey) {
		// C' = [Z^(bk), mZ^k]

		// Z^(bk) = e(g^(ak), g^(b/a))
		// where g^(b/a) = reEncryptionKey and g^(ak) = C[1]

		// mZ^k = C[2]

		Element c1 = parameters.getE().pairing(reEncryptionKey, cipher.getC1());
		Element c2 = cipher.getC2(); // unmodified

		return new CipherText(c1, c2);
	}

	public CipherText encryptReEncryptable(String message, Element destPublicKey) {
		Element m = stringToElementInGroup2(message);

		// get a random integer k from group Zq
		Element k = parameters.getGroupZq().newRandomElement().getImmutable();

		// C[1] = g^(ak)
		// It can be proven that g^(ak) = a^k, where a = destPublicKey
		// C[2] = m*Z^k

		Element c1 = destPublicKey.powZn(k).getImmutable();
		Element c2 = m.mul(parameters.getZ().getPowPreProcessing().powZn(k));

		return new CipherText(c1, c2);
	}

	public String decryptReEncryptable(CipherText cipher, Element destSecretKey) {
		// M = C[2]/e(C[1], g)^(1/a), where a = destSecretKey

		Element denominator = parameters.getE().pairing(
				cipher.getC1(),
				parameters.getG().getPowPreProcessing()
						.powZn(destSecretKey.invert()));

		Element m = cipher.getC2().div(denominator);

		return elementToPlainText(m);
	}

	private Element stringToElementInGroup2(String message) {
		byte[] bytes = message.getBytes();

		int maxLength = parameters.getGroup2().getLengthInBytes();

		// System.out.println(String.format(
		// "Message is %d bytes long - Max length is %d", bytes.length,
		// maxLength));

		if (bytes.length > maxLength) {
			throw new IllegalArgumentException(
					"Message is too long in stringToElement()");
		}

		final Element result = parameters.getGroup2().newElement();

		result.setFromBytes(bytes);

		return result.getImmutable();
	}

	private String elementToBase64String(Element element) {
		return Base64.getEncoder().encodeToString(element.toBytes());
	}

	private Element base64StringToElement(String s, Field<?> field) {
		byte[] bytes = Base64.getDecoder().decode(s);

		Element curveElement = field.newZeroElement();

		curveElement.setFromBytes(bytes);

		return curveElement;
	}

	private CurveElement base64StringToCurveElement(String s, Field<?> field) {
		byte[] bytes = Base64.getDecoder().decode(s);

		CurveElement<?> curveElement = (CurveElement<?>) field.newZeroElement();

		curveElement.setFromBytes(bytes);

		return curveElement;
	}

	private String elementToPlainText(Element element) {
		return new String(element.toBytes()).trim();
	}
}
