package no.uis.msalte.thesis.crypto.tests;

import static org.junit.Assert.*;
import it.unisa.dia.gas.jpbc.Element;
import no.uis.msalte.thesis.crypto.model.CipherText;
import no.uis.msalte.thesis.crypto.scheme.ProxyReEncryptionParameters;
import no.uis.msalte.thesis.crypto.scheme.ProxyReEncryptionSchemeImpl;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProxyReEncryptionSchemeTest {
	private static ProxyReEncryptionSchemeImpl scheme;

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
		Element sk = scheme.newSecretKey();
		Element pk = scheme.newPublicKey(sk);

		String message = "This is a secret";

		CipherText cipher = scheme.encrypt(message, pk);

		String decrypted = scheme.decrypt(cipher, sk);

		assertEquals(message, decrypted);
	}

	@Test
	public void testGivenEncryptedReEncryptableMessageDecryptionShouldRecoverMessage() {
		Element sk = scheme.newSecretKey();
		Element pk = scheme.newPublicKey(sk);

		String message = "This is a secret message";
		
		CipherText cipher = scheme.encryptReEncryptable(message, pk);
		
		String decrypted = scheme.decryptReEncryptable(cipher, sk);
		
		assertEquals(message, decrypted);
	}
	
	@Test
	public void testGivenReEncryptedMessageDecryptionByDelegateeShouldRecoverMessage() {
		Element aliceSecretKey = scheme.newSecretKey();
		Element alicePublicKey = scheme.newPublicKey(aliceSecretKey);

		Element bobSecretKey = scheme.newSecretKey();
		Element bobPublicKey = scheme.newPublicKey(bobSecretKey);

		String messageForAlice = "Hello Alice!";

		CipherText cipherForAlice = scheme.encryptReEncryptable(
				messageForAlice, alicePublicKey);

		Element reEncryptionKeyFromAliceToBob = scheme.newReEncryptionKey(
				aliceSecretKey, bobPublicKey);

		CipherText cipherForBob = scheme.reEncrypt(cipherForAlice,
				reEncryptionKeyFromAliceToBob);

		String bobDecryptedMessage = scheme.decrypt(cipherForBob, bobSecretKey);

		assertEquals(messageForAlice, bobDecryptedMessage);
	}

}
