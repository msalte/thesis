package no.uis.msalte.thesis.crypto;

import java.io.IOException;

import no.uis.msalte.thesis.crypto.scheme.ProxyReEncryptionParameters;
import no.uis.msalte.thesis.crypto.scheme.ProxyReEncryptionScheme;
import no.uis.msalte.thesis.crypto.scheme.ProxyReEncryptionSchemeImpl;

public class App {

	public static void main(String[] args) throws IOException {
		ProxyReEncryptionParameters parameters = new ProxyReEncryptionParameters()
				.initialize();
		ProxyReEncryptionScheme scheme = new ProxyReEncryptionSchemeImpl(
				parameters);

		String aliceSecretKey = "F1k7LYIJbBZA5QYbA0Kh43yZW8M=";
		String alicePublicKey = "LRosMdblCrGlWYbtd8nORQekWfFBTsbNtCTqrO2uHzvkqaZhH57qqDgD+UNhTNAsbyY5g+ts9mk1r8g2P9fv8Z89McAaXQ346rs100db8lvv4VC1zGYj9nkEJHPIsW7yjLxOTPspAgGWI8nrkl9fXLNm+hD7YWdYSLj9mFvOhEY=";

		String bobSecretKey = "HWYCQM0L37yvAuOJCdNHsPIqeQw=";
		String bobPublicKey = "BIwaJpLGdcdqAYfU6/OMGnDth8vFZQInbqRDYRiSvn4ACsma11QQfYAXSI3XNUQKtYvZL27dXyda/cKuYyDoWRPzamUorTM2fMBWW5KbBZZ6H0H3W8K9s9YItSPeSMaNDHkDvNec0sAQkETPs2PLXuX9GUW0lTFWM4mMWGSczSQ=";

		String aliceToBobReEncryptionKey = scheme.newReEncryptionKey(
				aliceSecretKey, bobPublicKey);

		String email = "This is a secret e-mail";

		String encryptedForAlice = scheme.encryptReEncryptable(email,
				alicePublicKey);
		String reEncryptedForBob = scheme.reEncrypt(encryptedForAlice,
				aliceToBobReEncryptionKey);

		String decryptedByAlice = scheme.decryptReEncryptable(
				encryptedForAlice, aliceSecretKey);
		String decryptedByBob = scheme.decrypt(reEncryptedForBob, bobSecretKey);

		System.out.println(decryptedByAlice);
		System.out.println(decryptedByBob);

	}

}
