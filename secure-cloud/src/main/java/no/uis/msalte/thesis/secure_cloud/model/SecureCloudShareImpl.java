package no.uis.msalte.thesis.secure_cloud.model;

public class SecureCloudShareImpl implements SecureCloudShare {

	public byte[] newSecretKey() {
		// TODO Auto-generated method stub
		return null;
	}

	public byte[] newPublicKey(byte[] secretKey) {
		// TODO Auto-generated method stub
		return null;
	}

	public byte[] newReEncryptionKey(byte[] secretKey, byte[] publicKey) {
		// TODO Auto-generated method stub
		return null;
	}

	public int upload(byte[] torrent) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void share(int id, byte[] preKey, byte[] publicKey) {
		// TODO Auto-generated method stub

	}

	public byte[] download(int id, byte[] publicKey) {
		// TODO Auto-generated method stub
		return null;
	}

	public byte[] newTorrent(byte[] file) {
		// TODO Auto-generated method stub
		return null;
	}

}
