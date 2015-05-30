package no.uis.msalte.thesis.bit_torrent.util;

import static no.uis.msalte.thesis.common.AppConstants.ANNOUNCE_URI;
import static no.uis.msalte.thesis.common.AppConstants.PIECE_LENGTH;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import com.turn.ttorrent.common.Torrent;

public class TorrentUtils {

	public static String createBase64(String name, String token, File file) {
		File torrent = null;
		FileOutputStream fos = null;

		try {
			// Create a temporary file in the file system to
			// represent the new torrent
			torrent = File
					.createTempFile(name, String.format(".%s", "torrent"));

			// Write the torrent contents into the temporary file
			fos = new FileOutputStream(torrent);
			writeBencodedContents(file, token, fos);

			fos.flush();
			fos.close();

			// Return an encoded version of the created torrent file
			byte[] bytes = Files.readAllBytes(Paths.get(torrent
					.getAbsolutePath()));

			return Base64.getEncoder().encodeToString(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			// Delete the torrent from the file system
			if (torrent != null) {
				torrent.delete();
			}
		}

		return null;
	}

	public static boolean isValidTorrent(File torrent) {
		try {
			// The torrent is valid if it contains an announce list
			return Torrent.load(torrent).getAnnounceList() != null;
		} catch (IOException e) {
			return false;
		}
	}

	public static boolean isBase64ValidTorrent(String torrent) {
		FileOutputStream fos = null;
		File tmp = null;

		try {
			final byte[] b = Base64.getDecoder().decode(torrent);

			tmp = File.createTempFile("temp", null);

			fos = new FileOutputStream(tmp);
			fos.write(b);
			fos.flush();
			fos.close();

			// The torrent is valid if it contains an announce list
			return Torrent.load(tmp).getAnnounceList() != null;
		} catch (Exception e) {
			return false;
		} finally {
			if (tmp != null) {
				tmp.delete();
			}
		}
	}

	private static Torrent writeBencodedContents(File file, String token,
			FileOutputStream fos) throws InterruptedException, IOException {

		final URI announce = URI.create(ANNOUNCE_URI);

		final Torrent torrent = Torrent.create(file, PIECE_LENGTH, announce,
				TorrentUtils.class.getPackage().getName(), token);
		
		torrent.save(fos);

		return torrent;
	}
}
