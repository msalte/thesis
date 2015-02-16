package no.uis.msalte.thesis.bit_torrent.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import com.turn.ttorrent.common.Torrent;

public class TorrentUtil {
	private static final int PIECE_LENGTH = 256 * 1000;
	private static final String ANNOUNCE_URI = "http://localhost:6969/announce";

	public static String create(String fileName, String fileExt, String file) {
		return create(fileName, fileExt, file, false, null);
	}

	public static void create(String fileName, String fileExt, String file,
			String outputDirectory) {
		create(fileName, fileExt, file, true, outputDirectory);
	}

	public static boolean validate(String torrent) {
		FileOutputStream fos = null;
		File tmp = null;

		try {
			final byte[] b = Base64.getDecoder().decode(torrent);

			tmp = File.createTempFile("temp", null);

			fos = new FileOutputStream(tmp);
			fos.write(b);
			fos.flush();
			fos.close();

			Torrent t = Torrent.load(tmp);

			// The torrent is valid if it contains an announce list
			return t.getAnnounceList() != null;
		} catch (Exception e) {
			return false;
		} finally {
			if (tmp != null) {
				tmp.delete();
			}
		}
	}

	private static String create(String fileName, String fileExt, String file,
			boolean saveToDisk, String outputDirectory) {

		File localFile = null;
		File localTorrent = null;

		FileOutputStream fos = null;
		try {
			// Create a local temporary file in the file system for
			// the encoded file supplied as parameter
			byte[] fileBytes = Base64.getDecoder().decode(file);

			localFile = File.createTempFile(fileName,
					String.format(".%s", fileExt));

			fos = new FileOutputStream(localFile);
			fos.write(fileBytes);

			fos.flush();
			fos.close();

			// Determine whether to write the torrent to disk or to create a
			// temporary file
			if (saveToDisk) {
				localTorrent = new File(String.format("%s//%s.%s.%s",
						outputDirectory, fileName, fileExt, "torrent"));
			} else {
				localTorrent = File.createTempFile(fileName,
						String.format(".%s", "torrent"));
			}

			// Write the torrent contents into the permanent/temporary file
			fos = new FileOutputStream(localTorrent);
			writeBencodedContents(localFile, fos);

			fos.flush();
			fos.close();

			// Return an encoded version of the torrent file
			return Base64.getEncoder()
					.encodeToString(
							Files.readAllBytes(Paths.get(localTorrent
									.getAbsolutePath())));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if (localFile != null) {
				localFile.delete();
			}

			if (localTorrent != null && !saveToDisk) {
				localTorrent.delete();
			}
		}

		return null;
	}

	private static Torrent writeBencodedContents(File file, FileOutputStream fos)
			throws InterruptedException, IOException {

		final URI announce = URI.create(ANNOUNCE_URI);

		final List<List<URI>> announceTiers = new ArrayList<List<URI>>();
		final List<URI> announceTier1 = new ArrayList<URI>();

		announceTier1.add(announce);
		announceTiers.add(announceTier1);

		final Torrent torrent = Torrent.create(file, PIECE_LENGTH,
				announceTiers, TorrentUtil.class.getPackage().getName());

		torrent.save(fos);

		return torrent;
	}
}
