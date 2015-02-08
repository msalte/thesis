package no.uis.msalte.thesis.web_service;

import no.uis.msalte.thesis.web_service.client.Client;
import no.uis.msalte.thesis.web_service.server.Server;

public class App {

	public static void main(String[] args) {
		Server.start();
		
		String torrent = Client.call(Server.Methods.NEW_TORRENT, "bytes".getBytes().toString());
		
		System.out.println(torrent);
	}
}
