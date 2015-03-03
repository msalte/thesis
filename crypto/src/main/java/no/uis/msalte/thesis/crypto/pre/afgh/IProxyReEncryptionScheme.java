package no.uis.msalte.thesis.crypto.pre.afgh;

import it.unisa.dia.gas.jpbc.Element;

public interface IProxyReEncryptionScheme {

	public Element newSecretKey();

	public Element newPublicKey(Element secretKey);

	public Element newReEncryptionKey(Element srcSecretKey,
			Element destPublicKey);

	public Element stringToElement(String m);

	public String elementToString(Element e);

}
