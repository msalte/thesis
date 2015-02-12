package no.uis.msalte.thesis.secure_cloud.access;

import java.util.ArrayList;
import java.util.Arrays;

import no.uis.msalte.thesis.secure_cloud.model.AccessTuple;

public class AccessControl {
	private ArrayList<AccessTuple> accessList;

	public AccessControl() {
		accessList = new ArrayList<AccessTuple>();
	}

	public void grant(String id, byte[] publicKey, byte[] reEncryptionKey) {
		accessList.add(new AccessTuple(id, publicKey, reEncryptionKey));
	}

	public byte[] reEncryptionKeyFor(String id, byte[] publicKey) {
		for (AccessTuple tuple : accessList) {
			if (tuple.getId().equals(id)
					&& Arrays.equals(tuple.getPublicKey(), publicKey)) {
				return tuple.getReEncryptionKey();
			}
		}

		return null;
	}
}
