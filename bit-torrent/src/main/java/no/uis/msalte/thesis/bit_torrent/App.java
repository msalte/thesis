package no.uis.msalte.thesis.bit_torrent;

import java.io.File;
import java.io.IOException;

import no.uis.msalte.thesis.bit_torrent.client.BitTorrentClient;
import no.uis.msalte.thesis.bit_torrent.tracker.BitTorrentTracker;

public class App {
	public static void main(String[] args) throws IOException {
		BitTorrentTracker tracker = new BitTorrentTracker(6969).start();
		tracker.start();

		File torrent = new File("C:\\Users\\Morten\\Desktop\\yeah.torrent");
		tracker.announce(torrent);

		BitTorrentClient c1 = new BitTorrentClient(
				"C:\\Users\\Morten\\Desktop\\yeah.torrent",
				"C:\\Users\\Morten\\Desktop\\ttorrent\\owner_dir\\");

		c1.getClient().share();

		BitTorrentClient c2 = new BitTorrentClient(
				"C:\\Users\\Morten\\Desktop\\yeah.torrent",
				"C:\\Users\\Morten\\Desktop\\ttorrent\\recipient_dir\\");

		c2.getClient().download();
	}
}
