package no.uis.msalte.thesis.secure_cloud.security;

import java.math.BigInteger;
import java.util.UUID;

import no.uis.msalte.thesis.crypto.el_gamal.ElGamalParams;
import no.uis.msalte.thesis.secure_cloud.access.AccessControl;
import no.uis.msalte.thesis.secure_cloud.storage.Persist;
import no.uis.msalte.thesis.secure_cloud.util.FilesUtil;

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

	public String upload(byte[] file) {
		final String fileName = String.format("%s.torrent", UUID.randomUUID()
				.toString());

		final String bytes = FilesUtil.encode(file);

		Persist.getInstance().write(Persist.MAP_TORRENTS, fileName, bytes);

		return fileName;
	}

	public boolean share(String fileName, byte[] publicKey,
			byte[] reEncryptionKey) {
		
		final boolean torrentExists = Persist.getInstance().hasKey(
				Persist.MAP_TORRENTS, fileName);

		if (torrentExists) {
			String pk = FilesUtil.encode(publicKey);
			String rek = FilesUtil.encode(reEncryptionKey);

			Persist.getInstance().write(Persist.MAP_PUBLIC_KEYS, fileName, pk);
			Persist.getInstance()
					.write(Persist.MAP_RE_ENCRYPTION_KEYS, pk, rek);

			return true;
		}

		return false;
	}

	public byte[] download(String fileName, byte[] publicKey) {
		String pk = FilesUtil.encode(publicKey);

		final boolean hasAccess = AccessControl.hasAccess(fileName, pk);

		if (hasAccess) {
			// String rek = AccessControl.getReEncryptionKeyFor(pk);

			String file = Persist.getInstance().read(Persist.MAP_TORRENTS,
					fileName);

			// TODO re-encrypt

			return FilesUtil.decode(file);
		}

		return null;
	}

	public byte[] newTorrent(byte[] file) {
		// TODO Auto-generated method stub
		return null;
	}

}
