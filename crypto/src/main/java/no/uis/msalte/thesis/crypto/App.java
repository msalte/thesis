package no.uis.msalte.thesis.crypto;

import no.uis.msalte.thesis.crypto.pre.afgh.ProxyReEncryptionParameters;
import no.uis.msalte.thesis.crypto.pre.afgh.ProxyReEncryptionScheme;

public class App {

	public static void main(String[] args) {
		final ProxyReEncryptionScheme scheme = new ProxyReEncryptionScheme(
				new ProxyReEncryptionParameters().initialize());

		// Element bobSecretKey = scheme.newSecretKey();
		// Element bobPublicKey = scheme.newPublicKey(bobSecretKey);
		//
		// Element aliceSecretKey = scheme.newSecretKey();
		// Element alicePublicKey = scheme.newPublicKey(aliceSecretKey);
		//
		// Element reEncryptAliceToBob =
		// scheme.newReEncryptionKey(aliceSecretKey, bobPublicKey);

		String message = "Hello there!";

		scheme.stringToElement(message);
	}

}
