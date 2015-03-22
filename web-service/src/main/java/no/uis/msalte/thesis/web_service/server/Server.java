package no.uis.msalte.thesis.web_service.server;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.SparkBase.externalStaticFileLocation;
import static spark.SparkBase.port;
import static spark.SparkBase.secure;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import spark.Spark;
import no.uis.msalte.thesis.web_service.App;
import no.uis.msalte.thesis.web_service.routes.ApiGetRoute;
import no.uis.msalte.thesis.web_service.routes.DecryptPostRoute;
import no.uis.msalte.thesis.web_service.routes.DownloadPostRoute;
import no.uis.msalte.thesis.web_service.routes.NewPublicKeyPostRoute;
import no.uis.msalte.thesis.web_service.routes.NewReEncryptionKeyPostRoute;
import no.uis.msalte.thesis.web_service.routes.NewSecretKeyGetRoute;
import no.uis.msalte.thesis.web_service.routes.NewTorrentPostRoute;
import no.uis.msalte.thesis.web_service.routes.SharePostRoute;
import no.uis.msalte.thesis.web_service.routes.UploadPostRoute;
import no.uis.msalte.thesis.web_service.util.JsonTransformer;
import no.uis.msalte.thesis.web_service.util.WebServiceUtils;

public class Server {
	private static final String DIR_TLS_KEY_STORE = "keystore";
	private static final String FILE_TLS_KEY_STORE = "web_service.jks";
	private static final String FILE_TLS_KEY_STORE_PW = "keystore.pw";

	private static final String ACCEPT_TYPE = "application/json";
	private static final JsonTransformer JSON_TRANSFORMER = new JsonTransformer();

	public static final int HTTP_PORT = 9090;

	public void start() {
		// ---- SETUP ---- //
		port(HTTP_PORT);
		secure(String.format("%s\\%s\\%s", App.DIR, DIR_TLS_KEY_STORE,
				FILE_TLS_KEY_STORE), parseKeyStorePassword(), null, null);
		setStaticFileLocation();

		// ---- GET FUNCTIONS ---- //
		get(NewSecretKeyGetRoute.PATH, ACCEPT_TYPE, new NewSecretKeyGetRoute(),
				JSON_TRANSFORMER);

		get(ApiGetRoute.PATH, ACCEPT_TYPE, new ApiGetRoute(), JSON_TRANSFORMER);

		// ---- POST FUNCTIONS ---- //
		post(UploadPostRoute.PATH, ACCEPT_TYPE, new UploadPostRoute(),
				JSON_TRANSFORMER);

		post(NewPublicKeyPostRoute.PATH, ACCEPT_TYPE,
				new NewPublicKeyPostRoute(), JSON_TRANSFORMER);

		post(NewReEncryptionKeyPostRoute.PATH, ACCEPT_TYPE,
				new NewReEncryptionKeyPostRoute(), JSON_TRANSFORMER);

		post(DecryptPostRoute.PATH, ACCEPT_TYPE, new DecryptPostRoute(),
				JSON_TRANSFORMER);

		post(SharePostRoute.PATH, ACCEPT_TYPE, new SharePostRoute(),
				JSON_TRANSFORMER);

		post(DownloadPostRoute.PATH, ACCEPT_TYPE, new DownloadPostRoute(),
				JSON_TRANSFORMER);

		post(NewTorrentPostRoute.PATH, ACCEPT_TYPE, new NewTorrentPostRoute(),
				JSON_TRANSFORMER);
	}

	public void stop() {
		Spark.stop();
	}

	private static String parseKeyStorePassword() {
		String path = String.format("%s\\%s\\%s", App.DIR, DIR_TLS_KEY_STORE,
				FILE_TLS_KEY_STORE_PW);

		try {
			return WebServiceUtils.parseInputStream(new FileInputStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private static void setStaticFileLocation() {
		try {
			final String location = WebServiceUtils
					.getFileResource("script.js").getParent();
			externalStaticFileLocation(location);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
