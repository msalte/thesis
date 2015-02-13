package no.uis.msalte.thesis.secure_cloud.access;

import no.uis.msalte.thesis.secure_cloud.storage.Persist;

public class AccessControl {
	public static boolean hasAccess(String torrent, String publicKey) {
		Persist p = Persist.getInstance();

		boolean torrentExists = p.hasKey(Persist.MAP_TORRENTS, torrent);
		boolean hasPublicKey = p.hasValue(Persist.MAP_PUBLIC_KEYS, torrent,
				publicKey);

		return torrentExists && hasPublicKey;
	}

	public static String getReEncryptionKeyFor(String publicKey) {
		return Persist.getInstance().read(Persist.MAP_RE_ENCRYPTION_KEYS,
				publicKey);
	}
}
