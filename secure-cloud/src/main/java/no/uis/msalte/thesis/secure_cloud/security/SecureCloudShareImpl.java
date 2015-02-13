package no.uis.msalte.thesis.secure_cloud.security;

import java.math.BigInteger;
import java.util.Base64;
import java.util.UUID;

import no.uis.msalte.thesis.crypto.el_gamal.ElGamalParams;
import no.uis.msalte.thesis.secure_cloud.access.AccessControl;
import no.uis.msalte.thesis.secure_cloud.storage.Persist;

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
		final String fileName = String.format("%s.%s", UUID.randomUUID()
				.toString(), ".torrent");

		final String bytes = Base64.getEncoder().encodeToString(file);

		Persist.getInstance().write(Persist.MAP_TORRENTS, fileName, bytes);

		return fileName;
	}

	public boolean share(String fileName, byte[] publicKey,
			byte[] reEncryptionKey) {
		Persist persist = Persist.getInstance();

		if (persist.hasKey(Persist.MAP_TORRENTS, fileName)) {
			String pk = Base64.getEncoder().encodeToString(publicKey);
			String rek = Base64.getEncoder().encodeToString(reEncryptionKey);

			persist.write(Persist.MAP_PUBLIC_KEYS, fileName, pk);
			persist.write(Persist.MAP_RE_ENCRYPTION_KEYS, pk, rek);

			return true;
		}

		return false;
	}

	public byte[] download(String fileName, byte[] publicKey) {
		String pk = Base64.getEncoder().encodeToString(publicKey);

		final boolean hasAccess = AccessControl.hasAccess(fileName, pk);

		if (hasAccess) {
			// String rek = AccessControl.getReEncryptionKeyFor(pk);

			String file = Persist.getInstance().read(Persist.MAP_TORRENTS,
					fileName);

			// TODO re-encrypt

			return Base64.getDecoder().decode(file);
		}

		return null;
	}

	public byte[] newTorrent(byte[] file) {
		// TODO Auto-generated method stub
		return null;
	}

}
