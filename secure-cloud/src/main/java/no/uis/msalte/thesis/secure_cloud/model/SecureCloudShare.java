package no.uis.msalte.thesis.secure_cloud.model;

public interface SecureCloudShare {
	/**
	 * Generates a new torrent for the given file
	 * 
	 * @param file
	 *            The file for which to generate torrent file
	 * @return The new torrent file, represented by bytes
	 */
	public byte[] newTorrent(byte[] file);

	/**
	 * Generates a new secret key
	 * 
	 * @return The new secret key, represented by bytes
	 */
	public byte[] newSecretKey();

	/**
	 * Generates a new public key
	 * 
	 * @param secretKey
	 *            The respective secret key
	 * @return The new public key, represented by bytes
	 */
	public byte[] newPublicKey(byte[] secretKey);

	/**
	 * Generates a new re-encryption key
	 * 
	 * @param secretKey
	 *            The respective secret key
	 * @param publicKey
	 *            The respective public key
	 * @return The new re-encryption key, represented by bytes
	 */
	public byte[] newReEncryptionKey(byte[] secretKey, byte[] publicKey);

	/**
	 * Upload a torrent to the system's torrent directory
	 * 
	 * @param torrent
	 *            The torrent, represented by bytes
	 * @return The file's id, generated by the system
	 */
	public int upload(byte[] torrent);

	/**
	 * Share a file in the torrent directory with another user
	 * 
	 * @param id
	 *            The id of the torrent to be shared
	 * @param publicKey
	 *            The recipient's public key
	 * @param reEncryptionKey
	 *            The respective re-encryption key
	 * @return true if success, false otherwise
	 */
	public boolean share(int id, byte[] publicKey, byte[] reEncryptionKey);

	/**
	 * Download a file from the torrent directory
	 * 
	 * @param id
	 *            The id of the torrent to download
	 * @param publicKey
	 *            The caller's public key. This must reflect an entry in the
	 *            system's share dictionary
	 * @return The torrent, represented by bytes
	 */
	public byte[] download(int id, byte[] publicKey);
}
