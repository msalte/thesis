package no.uis.msalte.thesis.bit_torrent.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import no.uis.msalte.thesis.bit_torrent.App;

import com.turn.ttorrent.common.Torrent;

public class BitTorrentUtil {
	private static final int PIECE_LENGTH = 256 * 1000;
	private static final String ANNOUNCE_URI = "http://localhost:6969/announce";

	/**
	 * Creates and stores a new torrent file for the given source file
	 * 
	 * @param sourceDir
	 *            The path to the directory containing the source file
	 * @param sourceFile
	 *            The name of the source file
	 * @param torrentDir
	 *            The path to the directory in which the resulting torrent
	 *            should be saved
	 * @param torrentFile
	 *            The name of the resulting torrent
	 */
	public static void newTorrent(String sourceDir, String sourceFile,
			String torrentDir, String torrentFile) {
		File input = new File(String.format("%s\\%s", sourceDir, sourceFile));
		File output = new File(String.format("%s\\%s", torrentDir, torrentFile));

		URI announce = URI.create(ANNOUNCE_URI);

		List<List<URI>> announceTiers = new ArrayList<List<URI>>();
		List<URI> announceTier1 = new ArrayList<URI>();

		announceTier1.add(announce);
		announceTiers.add(announceTier1);

		try {
			FileOutputStream fos = new FileOutputStream(output);
			Torrent.create(input, PIECE_LENGTH, announceTiers,
					App.class.getPackage().getName()).save(fos);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
