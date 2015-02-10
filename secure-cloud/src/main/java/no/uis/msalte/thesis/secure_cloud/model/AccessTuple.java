package no.uis.msalte.thesis.secure_cloud.model;

public class AccessTuple {
	private int id;
	private byte[] publicKey;
	private byte[] reEncryptionKey;

	public AccessTuple(int id, byte[] publicKey, byte[] reEncryptionKey) {
		this.id = id;
		this.publicKey = publicKey;
		this.reEncryptionKey = reEncryptionKey;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte[] getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(byte[] publicKey) {
		this.publicKey = publicKey;
	}

	public byte[] getReEncryptionKey() {
		return reEncryptionKey;
	}

	public void setReEncryptionKey(byte[] reEncryptionKey) {
		this.reEncryptionKey = reEncryptionKey;
	}

}
