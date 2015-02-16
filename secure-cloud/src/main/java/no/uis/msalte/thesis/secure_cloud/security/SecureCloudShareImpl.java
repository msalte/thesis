package no.uis.msalte.thesis.secure_cloud.security;

import java.math.BigInteger;
import java.util.UUID;

import no.uis.msalte.thesis.bit_torrent.util.TorrentUtil;
import no.uis.msalte.thesis.crypto.el_gamal.ElGamalParams;
import no.uis.msalte.thesis.secure_cloud.access.AccessControl;
import no.uis.msalte.thesis.secure_cloud.model.KeyTuple;
import no.uis.msalte.thesis.secure_cloud.storage.Persist;

public class SecureCloudShareImpl implements SecureCloudShare {
	private ElGamalParams params = new ElGamalParams(512);

	public String newSecretKey() {
		return params.newSecretKey().toString();
	}

	public String newPublicKey(String secretKey) {
		return params.newPublicKey(new BigInteger(secretKey)).toString();
	}

	public String newReEncryptionKey(String secretKey, String publicKey) {
		// TODO Auto-generated method stub
		return null;
	}

	public String upload(String file) {
		final String fileName = String.format("%s.torrent", UUID.randomUUID()
				.toString());

		Persist.getInstance().storeTorrent(fileName, file);

		return fileName;
	}

	public boolean share(String fileName, String publicKey,
			String reEncryptionKey) {
		return Persist.getInstance().storeKeysTuple(fileName,
				new KeyTuple(publicKey, reEncryptionKey));
	}

	public String download(String fileName, String publicKey) {
		final String reEncryptionKey = AccessControl.getReEncryptionKey(fileName, publicKey);
		
		final boolean hasAccess = reEncryptionKey != null;

		if (hasAccess) {
			String file = Persist.getInstance().readTorrent(fileName);

			// TODO re-encrypt

			return file;
		}

		return null;
	}

	public String newTorrent(String file, String extension) {
		String fileName = UUID.randomUUID().toString();
		
		return TorrentUtil.create(fileName, extension, file);
	}

}
