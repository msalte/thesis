package no.uis.msalte.thesis.secure_cloud.access;

import no.uis.msalte.thesis.secure_cloud.storage.Persist;

public class AccessControl {
	public static boolean hasAccess(String torrent, String publicKey) {

		final boolean torrentExists = Persist.getInstance().hasKey(
				Persist.MAP_TORRENTS, torrent);

		final boolean hasCorrespondingPublicKey = Persist.getInstance()
				.hasValue(Persist.MAP_PUBLIC_KEYS, torrent, publicKey);

		return torrentExists && hasCorrespondingPublicKey;
	}

	public static String getReEncryptionKeyFor(String publicKey) {
		return Persist.getInstance().read(Persist.MAP_RE_ENCRYPTION_KEYS,
				publicKey);
	}
}
