package no.uis.msalte.thesis.secure_cloud.storage;

import java.util.HashMap;
import java.util.UUID;

public class TorrentDirectory {
	private HashMap<String, byte[]> torrents;

	public TorrentDirectory() {
		torrents = new HashMap<String, byte[]>();
	}

	public String store(byte[] value) {
		final String key = UUID.randomUUID().toString();

		torrents.put(key, value);

		return key;
	}

	public byte[] get(String key) {
		return torrents.get(key);
	}
}