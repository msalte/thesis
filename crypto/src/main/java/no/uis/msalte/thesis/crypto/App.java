package no.uis.msalte.thesis.crypto;

import no.uis.msalte.thesis.crypto.scheme.ProxyReEncryptionParameters;
import no.uis.msalte.thesis.crypto.scheme.ProxyReEncryptionSchemeImpl;

public class App {

	public static void main(String[] args) {
		ProxyReEncryptionSchemeImpl scheme = new ProxyReEncryptionSchemeImpl(
				new ProxyReEncryptionParameters().initialize());

		String secretKey = scheme.newSecretKey();
		String publicKey = scheme.newPublicKey(secretKey);
		
		String message = "Fuku bla bla bla aokaw oqkwe";
		
		String encrypted = scheme.encrypt(message, publicKey);
		String decrypted = scheme.decrypt(encrypted, secretKey);
		
		System.out.println(encrypted);
		System.out.println(decrypted);
	}

}
