package no.uis.msalte.thesis.crypto.pre.scheme;

import it.unisa.dia.gas.jpbc.Element;
import no.uis.msalte.thesis.crypto.pre.model.CipherTuple;

public class ProxyReEncryptionScheme implements IProxyReEncryptionScheme {
	private ProxyReEncryptionParameters parameters;

	public ProxyReEncryptionScheme(ProxyReEncryptionParameters parameters) {
		this.parameters = parameters;
	}

	public Element newSecretKey() {
		// SK = random element in Zq
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

	public CipherTuple encrypt(String message, Element destPublicKey) {
		Element m = messageToElementInGroup2(message);

		// get a random integer k from group Zq
		Element k = parameters.getGroupZq().newRandomElement().getImmutable();

		// C[0] = Z^(ak), where a = destPublicKey
		// Z^(ak) = e(a, g^k)
		Element c0 = parameters.getE().pairing(destPublicKey,
				parameters.getG().getPowPreProcessing().powZn(k));

		// C[1] = m*Z^k
		Element c1 = m.mul(parameters.getZ().getPowPreProcessing().powZn(k));

		return new CipherTuple(new Element[] { c0, c1 });
	}

	public String decrypt(CipherTuple cipher, Element destSecretKey) {
		// M = C[1]/C[0]^(1/a), where a = destSecretKey
		Element m = cipher.get(1).div(
				cipher.get(0).powZn(destSecretKey.invert()));

		return elementToString(m);
	}

	public CipherTuple reEncrypt(CipherTuple cipher, Element reEncryptionKey) {
		// C[0]' = C[0]
		// C[1]' = e(g^(ak), reEncryptionKey), where g^(ak) = C[1]

		return new CipherTuple(new Element[] { cipher.get(0),
				parameters.getE().pairing(cipher.get(1), reEncryptionKey) });
	}

	public CipherTuple encryptReEncryptable(String message,
			Element destPublicKey) {
		// TODO Auto-generated method stub
		return null;
	}

	public String decryptReEncryptable(CipherTuple cipher, Element destSecretKey) {
		// TODO Auto-generated method stub
		return null;
	}

	private Element messageToElementInGroup2(String message) {
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

	private String elementToString(Element element) {
		return new String(element.toBytes()).trim();
	}

}
