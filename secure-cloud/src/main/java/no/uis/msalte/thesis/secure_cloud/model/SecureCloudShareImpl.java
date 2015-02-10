package no.uis.msalte.thesis.secure_cloud.model;

import java.math.BigInteger;

import no.uis.msalte.thesis.crypto.el_gamal.ElGamalParams;
import no.uis.msalte.thesis.secure_cloud.access.AccessControl;
import no.uis.msalte.thesis.secure_cloud.storage.TorrentDirectory;

public class SecureCloudShareImpl implements SecureCloudShare {

	private TorrentDirectory torrentDirectory = new TorrentDirectory();
	private AccessControl accessControl = new AccessControl();
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
		return torrentDirectory.store(torrent.toString());
	}

	public boolean share(int id, byte[] publicKey, byte[] reEncryptionKey) {
		if (torrentDirectory.get(id) != null) {

			accessControl.grant(id, publicKey, reEncryptionKey);

			return true;
		}

		return false;
	}

	public byte[] download(int id, byte[] publicKey) {
		if (accessControl.hasAccess(id, publicKey)) {
			return torrentDirectory.get(id).getBytes();
		}

		return null;
	}

	public byte[] newTorrent(byte[] file) {
		// TODO Auto-generated method stub
		return null;
	}

}
