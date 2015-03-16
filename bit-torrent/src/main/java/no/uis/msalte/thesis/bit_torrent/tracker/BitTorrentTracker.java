package no.uis.msalte.thesis.bit_torrent.tracker;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.InetSocketAddress;

import com.turn.ttorrent.tracker.TrackedTorrent;
import com.turn.ttorrent.tracker.Tracker;

public class BitTorrentTracker {
	private Tracker tracker;
	private FilenameFilter filter;
	private int port;
	private boolean isStarted;

	public BitTorrentTracker(int port) {
		this.port = port;
	}

	public BitTorrentTracker start() {
		if (!isStarted) {
			try {
				tracker = new Tracker(new InetSocketAddress(port));
				filter = new FilenameFilter() {
					public boolean accept(File dir, String name) {
						return name.endsWith(".torrent");
					}
				};

				tracker.start();
			} catch (IOException e) {
				e.printStackTrace();
			}

			isStarted = true;
		}

		return this;
	}

	public void stop() {
		if (isStarted) {
			tracker.stop();

			isStarted = false;
		}
	}

	public void announceAll(File directory) {
		for (File torrent : directory.listFiles(filter)) {
			announce(torrent);
		}
	}

	public void announce(File torrent) {
		if (tracker != null) {
			try {
				tracker.announce(TrackedTorrent.load(torrent));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isStarted() {
		return isStarted;
	}

	public Tracker getTracker() {
		return tracker;
	}
}
