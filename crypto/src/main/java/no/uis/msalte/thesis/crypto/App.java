package no.uis.msalte.thesis.crypto;

import no.uis.msalte.thesis.crypto.model.CipherText;
import no.uis.msalte.thesis.crypto.scheme.ProxyReEncryptionParameters;
import no.uis.msalte.thesis.crypto.scheme.ProxyReEncryptionScheme;
import no.uis.msalte.thesis.crypto.scheme.ProxyReEncryptionSchemeImpl;

public class App {

	public static void main(String[] args) {
		ProxyReEncryptionScheme scheme = new ProxyReEncryptionSchemeImpl(new ProxyReEncryptionParameters().initialize());
		
		String secretKey = scheme.newSecretKey();
		String publicKey = scheme.newPublicKey(secretKey);
		
		System.out.println(secretKey);
		System.out.println(publicKey);
		
		String message = "Denne meldingen er hemmelig!!!!!";
		
		CipherText ct = scheme.encrypt(message, publicKey);
		
		String decrypted = scheme.decrypt(ct, secretKey);
		
		System.out.println(decrypted);
	}

}
