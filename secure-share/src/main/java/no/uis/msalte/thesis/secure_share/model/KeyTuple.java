package no.uis.msalte.thesis.secure_share.model;

public class KeyTuple {
	private String publicKey;
	private String reEncryptionKey;

	public KeyTuple(String publicKey, String reEncryptionKey) {
		this.publicKey = publicKey;
		this.reEncryptionKey = reEncryptionKey;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getReEncryptionKey() {
		return reEncryptionKey;
	}

	public void setReEncryptionKey(String reEncryptionKey) {
		this.reEncryptionKey = reEncryptionKey;
	}
}
