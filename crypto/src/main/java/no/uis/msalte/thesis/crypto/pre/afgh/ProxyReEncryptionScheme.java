package no.uis.msalte.thesis.crypto.pre.afgh;

import it.unisa.dia.gas.jpbc.Element;

public class ProxyReEncryptionScheme implements IProxyReEncryptionScheme {
	private ProxyReEncryptionParameters parameters;

	public ProxyReEncryptionScheme(ProxyReEncryptionParameters parameters) {
		this.parameters = parameters;
	}

	public Element newSecretKey() {
		// SK = random element a in Zr
		return parameters.getGroupZr().newRandomElement().getImmutable();
	}

	public Element newPublicKey(Element secretKey) {
		// PK = g^a
		// a = secret key
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

	public Element stringToElement(String m) {
		// m must exist in group2

		byte[] bytes = m.getBytes();

		int maxLength = parameters.getGroup2().getLengthInBytes();

		System.out.println(String.format(
				"Message is %d bytes long - Max length is %d", bytes.length,
				maxLength));

		if (bytes.length > maxLength) {
			throw new IllegalArgumentException(
					"Message is too long in stringToElement()");
		}

		final Element result = parameters.getGroup2().newElement();

		result.setFromBytes(bytes);

		return result.getImmutable();

	}

	public String elementToString(Element e) {
		return new String(e.toBytes()).trim();
	}

	public void encrypt() {
		// TODO Auto-generated method stub
		
	}

	public void decrypt() {
		// TODO Auto-generated method stub
		
	}

	public void reEncrypt() {
		// TODO Auto-generated method stub
		
	}

	public void encryptReEncryptable() {
		// TODO Auto-generated method stub
		
	}

	public void decryptReEncryptable() {
		// TODO Auto-generated method stub
		
	}

}
