package no.uis.msalte.thesis.web_service;

import no.uis.msalte.thesis.web_service.server.Server;

public class App {
	public static final String DIR = "C:\\Users\\Morten\\Desktop\\app_dir";
	
	public static void main(String[] args) {
		new Server().start();
	}
}
