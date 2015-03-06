package no.uis.msalte.thesis.crypto;

import no.uis.msalte.thesis.crypto.scheme.ProxyReEncryptionParameters;
import no.uis.msalte.thesis.crypto.scheme.ProxyReEncryptionSchemeImpl;

public class App {

	public static void main(String[] args) {
		ProxyReEncryptionSchemeImpl scheme = new ProxyReEncryptionSchemeImpl(new ProxyReEncryptionParameters().initialize());
		
		String aliceSecretKey = scheme.newSecretKey();
		String alicePublicKey = scheme.newPublicKey(aliceSecretKey);
		
		String bobSecretKey = scheme.newSecretKey();
		String bobPublicKey = scheme.newPublicKey(bobSecretKey);
		
		String m = "Hello this is a secret message for Alice";
		
		String cipherForAlice = scheme.encryptReEncryptable(m, alicePublicKey);

		String reEncryptionKeyAliceToBob = scheme.newReEncryptionKey(aliceSecretKey, bobPublicKey);
		
		String cipherForBob = scheme.reEncrypt(cipherForAlice, reEncryptionKeyAliceToBob);
		
		String decryptedByAlice = scheme.decryptReEncryptable(cipherForAlice, aliceSecretKey);
		
		String decryptedByBob = scheme.decrypt(cipherForBob, bobSecretKey);
		
		System.out.println(decryptedByAlice);
		System.out.println(decryptedByBob);
	}

}
