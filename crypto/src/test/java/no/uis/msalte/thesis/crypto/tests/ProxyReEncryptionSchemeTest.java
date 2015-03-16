package no.uis.msalte.thesis.crypto.tests;

import static org.junit.Assert.assertEquals;
import no.uis.msalte.thesis.crypto.scheme.ProxyReEncryptionScheme;
import no.uis.msalte.thesis.crypto.scheme.ProxyReEncryptionSchemeImpl;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProxyReEncryptionSchemeTest {
	private static ProxyReEncryptionScheme scheme;
	private static final String MESSAGE = "This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message This message is repeated many times to make a long message".trim();
	
	@BeforeClass
	public static void createSchemeInstance() {
		scheme = new ProxyReEncryptionSchemeImpl();
	}

	@AfterClass
	public static void nullSchemeInstance() {
		scheme = null;
	}

	@Test
	public void testGivenEncryptedMessageDecryptionShouldRecoverMessage() {
		String sk = scheme.newSecretKey();
		String pk = scheme.newPublicKey(sk);
		
		String cipher = scheme.encrypt(MESSAGE, pk);

		String decrypted = scheme.decrypt(cipher, sk);

		assertEquals(MESSAGE, decrypted);
	}

	@Test
	public void testGivenEncryptedReEncryptableMessageDecryptionShouldRecoverMessage() {
		String sk = scheme.newSecretKey();
		String pk = scheme.newPublicKey(sk);
		
		String cipher = scheme.encryptReEncryptable(MESSAGE, pk);
		
		String decrypted = scheme.decryptReEncryptable(cipher, sk);
		
		assertEquals(MESSAGE, decrypted);
	}
	
	@Test
	public void testGivenReEncryptedMessageDecryptionByDelegateeShouldRecoverMessage() {
		String aliceSecretKey = scheme.newSecretKey();
		String alicePublicKey = scheme.newPublicKey(aliceSecretKey);

		String bobSecretKey = scheme.newSecretKey();
		String bobPublicKey = scheme.newPublicKey(bobSecretKey);

		String cipherForAlice = scheme.encryptReEncryptable(
				MESSAGE, alicePublicKey);

		String reEncryptionKeyFromAliceToBob = scheme.newReEncryptionKey(
				aliceSecretKey, bobPublicKey);

		String cipherForBob = scheme.reEncrypt(cipherForAlice,
				reEncryptionKeyFromAliceToBob);

		String bobDecryptedMessage = scheme.decrypt(cipherForBob, bobSecretKey);

		assertEquals(MESSAGE, bobDecryptedMessage);
	}

}
