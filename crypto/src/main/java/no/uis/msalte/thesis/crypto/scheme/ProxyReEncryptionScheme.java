package no.uis.msalte.thesis.crypto.scheme;

import no.uis.msalte.thesis.crypto.model.CipherText;
import it.unisa.dia.gas.jpbc.Element;

public interface ProxyReEncryptionScheme {
	/**
	 * Generates a new secret key
	 * 
	 * @return The new secret key
	 */
	public Element newSecretKey();
	
	/**
	 * Generates a new public key
	 * 
	 * @param secretKey
	 *            The corresponding secret key
	 * @return The new public key
	 */
	public Element newPublicKey(Element secretKey);

	/**
	 * Generates a new re-encryption key
	 * 
	 * @param srcSecretKey
	 *            The source's secret key
	 * @param destPublicKey
	 *            The destination's public key
	 * @return The new re-encryption key
	 */
	public Element newReEncryptionKey(Element srcSecretKey,
			Element destPublicKey);

	/**
	 * Encrypts a message in a LVL 1 fashion, in accordance to the AFGH scheme
	 * 
	 * @param message
	 *            The message to encrypt
	 * @param destPublicKey
	 *            The destination's public key
	 * @return The encrypted message
	 */
	public CipherText encrypt(String message, Element destPublicKey);

	/**
	 * Decrypts a message encrypted in a LVL 1 fashion, in accordance to the
	 * AFGH scheme
	 * 
	 * @param cipher
	 *            The cipher text to decrypt
	 * @param destSecretKey
	 *            The destination's secret key
	 * @return The decrypted message
	 */
	public String decrypt(CipherText cipher, Element destSecretKey);

	/**
	 * Re-encrypts a message that is encrypted in a LVL 2 fashion, in accordance
	 * to the AFGH scheme
	 * 
	 * @param cipher
	 *            The cipher text to re-encrypt
	 * @param reEncryptionKey
	 *            The re-encryption key
	 * @return The re-encrypted message
	 */
	public CipherText reEncrypt(CipherText cipher, Element reEncryptionKey);

	/**
	 * Encrypts a message in a LVL 2 fashion, in accordance to the AFGH scheme
	 * 
	 * @param message
	 *            The message to encrypt
	 * @param destPublicKey
	 *            The destination's public key
	 * @return The encrypted message
	 */
	public CipherText encryptReEncryptable(String message,
			Element destPublicKey);

	/**
	 * Decrypts a message encrypted in a LVL 2 fashion, in accordance to the
	 * AFGH scheme
	 * 
	 * @param cipher
	 *            The cipher text to decrypt
	 * @param destSecretKey
	 *            The destination's secret key
	 * @return The decrypted message
	 */
	public String decryptReEncryptable(CipherText cipher,
			Element destSecretKey);

}
