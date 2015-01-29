package no.uis.msalte.thesis.bit_torrent;

import java.io.File;

public class App {
	private static final String OWNER_DIR = "C:\\Users\\Morten\\Desktop\\ttorrent\\owner_dir";
	private static final String RECIPIENT_DIR = "C:\\Users\\Morten\\Desktop\\ttorrent\\recipient_dir";
	private static final String TORRENTS_DIR = "C:\\Users\\Morten\\Desktop\\ttorrent\\torrents";

	private static final String OWNER_FILE = "document.pdf";
	private static final String TORRENT_FILE = String.format("%s%s",
			OWNER_FILE, BitTorrentTracker.TORRENT_SUFFIX);

	private static final String TORRENT_FULL_PATH = String.format("%s\\%s",
			TORRENTS_DIR, TORRENT_FILE);

	public static void main(String[] args) {
//		BitTorrentUtil.newTorrent(OWNER_DIR, OWNER_FILE, TORRENTS_DIR, TORRENT_FILE);

		startTracker();
		share(TORRENT_FULL_PATH, OWNER_DIR);
		download(TORRENT_FULL_PATH, RECIPIENT_DIR);
	}

	private static void share(String torrentPath, String outputDir) {
		startClient(torrentPath, outputDir).getClient().share();
	}

	private static void download(String torrentPath, String outputDir) {
		startClient(torrentPath, outputDir).getClient().download();
	}

	private static BitTorrentClient startClient(String torrentPath,
			String outputDir) {
		return new BitTorrentClient(torrentPath, outputDir);
	}

	private static void startTracker() {
		BitTorrentTracker tracker = new BitTorrentTracker(6969);
		tracker.announceAll(new File(TORRENTS_DIR));
		tracker.getTracker().start();
	}
}
