package no.uis.msalte.thesis.secure_cloud.security;

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

	public String upload(byte[] torrent) {
		return torrentDirectory.store(torrent);
	}

	public boolean share(String id, byte[] publicKey, byte[] reEncryptionKey) {
		if (torrentDirectory.get(id) != null) {

			accessControl.grant(id, publicKey, reEncryptionKey);

			return true;
		}

		return false;
	}

	public byte[] download(String id, byte[] publicKey) {
		final byte[] reEncryptionKey = accessControl.reEncryptionKeyFor(id,
				publicKey);

		final boolean hasAccess = reEncryptionKey != null;

		if (hasAccess) {
			byte[] torrent = torrentDirectory.get(id);

			// TODO perform re-encryption

			return torrent;
		}

		return null;
	}

	public byte[] newTorrent(byte[] file) {
		// TODO Auto-generated method stub
		return null;
	}

}
