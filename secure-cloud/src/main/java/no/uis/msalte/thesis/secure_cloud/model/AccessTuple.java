package no.uis.msalte.thesis.secure_cloud.model;

public class AccessTuple {
	private String id;
	private byte[] publicKey;
	private byte[] reEncryptionKey;

	public AccessTuple(String id, byte[] publicKey, byte[] reEncryptionKey) {
		this.id = id;
		this.publicKey = publicKey;
		this.reEncryptionKey = reEncryptionKey;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
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
