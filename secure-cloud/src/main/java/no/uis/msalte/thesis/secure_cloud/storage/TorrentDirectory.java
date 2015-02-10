package no.uis.msalte.thesis.secure_cloud.storage;

import java.util.HashMap;

public class TorrentDirectory {

	private int nextId;
	private HashMap<Integer, String> torrents;

	public TorrentDirectory() {
		nextId = 1;
		torrents = new HashMap<Integer, String>();
	}

	public int store(String bytes) {
		torrents.put(nextId, bytes);

		return nextId++;
	}

	public String get(int id) {
		return torrents.get(id);
	}
}