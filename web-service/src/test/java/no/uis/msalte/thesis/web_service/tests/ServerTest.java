package no.uis.msalte.thesis.web_service.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import no.uis.msalte.thesis.web_service.client.Client;
import no.uis.msalte.thesis.web_service.model.HttpMethod;
import no.uis.msalte.thesis.web_service.model.WebService;
import no.uis.msalte.thesis.web_service.model.WebServiceResponse;
import no.uis.msalte.thesis.web_service.server.Server;
import no.uis.msalte.thesis.web_service.util.JsonRenderer;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ServerTest {
	private static Server server;

	@BeforeClass
	public static void startServer() {
		server = new Server();
		server.start();
	}

	@AfterClass
	public static void stopServer() {
		server.stop();
	}

	@Test
	public void testGivenUploadFileSuccessThenShouldReturnNewFileName()
			throws Exception {
		final String fileAsBytes = fileAsByteString(getPath("file.torrent"));

		// upload file
		final String result = Client.call(HttpMethod.POST,
				WebService.FUNC_UPLOAD,
				new String[] { WebService.PARAM_TORRENT },
				new String[] { fileAsBytes });

		final String fileName = JsonRenderer.RENDERER
				.fromJson(result, WebServiceResponse.class).getContent()
				.toString();

		try {
			assertTrue(fileName.endsWith(".torrent"));
		} catch (IllegalArgumentException e) {
			assertTrue(false);
		}
	}

	@Test
	public void testGivenShareFileSuccessThenRecipientShouldBeGrantedDownload()
			throws Exception {
		final String publicKey = "public_key";
		final String reEncryptionKey = "re_encryption_key";

		final String fileAsBytes = fileAsByteString(getPath("file.torrent"));

		// upload file
		final String uploadResult = Client.call(HttpMethod.POST,
				WebService.FUNC_UPLOAD,
				new String[] { WebService.PARAM_TORRENT },
				new String[] { fileAsBytes });

		// retrieve id
		final String id = JsonRenderer.RENDERER
				.fromJson(uploadResult, WebServiceResponse.class).getContent()
				.toString();

		// share
		Client.call(HttpMethod.POST, WebService.FUNC_SHARE, new String[] {
				WebService.PARAM_ID, WebService.PARAM_PUBLIC_KEY,
				WebService.PARAM_RE_ENCRYPTION_KEY }, new String[] { id,
				publicKey, reEncryptionKey });

		// download
		final String downloadResult = Client.call(HttpMethod.POST,
				WebService.FUNC_DOWNLOAD, new String[] { WebService.PARAM_ID,
						WebService.PARAM_PUBLIC_KEY }, new String[] { id,
						publicKey });

		final String downloadedFileBytes = JsonRenderer.RENDERER
				.fromJson(downloadResult, WebServiceResponse.class)
				.getContent().toString();

		assertEquals(fileAsBytes, downloadedFileBytes);
	}

	private String getPath(String filename) throws URISyntaxException {
		return new File(getClass().getClassLoader().getResource(filename)
				.toURI()).getAbsolutePath();
	}

	private String fileAsByteString(String path) {
		try {
			return new String(Base64.getEncoder().encode(
					Files.readAllBytes(Paths.get(path))));
		} catch (IOException e) {
			return "";
		}
	}
}
