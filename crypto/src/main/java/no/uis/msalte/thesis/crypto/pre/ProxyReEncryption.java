package no.uis.msalte.thesis.crypto.pre;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;

import java.math.BigInteger;

public class ProxyReEncryption {
	private ProxyReEncryptionParams params;

	public ProxyReEncryption(ProxyReEncryptionParams params) {
		this.params = params;
	}

	public Element newSecretKey() {
		return params.getZr().newRandomElement().getImmutable();
	}

	public Element newPublicKey(Element secretKey) {
		ElementPowPreProcessing g = params.getGPowPreProcessing();

		return g.powZn(secretKey).getImmutable();
	}

	public void test() {
		ElementPowPreProcessing z = params.getZPowPreProcessing();
		
		Element m = stringToElement("heisann");
		Element a = params.getZr().newRandomElement().getImmutable();
		Element k = params.getZr().newRandomElement().getImmutable();
		
		// encryption
		Element c1 = z.powZn(a.mulZn(k));
		Element c2 = m.mulZn(z.powZn(k));
	
		// decryption
		Element beta = c1.duplicate().getImmutable();
		Element alpha = c2.duplicate().getImmutable();
		
		Element one = m.duplicate().set(BigInteger.ONE).getImmutable();
		
		Element temp = one.div(a);
		
		Element recovered = beta.div(alpha.powZn(one.div(a)));

		System.out.println(m.equals(recovered));
	}

	public Tuple encryptFirstLevel(Element m, Element a) {
		// message must exist in g2

		ElementPowPreProcessing z = params.getZPowPreProcessing();

		// random k in zr
		Element k = params.getZr().newRandomElement().getImmutable();

		// set c1 and c2
		Element c1 = z.powZn(a.mulZn(k));
		Element c2 = m.mulZn(z.powZn(k));

		return new Tuple(c1, c2);
	}

	public Element decryptFirstLevel(Tuple c, Element a) {
		Element beta = c.get(1);
		Element alpha = c.get(2);
		
		Element one = a.duplicate().set(BigInteger.ONE);
		
		Element recovered = beta.div(alpha.powZn(one.div(a)));
		
		return recovered;
	}

	public Element decryptSecondLevel(Tuple ciphertext, Element secretKey) {
		Element c1 = ciphertext.get(1);
		Element c2 = ciphertext.get(2);

		Pairing e = params.getPairing();
		Element g = params.getG();

		Element m = c1.div(e.pairing(c2, g).powZn(secretKey.invert()));

		return m;
	}

	public Element stringToElement(String s) {
		Field g2 = params.getG2();

		byte[] bytes = s.getBytes();
		int maxLength = g2.getLengthInBytes();

		if (bytes.length > maxLength) {
			throw new IllegalArgumentException("Message is too long");
		}

		Element element = g2.newElement();
		element.setFromBytes(bytes);

		return element.getImmutable();
	}

	public String elementToString(Element e) {
		return new String(e.toBytes()).trim();
	}
}
