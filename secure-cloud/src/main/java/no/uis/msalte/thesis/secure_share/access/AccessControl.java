package no.uis.msalte.thesis.secure_share.access;

import no.uis.msalte.thesis.secure_share.model.KeyTuple;
import no.uis.msalte.thesis.secure_share.storage.Persist;

public class AccessControl {
	
	public static String getReEncryptionKey(String fileName, String publicKey) {

		final boolean torrentExists = Persist.getInstance().readTorrent(fileName) != null;

		if(torrentExists) {
			for(KeyTuple tuple : Persist.getInstance().readKeyTuples(fileName)) {
				if(tuple.getPublicKey().equals(publicKey)) {
					return tuple.getReEncryptionKey();
				}
			}
		}
		
		return null;
	}
}
