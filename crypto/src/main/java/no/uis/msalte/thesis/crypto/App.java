package no.uis.msalte.thesis.crypto;

import java.io.IOException;

import no.uis.msalte.thesis.crypto.scheme.ProxyReEncryptionScheme;
import no.uis.msalte.thesis.crypto.scheme.ProxyReEncryptionSchemeImpl;

public class App {

	public static void main(String[] args) throws IOException {
		ProxyReEncryptionScheme scheme = new ProxyReEncryptionSchemeImpl();

		String aliceSecretKey = scheme.newSecretKey();
		String alicePublicKey = scheme.newPublicKey(aliceSecretKey);

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
