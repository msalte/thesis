package no.uis.msalte.thesis.bit_torrent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.turn.ttorrent.common.Torrent;

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
//		createTorrent(OWNER_DIR, OWNER_FILE, TORRENTS_DIR, TORRENT_FILE);

		startTracker();
		share(TORRENT_FULL_PATH, OWNER_DIR);
		download(TORRENT_FULL_PATH, RECIPIENT_DIR);
	}

	private static void createTorrent(String sourceDir, String sourceFile,
			String torrentDir, String torrentFile) {
		File input = new File(String.format("%s\\%s", sourceDir, sourceFile));
		File output = new File(String.format("%s\\%s", torrentDir, torrentFile));

		URI announce = URI.create("http://localhost:6969/announce");

		List<List<URI>> announceTiers = new ArrayList<List<URI>>();
		List<URI> announceTier1 = new ArrayList<URI>();

		announceTier1.add(announce);
		announceTiers.add(announceTier1);

		int pieceLength = 256 * 1000; // 256KB

		try {
			FileOutputStream fos = new FileOutputStream(output);
			Torrent.create(input, pieceLength, announceTiers,
					App.class.getPackage().getName()).save(fos);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
