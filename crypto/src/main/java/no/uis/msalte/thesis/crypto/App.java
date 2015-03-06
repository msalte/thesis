package no.uis.msalte.thesis.crypto;

import java.util.Base64;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.field.curve.CurveElement;
import no.uis.msalte.thesis.crypto.model.CipherText;
import no.uis.msalte.thesis.crypto.scheme.ProxyReEncryptionParameters;
import no.uis.msalte.thesis.crypto.scheme.ProxyReEncryptionSchemeImpl;

public class App {

	public static void main(String[] args) {
		final ProxyReEncryptionParameters parameters = 
				new ProxyReEncryptionParameters().initialize();
		
		final ProxyReEncryptionSchemeImpl scheme = new ProxyReEncryptionSchemeImpl(parameters);
		
		Element aliceSecretKey = scheme.newSecretKey();
		Element alicePublicKey = scheme.newPublicKey(aliceSecretKey);
		
		Element bobSecretKey = scheme.newSecretKey();
		Element bobPublicKey = scheme.newPublicKey(bobSecretKey);
		
		String m = "A secret message";
		
		CipherText messageToAlice = scheme.encryptReEncryptable(m, alicePublicKey);
		
		String decryptedByAlice = scheme.decryptReEncryptable(messageToAlice, aliceSecretKey);
		
		System.out.println(decryptedByAlice);
		
		byte[] b = bobPublicKey.toBytes();
		String bpk = Base64.getEncoder().encodeToString(b);
		
		Element ce = (CurveElement<?>) parameters.getG().getElement().getField().newZeroElement();
		
		byte[] b2 = Base64.getDecoder().decode(bpk);
		ce.setFromBytes(b2);
		
		System.out.println(ce);
		Element aliceToBobReEncryptionKey = scheme.newReEncryptionKey(aliceSecretKey, ce);
		
		CipherText reEncryptedForBob = scheme.reEncrypt(messageToAlice, aliceToBobReEncryptionKey);
		
		String decryptedByBob = scheme.decrypt(reEncryptedForBob, bobSecretKey);
		
		System.out.println(decryptedByBob);
	}

}
