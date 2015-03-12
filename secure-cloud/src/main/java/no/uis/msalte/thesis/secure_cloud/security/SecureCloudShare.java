package no.uis.msalte.thesis.secure_cloud.security;

import java.io.File;

public interface SecureCloudShare {
	/**
	 * Generates a new torrent for the given file
	 * 
	 * @param file
	 *            The source file
	 * @param extension
	 *            The file's extension, i.e. mp3
	 * @return The generated torrent, represented by a Base64 encoded string
	 */
	public String newTorrent(File file, String extension);

	/**
	 * Generates a new secret key
	 * 
	 * @return The new secret key, represented by a Base64 encoded string
	 */
	public String newSecretKey();

	/**
	 * Generates a new public key for the given secret key
	 * 
	 * @param secretKey
	 *            The secret key for which to generate a public key
	 * @return The new public key, represented by a Base64 encoded string
	 */
	public String newPublicKey(String secretKey);

	/**
	 * Generates a new re-encryption key for the given key pair
	 * 
	 * @param srcSecretKey
	 *            The respective secret key
	 * @param destPublicKey
	 *            The respective public key
	 * @return The new re-encryption key, represented by a Base64 encoded string
	 */
	public String newReEncryptionKey(String srcSecretKey, String destPublicKey);

	/**
	 * Upload a torrent file to the system
	 * 
	 * @param file
	 *            The torrent file
	 * @param publicKey
	 *            The file owner's public key
	 * @return The uploaded torrent's unique file name, generated by the system
	 */
	public String upload(File file, String publicKey);

	/**
	 * Share a torrent in the system with a particular public key holder
	 * 
	 * @param fileName
	 *            The name of the torrent file to share
	 * @param publicKey
	 *            The public key of the recipient
	 * @param reEncryptionKey
	 *            The respective re-encryption key
	 * @return true if successful, false otherwise
	 */
	public boolean share(String fileName, String publicKey,
			String reEncryptionKey);

	/**
	 * Download a torrent from the system
	 * 
	 * @param fileName
	 *            The name of the torrent file to download
	 * @param publicKey
	 *            The public key of the caller, used to determine access rights
	 * @return The torrent file, represented by a Base64 encoded string
	 */
	public String download(String fileName, String publicKey);
}
