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
import no.uis.msalte.thesis.web_service.model.CallResponse;
import no.uis.msalte.thesis.web_service.model.FunctionCalls;
import no.uis.msalte.thesis.web_service.model.HttpMethod;
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
	public void testGivenUploadFileSuccessThenShouldReturnNewId()
			throws Exception {
		final String fileAsBytes = fileAsByteString(getPath("file.torrent"));

		// upload file
		final String result = Client.call(HttpMethod.POST,
				FunctionCalls.FUNC_UPLOAD,
				new String[] { FunctionCalls.PARAM_TORRENT },
				new String[] { fileAsBytes });

		final String id = JsonRenderer.RENDERER
				.fromJson(result, CallResponse.class).getContent().toString();

		assertTrue(new Integer(id) instanceof Integer);
	}

	@Test
	public void testGivenShareFileSuccessThenRecipientShouldBeGrantedDownload()
			throws Exception {
		final String publicKey = "public_key";
		final String reEncryptionKey = "re_encryption_key";

		final String fileAsBytes = fileAsByteString(getPath("file.torrent"));

		// upload file
		final String uploadResult = Client.call(HttpMethod.POST,
				FunctionCalls.FUNC_UPLOAD,
				new String[] { FunctionCalls.PARAM_TORRENT },
				new String[] { fileAsBytes });

		// retrieve id
		final String id = JsonRenderer.RENDERER
				.fromJson(uploadResult, CallResponse.class).getContent()
				.toString();

		// share
		Client.call(HttpMethod.POST, FunctionCalls.FUNC_SHARE, new String[] {
				FunctionCalls.PARAM_ID, FunctionCalls.PARAM_PUBLIC_KEY,
				FunctionCalls.PARAM_RE_ENCRYPTION_KEY }, new String[] { id,
				publicKey, reEncryptionKey });

		// download
		final String downloadResult = Client.call(HttpMethod.POST,
				FunctionCalls.FUNC_DOWNLOAD,
				new String[] { FunctionCalls.PARAM_ID,
						FunctionCalls.PARAM_PUBLIC_KEY }, new String[] { id,
						publicKey });

		final String downloadedFileBytes = JsonRenderer.RENDERER
				.fromJson(downloadResult, CallResponse.class).getContent()
				.toString();

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
