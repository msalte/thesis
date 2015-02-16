package no.uis.msalte.thesis.web_service.tests;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;

import no.uis.msalte.thesis.bit_torrent.util.TorrentUtil;
import no.uis.msalte.thesis.secure_cloud.util.FilesUtil;
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
	public void testGivenNewTorrentSuccessThenReturnedContentShouldBeValidTorrent()
			throws Exception {

		final byte[] localFile = Files.readAllBytes(Paths
				.get(getPath("test.txt")));

		final String localDocumentString = Base64.getEncoder().encodeToString(
				localFile);

		// new torrent web service call
		final String result = Client.call(HttpMethod.POST,
				WebService.FUNC_NEW_TORRENT,
				new String[] { WebService.PARAM_FILE },
				new String[] { localDocumentString });

		// response parse
		final WebServiceResponse res = JsonRenderer.RENDERER.fromJson(result,
				WebServiceResponse.class);

		// validate
		if (res.getStatus() == HttpURLConnection.HTTP_OK) {
			String returnedTorrent = res.getContent().toString();

			assertTrue(TorrentUtil.validate(returnedTorrent));
		} else {
			assertTrue(false);
		}
	}

	@Test
	public void testGivenUploadFileSuccessThenShouldReturnNewFileName()
			throws Exception {

		final byte[] file = Files.readAllBytes(Paths
				.get(getPath("file.torrent")));

		// upload file
		final String result = Client.call(HttpMethod.POST,
				WebService.FUNC_UPLOAD, new String[] { WebService.PARAM_FILE },
				new String[] { FilesUtil.encode(file) });

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

		final String publicKey = FilesUtil.encode("public_key".getBytes());
		final String reEncryptionKey = FilesUtil.encode("re_encryption_key"
				.getBytes());

		final byte[] file = Files.readAllBytes(Paths
				.get(getPath("file.torrent")));

		// upload file
		final String uploadResult = Client.call(HttpMethod.POST,
				WebService.FUNC_UPLOAD, new String[] { WebService.PARAM_FILE },
				new String[] { FilesUtil.encode(file) });

		// retrieve file name
		final String fileName = JsonRenderer.RENDERER
				.fromJson(uploadResult, WebServiceResponse.class).getContent()
				.toString();

		// share
		Client.call(HttpMethod.POST, WebService.FUNC_SHARE, new String[] {
				WebService.PARAM_FILE_NAME, WebService.PARAM_PUBLIC_KEY,
				WebService.PARAM_RE_ENCRYPTION_KEY }, new String[] { fileName,
				publicKey, reEncryptionKey });

		// download
		final String downloadResult = Client.call(HttpMethod.POST,
				WebService.FUNC_DOWNLOAD,
				new String[] { WebService.PARAM_FILE_NAME,
						WebService.PARAM_PUBLIC_KEY }, new String[] { fileName,
						publicKey });

		final String downloadedFile = JsonRenderer.RENDERER
				.fromJson(downloadResult, WebServiceResponse.class)
				.getContent().toString();

		assertTrue(Arrays.equals(file, FilesUtil.decode(downloadedFile)));
	}

	private String getPath(String filename) throws URISyntaxException {
		return new File(getClass().getClassLoader().getResource(filename)
				.toURI()).getAbsolutePath();
	}
}
