package no.uis.msalte.thesis.secure_cloud;

import no.uis.msalte.thesis.secure_cloud.storage.Persist;

public class App {
	public static void main(String[] args) {
		Persist p = Persist.getInstance();
		
//		p.reset();
//		
//		p.write(Persist.MAP_TORRENTS, "torrentId1", "torrentBytes1");
//		p.write(Persist.MAP_TORRENTS, "torrentId2", "torrentBytes2");
//		p.write(Persist.MAP_TORRENTS, "torrentId3", "torrentBytes3");
//		
//		p.write(Persist.MAP_PUBLIC_KEYS, "torrentId1", "pk1");
//		p.write(Persist.MAP_RE_ENCRYPTION_KEYS, "pk1", "rek1");
		
		System.out.println(p.formatMap(Persist.MAP_TORRENTS));
		System.out.println(p.formatMap(Persist.MAP_PUBLIC_KEYS));
		System.out.println(p.formatMap(Persist.MAP_RE_ENCRYPTION_KEYS));
	}
}
