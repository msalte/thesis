package no.uis.msalte.thesis.secure_cloud;

import no.uis.msalte.thesis.secure_cloud.storage.Persist;


public class App {
	public static void main(String[] args) {
		 Persist p = Persist.getInstance();
		 
		 System.out.println(p.formatMap(Persist.Maps.TORRENTS));
		 System.out.println(p.formatMap(Persist.Maps.KEY_TUPLES));
	}
}
