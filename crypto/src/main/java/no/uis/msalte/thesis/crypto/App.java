package no.uis.msalte.thesis.crypto;

import no.uis.msalte.thesis.crypto.scheme.ProxyReEncryptionParameters;
import no.uis.msalte.thesis.crypto.scheme.ProxyReEncryptionSchemeImpl;

public class App {

	public static void main(String[] args) {
		ProxyReEncryptionSchemeImpl scheme = new ProxyReEncryptionSchemeImpl(
				new ProxyReEncryptionParameters().initialize());

		String secretKey = scheme.newSecretKey();
		String publicKey = scheme.newPublicKey(secretKey);
		
		String message = "This message is short but it is pretty long still This message is short but it is pretty long still This message is short but it is pretty long still";
		
		String encrypted = scheme.encrypt(message, publicKey);
		String decrypted = scheme.decrypt(encrypted, secretKey);
		
		System.out.println(encrypted);
		System.out.println(decrypted);
		
		System.out.println(message.equals(decrypted));
	}

}
