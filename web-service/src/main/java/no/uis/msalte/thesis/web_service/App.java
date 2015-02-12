package no.uis.msalte.thesis.web_service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import no.uis.msalte.thesis.web_service.client.Client;
import no.uis.msalte.thesis.web_service.model.HttpMethod;
import no.uis.msalte.thesis.web_service.server.Server;

public class App {

	public static void main(String[] args) {
		new Server().start();

		String path = "C:\\Users\\Morten\\Desktop\\ttorrent\\torrents\\document.pdf.torrent";

		String function = "upload";

		String[] params = new String[] { "bytes" };
		String[] argz = new String[] { fileAsByteString(path) };

		String result = Client.call(HttpMethod.POST, function, params, argz);
		
		System.out.println(result);
	}

	private static String fileAsByteString(String path) {
		try {
			return new String(Base64.getEncoder().encode(
					Files.readAllBytes(Paths.get(path))));
		} catch (IOException e) {
			return "";
		}
	}
}
