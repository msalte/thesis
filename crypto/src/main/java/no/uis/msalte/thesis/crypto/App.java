package no.uis.msalte.thesis.crypto;

import java.io.IOException;

import no.uis.msalte.thesis.crypto.scheme.ProxyReEncryptionParameters;
import no.uis.msalte.thesis.crypto.scheme.ProxyReEncryptionSchemeImpl;

public class App {

	public static void main(String[] args) throws IOException {
		ProxyReEncryptionSchemeImpl scheme = new ProxyReEncryptionSchemeImpl(
				new ProxyReEncryptionParameters().initialize());

		String aliceSecretKey = scheme.newSecretKey();
		String alicePublicKey = scheme.newPublicKey(aliceSecretKey);

		String bobSecretKey = scheme.newSecretKey();
		String bobPublicKey = scheme.newPublicKey(bobSecretKey);

		String message = "This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message".trim();

		String encryptedForAlice = scheme.encryptReEncryptable(message,
				alicePublicKey);
		String decryptedByAlice = scheme.decryptReEncryptable(
				encryptedForAlice, aliceSecretKey);

		String reEncryptionKeyFromAliceToBob = scheme.newReEncryptionKey(
				aliceSecretKey, bobPublicKey);

		String reEncryptedForBob = scheme.reEncrypt(encryptedForAlice,
				reEncryptionKeyFromAliceToBob);
		String decryptedByBob = scheme.decrypt(reEncryptedForBob, bobSecretKey);

		System.out.println(message);
		System.out.println(decryptedByAlice);
		System.out.println(decryptedByBob);

		System.out.println(message.equals(decryptedByAlice)
				&& message.equals(decryptedByBob));
	}

}
