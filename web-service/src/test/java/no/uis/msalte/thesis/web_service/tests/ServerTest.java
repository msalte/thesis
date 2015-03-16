package no.uis.msalte.thesis.web_service.tests;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.util.Base64;

import no.uis.msalte.thesis.bit_torrent.util.TorrentUtils;
import no.uis.msalte.thesis.web_service.client.Client;
import no.uis.msalte.thesis.web_service.model.WebServiceResponse;
import no.uis.msalte.thesis.web_service.model.WebServiceRoute;
import no.uis.msalte.thesis.web_service.server.Server;
import no.uis.msalte.thesis.web_service.util.JsonTransformer;

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

		final File file = getFileResource("document.pdf");

		// new torrent web service call
		final String result = Client.post(WebServiceRoute.FUNC_NEW_TORRENT,
				new String[] {}, new String[] {}, file);

		// response parse
		final WebServiceResponse res = JsonTransformer.GSON.fromJson(result,
				WebServiceResponse.class);

		// validate
		if (res.getStatus() == HttpURLConnection.HTTP_OK) {
			String torrentBytes = res.getContent().toString();

			assertTrue(TorrentUtils.isValidTorrent(torrentBytes));
		} else {
			assertTrue(false);
		}
	}

	@Test
	public void testGivenUploadFileSuccessThenShouldReturnNewFileName()
			throws Exception {

		final String publicKey = Base64.getEncoder().encodeToString(
				"public_key".getBytes());
		final File file = getFileResource("file.torrent");

		// upload file
		final String result = Client.post(WebServiceRoute.FUNC_UPLOAD,
				new String[] { WebServiceRoute.PARAM_PUBLIC_KEY },
				new String[] { publicKey }, file);

		final String fileName = JsonTransformer.GSON
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

		final String sourcePublicKey = Base64.getEncoder().encodeToString(
				"source_public_key".getBytes());
		final String targetPublicKey = Base64.getEncoder().encodeToString(
				"target_public_key".getBytes());
		final String sourceToTargetReEncryptionKey = Base64
				.getEncoder()
				.encodeToString("source_to_target_re_encryption_key".getBytes());

		final File file = getFileResource("file.torrent");

		// upload file
		final String uploadResult = Client.post(WebServiceRoute.FUNC_UPLOAD,
				new String[] { WebServiceRoute.PARAM_PUBLIC_KEY },
				new String[] { sourcePublicKey }, file);

		// retrieve file name
		final String fileName = JsonTransformer.GSON
				.fromJson(uploadResult, WebServiceResponse.class).getContent()
				.toString();

		// share
		Client.post(WebServiceRoute.FUNC_SHARE, new String[] {
				WebServiceRoute.PARAM_FILE_NAME,
				WebServiceRoute.PARAM_PUBLIC_KEY,
				WebServiceRoute.PARAM_RE_ENCRYPTION_KEY }, new String[] {
				fileName, targetPublicKey, sourceToTargetReEncryptionKey },
				null);

		// download
		final String downloadResult = Client.post(
				WebServiceRoute.FUNC_DOWNLOAD, new String[] {
						WebServiceRoute.PARAM_FILE_NAME,
						WebServiceRoute.PARAM_PUBLIC_KEY }, new String[] {
						fileName, targetPublicKey }, null);

		final WebServiceResponse downloaded = JsonTransformer.GSON.fromJson(
				downloadResult, WebServiceResponse.class);

		if (downloaded.getContent() != null) {
			assertTrue(downloaded.getMessage().equals("Download granted"));
		} else {
			assertTrue(false);
		}
	}

	private File getFileResource(String filename) throws URISyntaxException {
		return new File(getClass().getClassLoader().getResource(filename)
				.toURI());
	}
}
