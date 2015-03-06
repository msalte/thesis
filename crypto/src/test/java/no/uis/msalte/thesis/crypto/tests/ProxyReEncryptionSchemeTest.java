package no.uis.msalte.thesis.crypto.tests;

import static org.junit.Assert.assertEquals;
import no.uis.msalte.thesis.crypto.scheme.ProxyReEncryptionParameters;
import no.uis.msalte.thesis.crypto.scheme.ProxyReEncryptionScheme;
import no.uis.msalte.thesis.crypto.scheme.ProxyReEncryptionSchemeImpl;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProxyReEncryptionSchemeTest {
	private static ProxyReEncryptionScheme scheme;

	@BeforeClass
	public static void createSchemeInstance() {
		scheme = new ProxyReEncryptionSchemeImpl(
				new ProxyReEncryptionParameters().initialize());
	}

	@AfterClass
	public static void nullSchemeInstance() {
		scheme = null;
	}

	@Test
	public void testGivenEncryptedMessageDecryptionShouldRecoverMessage() {
		String sk = scheme.newSecretKey();
		String pk = scheme.newPublicKey(sk);

		String message = "This is a secret";

		String cipher = scheme.encrypt(message, pk);

		String decrypted = scheme.decrypt(cipher, sk);

		assertEquals(message, decrypted);
	}

	@Test
	public void testGivenEncryptedReEncryptableMessageDecryptionShouldRecoverMessage() {
		String sk = scheme.newSecretKey();
		String pk = scheme.newPublicKey(sk);

		String message = "This is a secret message";
		
		String cipher = scheme.encryptReEncryptable(message, pk);
		
		String decrypted = scheme.decryptReEncryptable(cipher, sk);
		
		assertEquals(message, decrypted);
	}
	
	@Test
	public void testGivenReEncryptedMessageDecryptionByDelegateeShouldRecoverMessage() {
		String aliceSecretKey = scheme.newSecretKey();
		String alicePublicKey = scheme.newPublicKey(aliceSecretKey);

		String bobSecretKey = scheme.newSecretKey();
		String bobPublicKey = scheme.newPublicKey(bobSecretKey);

		String messageForAlice = "Hello Alice!";

		String cipherForAlice = scheme.encryptReEncryptable(
				messageForAlice, alicePublicKey);

		String reEncryptionKeyFromAliceToBob = scheme.newReEncryptionKey(
				aliceSecretKey, bobPublicKey);

		String cipherForBob = scheme.reEncrypt(cipherForAlice,
				reEncryptionKeyFromAliceToBob);

		String bobDecryptedMessage = scheme.decrypt(cipherForBob, bobSecretKey);

		assertEquals(messageForAlice, bobDecryptedMessage);
	}

}
