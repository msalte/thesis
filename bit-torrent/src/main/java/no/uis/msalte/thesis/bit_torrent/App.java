package no.uis.msalte.thesis.bit_torrent;

import java.io.File;

public class App {
	private static final String SOURCE_DIR = "C:\\Users\\Morten\\Desktop\\ttorrent\\source_dir";
	private static final String RECIPIENT_DIR = "C:\\Users\\Morten\\Desktop\\ttorrent\\recipient_dir";
	private static final String TORRENTS_DIR = "C:\\Users\\Morten\\Desktop\\ttorrent\\torrents";

	private static final String TORRENT = "2012-12-21 12.07.56.jpg.torrent";

	private static final String TORRENT_PATH = String.format("%s\\%s",
			TORRENTS_DIR, TORRENT);

	public static void main(String[] args) {
		startTracker();

		share(TORRENT_PATH, SOURCE_DIR);
		download(TORRENT_PATH, RECIPIENT_DIR);
	}

	private static void share(String torrent, String dir) {
		startClient(torrent, dir).getClient().share();
	}

	private static void download(String torrent, String dir) {
		startClient(torrent, dir).getClient().download();
	}

	private static BitTorrentClient startClient(String torrent, String dir) {
		return new BitTorrentClient(torrent, dir);
	}

	private static void startTracker() {
		BitTorrentTracker tracker = new BitTorrentTracker(6969);
		tracker.announceAll(new File(TORRENTS_DIR));
		tracker.getTracker().start();
	}
}
