package no.uis.msalte.thesis.crypto;

import it.unisa.dia.gas.jpbc.Element;
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
		
		Element aliceToBobReEncryptionKey = scheme.newReEncryptionKey(aliceSecretKey, bobPublicKey);
		
		CipherText reEncryptedForBob = scheme.reEncrypt(messageToAlice, aliceToBobReEncryptionKey);
		
		String decryptedByBob = scheme.decrypt(reEncryptedForBob, bobSecretKey);
		
		System.out.println(decryptedByBob);
	}

}
