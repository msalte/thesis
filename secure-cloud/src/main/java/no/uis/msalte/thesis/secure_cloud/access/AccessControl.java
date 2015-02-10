package no.uis.msalte.thesis.secure_cloud.access;

import java.util.ArrayList;
import java.util.Arrays;

import no.uis.msalte.thesis.secure_cloud.model.AccessTuple;

public class AccessControl {
	private ArrayList<AccessTuple> accessList;

	public AccessControl() {
		accessList = new ArrayList<AccessTuple>();
	}

	public void grant(int id, byte[] publicKey, byte[] reEncryptionKey) {
		accessList.add(new AccessTuple(id, publicKey, reEncryptionKey));
	}

	public boolean hasAccess(int id, byte[] publicKey) {
		for (AccessTuple tuple : accessList) {
			if (tuple.getId() == id
					&& Arrays.equals(tuple.getPublicKey(), publicKey)) {
				return true;
			}
		}

		return false;
	}
}
