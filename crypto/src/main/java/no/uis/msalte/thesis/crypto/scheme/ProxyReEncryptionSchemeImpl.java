package no.uis.msalte.thesis.crypto.scheme;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.field.curve.CurveElement;

import java.util.Base64;

import no.uis.msalte.thesis.crypto.model.CipherText;

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

	public CipherText encrypt(String message, String destPublicKey) {
		ElementPowPreProcessing g = parameters.getG().getPowPreProcessing();
		ElementPowPreProcessing z = parameters.getZ().getPowPreProcessing();

		Element m = stringToElementInGroup2(message);
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

	public String decrypt(CipherText cipher, String destSecretKey) {
		Element dsk = base64StringToElement(destSecretKey,
				parameters.getGroupZq());

		// M = c2/c1^(1/a), where a = destSecretKey
		Element m = cipher.getC2().div(cipher.getC1().powZn(dsk.invert()));

		return elementToPlainText(m);
	}

	public CipherText reEncrypt(CipherText cipher, String reEncryptionKey) {
		Element rek = base64StringToCurveElement(reEncryptionKey, parameters
				.getG().getElement().getField());

		// c1' = Z^(bk)
		// c2' = mZ^k = c2

		// Z^(bk) = e(g^(ak), g^(b/a))
		// g^(ak) = c1
		// g^(b/a) = reEncryptionKey

		// mZ^k = c2

		Element c1 = parameters.getE().pairing(rek, cipher.getC1());
		Element c2 = cipher.getC2(); // unmodified

		return new CipherText(c1, c2);
	}

	public CipherText encryptReEncryptable(String message, String destPublicKey) {
		ElementPowPreProcessing g = parameters.getG().getPowPreProcessing();
		ElementPowPreProcessing z = parameters.getZ().getPowPreProcessing();

		Element m = stringToElementInGroup2(message);
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

	public String decryptReEncryptable(CipherText cipher, String destSecretKey) {
		ElementPowPreProcessing g = parameters.getG().getPowPreProcessing();
		Pairing e = parameters.getE();

		Element dsk = base64StringToElement(destSecretKey,
				parameters.getGroupZq());

		// M = c2/e(c1, g)^(1/a), where a = destSecretKey
		
		Element denominator = e.pairing(cipher.getC1(), g.powZn(dsk.invert()));

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

	private CurveElement<?> base64StringToCurveElement(String s, Field<?> field) {
		byte[] bytes = Base64.getDecoder().decode(s);

		CurveElement<?> curveElement = (CurveElement<?>) field.newZeroElement();

		curveElement.setFromBytes(bytes);

		return curveElement;
	}

	private String elementToPlainText(Element element) {
		return new String(element.toBytes()).trim();
	}
}
