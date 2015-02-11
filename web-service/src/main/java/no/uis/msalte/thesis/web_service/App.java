package no.uis.msalte.thesis.web_service;

import no.uis.msalte.thesis.web_service.client.Client;
import no.uis.msalte.thesis.web_service.server.Server;

public class App {

	public static void main(String[] args) {
		new Server().start();
		
		String method = "upload";
		String[] params = new String[] {"bytes", "fuck"};
		String[] argz = new String [] {"abc", "load"};
		
		Client.post(method, params, argz);
	}
}
