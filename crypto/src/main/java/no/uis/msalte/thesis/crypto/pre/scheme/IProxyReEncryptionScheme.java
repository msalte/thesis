package no.uis.msalte.thesis.crypto.pre.scheme;

import no.uis.msalte.thesis.crypto.pre.model.CipherTuple;
import it.unisa.dia.gas.jpbc.Element;

public interface IProxyReEncryptionScheme {
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
	public CipherTuple encrypt(Element message, Element destPublicKey);

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
	public Element decrypt(CipherTuple cipher, Element destSecretKey);

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
	public CipherTuple reEncrypt(CipherTuple cipher, Element reEncryptionKey);

	/**
	 * Encrypts a message in a LVL 2 fashion, in accordance to the AFGH scheme
	 * 
	 * @param message
	 *            The message to encrypt
	 * @param destPublicKey
	 *            The destination's public key
	 * @return The encrypted message
	 */
	public CipherTuple encryptReEncryptable(Element message,
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
	public Element decryptReEncryptable(CipherTuple cipher,
			Element destSecretKey);

	/**
	 * Converts a message into an element. The message exists in G2
	 * 
	 * @param message
	 *            The message to convert
	 * @return The converted message
	 */
	public Element messageToElement(String message);

	/**
	 * Converts an element into a message.
	 * 
	 * @param element
	 *            The element to convert
	 * @return The converted element
	 */
	public String elementToMessage(Element element);

}
