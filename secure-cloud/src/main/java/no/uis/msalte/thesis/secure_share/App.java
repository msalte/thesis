package no.uis.msalte.thesis.secure_share;

import no.uis.msalte.thesis.secure_share.storage.Persist;

public class App {
	
	public static void main(String[] args) {
		Persist p = Persist.getInstance();

		System.out.println(p.formatMap(Persist.Maps.TORRENTS));
		System.out.println(p.formatMap(Persist.Maps.SHARES));
	}
}
