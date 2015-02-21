package no.uis.msalte.thesis.bit_torrent.client;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.turn.ttorrent.client.Client;
import com.turn.ttorrent.client.SharedTorrent;

public class BitTorrentClient {

	private Client client;

	public static final double MAX_DOWNLOAD_RATE = 50.0 * 1000;
	public static final double MAX_UPLOAD_RATE = 50.0 * 1000;

	public BitTorrentClient(String torrentPath, String outputDir) {
		init(torrentPath, outputDir);
	}

	private void init(String torrentPath, String outputDir) {
		try {
			client = new Client(InetAddress.getLocalHost(),
					SharedTorrent.fromFile(new File(torrentPath), new File(
							outputDir)));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (client != null) {
			client.setMaxDownloadRate(MAX_DOWNLOAD_RATE);
			client.setMaxUploadRate(MAX_UPLOAD_RATE);
		}
	}

	public Client getClient() {
		return client;
	}
}
