package no.uis.msalte.thesis.bit_torrent;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.InetSocketAddress;

import com.turn.ttorrent.tracker.TrackedTorrent;
import com.turn.ttorrent.tracker.Tracker;

public class BitTorrentTracker {
	private Tracker tracker;
	private FilenameFilter filter;

	private static final String TORRENT_SUFFIX = ".torrent";

	public BitTorrentTracker(int port) {
		init(port);
	}

	private void init(int port) {
		try {
			tracker = new Tracker(new InetSocketAddress(port));
			filter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith(TORRENT_SUFFIX);
				}
			};
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void announceAll(File dir) {
		try {
			for (File torrent : dir.listFiles(filter)) {
				if (tracker != null) {
					tracker.announce(TrackedTorrent.load(torrent));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Tracker getTracker() {
		return tracker;
	}
}
