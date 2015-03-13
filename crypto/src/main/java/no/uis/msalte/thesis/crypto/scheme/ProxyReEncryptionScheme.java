package no.uis.msalte.thesis.crypto.scheme;

/**
 * An implementation of the AFGH proxy re-encryption scheme
 * @author Morten
 *
 */
public interface ProxyReEncryptionScheme {
	/**
	 * Generates a new secret key
	 * 
	 * @return The new secret key
	 */
	public String newSecretKey();
	
	/**
	 * Generates a new public key
	 * 
	 * @param secretKey
	 *            The corresponding secret key
	 * @return The new public key
	 */
	public String newPublicKey(String secretKey);

	/**
	 * Generates a new re-encryption key
	 * 
	 * @param srcSecretKey
	 *            The source's secret key
	 * @param destPublicKey
	 *            The destination's public key
	 * @return The new re-encryption key
	 */
	public String newReEncryptionKey(String srcSecretKey,
			String destPublicKey);

	/**
	 * Encrypts a message in a LVL 1 fashion, in accordance to the AFGH scheme
	 * 
	 * @param message
	 *            The message to encrypt
	 * @param destPublicKey
	 *            The destination's public key
	 * @return The encrypted message
	 */
	public String encrypt(String message, String destPublicKey);

	/**
	 * Decrypts a message encrypted/re-encrypted in a LVL 1 fashion, 
	 * in accordance to the AFGH scheme
	 * 
	 * @param cipher
	 *            The cipher text to decrypt
	 * @param destSecretKey
	 *            The destination's secret key
	 * @return The decrypted message
	 */
	public String decrypt(String cipher, String destSecretKey);

	/**
	 * Re-encrypts a message that is encrypted in a LVL 2 fashion, in accordance
	 * to the AFGH scheme. Note that the resulting cipher will become a LVL 1 cipher
	 * 
	 * @param cipher
	 *            The cipher text to re-encrypt
	 * @param reEncryptionKey
	 *            The re-encryption key
	 * @return The re-encrypted message, now a LVL 1 cipher
	 */
	public String reEncrypt(String cipher, String reEncryptionKey);

	/**
	 * Encrypts a message in a LVL 2 fashion, in accordance to the AFGH scheme
	 * 
	 * @param message
	 *            The message to encrypt
	 * @param destPublicKey
	 *            The destination's public key
	 * @return The encrypted message
	 */
	public String encryptReEncryptable(String message,
			String destPublicKey);

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
	public String decryptReEncryptable(String cipher,
			String destSecretKey);

}
