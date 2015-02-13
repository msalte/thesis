package no.uis.msalte.thesis.secure_cloud.security;

import java.math.BigInteger;
import java.util.UUID;

import no.uis.msalte.thesis.crypto.el_gamal.ElGamalParams;
import no.uis.msalte.thesis.secure_cloud.access.AccessControl;
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

		Persist.getInstance().write(Persist.MAP_TORRENTS, fileName, file);

		return fileName;
	}

	public boolean share(String fileName, String publicKey,
			String reEncryptionKey) {

		final boolean torrentExists = Persist.getInstance().hasKey(
				Persist.MAP_TORRENTS, fileName);

		if (torrentExists) {
			
			Persist.getInstance().write(Persist.MAP_PUBLIC_KEYS, fileName,
					publicKey);
			
			Persist.getInstance().write(Persist.MAP_RE_ENCRYPTION_KEYS,
					publicKey, reEncryptionKey);

			return true;
		}

		return false;
	}

	public String download(String fileName, String publicKey) {
		
		final boolean hasAccess = AccessControl.hasAccess(fileName, publicKey);

		if (hasAccess) {
			// String rek = AccessControl.getReEncryptionKeyFor(pk);

			String file = Persist.getInstance().read(Persist.MAP_TORRENTS,
					fileName);

			// TODO re-encrypt

			return file;
		}

		return null;
	}

	public String newTorrent(String file) {
		// TODO Auto-generated method stub
		return null;
	}

}
