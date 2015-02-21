package no.uis.msalte.thesis.bit_torrent;

import no.uis.msalte.thesis.bit_torrent.tracker.BitTorrentTracker;

public class App {
	public static void main(String[] args) {
		new BitTorrentTracker(6969).start();
	}
}
