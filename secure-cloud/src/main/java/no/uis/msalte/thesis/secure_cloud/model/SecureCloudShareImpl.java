package no.uis.msalte.thesis.secure_cloud.model;

import java.math.BigInteger;

import no.uis.msalte.thesis.crypto.el_gamal.ElGamalParams;

public class SecureCloudShareImpl implements SecureCloudShare {
	
	private ElGamalParams params = new ElGamalParams(512);

	public byte[] newSecretKey() {
		return params.newSecretKey().toByteArray();
	}

	public byte[] newPublicKey(byte[] secretKey) {
		return params.newPublicKey(new BigInteger(secretKey)).toByteArray();
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
