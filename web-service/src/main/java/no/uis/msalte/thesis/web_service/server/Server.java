package no.uis.msalte.thesis.web_service.server;

import java.net.URISyntaxException;

import no.uis.msalte.thesis.web_service.routes.ApiGetRoute;
import no.uis.msalte.thesis.web_service.routes.DownloadPostRoute;
import no.uis.msalte.thesis.web_service.routes.NewPublicKeyPostRoute;
import no.uis.msalte.thesis.web_service.routes.NewReEncryptionKeyPostRoute;
import no.uis.msalte.thesis.web_service.routes.NewSecretKeyGetRoute;
import no.uis.msalte.thesis.web_service.routes.NewTorrentPostRoute;
import no.uis.msalte.thesis.web_service.routes.SharePostRoute;
import no.uis.msalte.thesis.web_service.routes.UploadPostRoute;
import no.uis.msalte.thesis.web_service.util.JsonTransformer;
import no.uis.msalte.thesis.web_service.util.WebServiceUtils;
import spark.Spark;

public class Server {

	private static final String ACCEPT_TYPE = "application/json";
	private static final JsonTransformer JSON_TRANSFORMER = new JsonTransformer();

	public static final int HTTP_PORT = 9090;

	public void start() {
		// ---- SETUP ---- //
		Spark.port(HTTP_PORT);
		setStaticFileLocation();

		// ---- GET FUNCTIONS ---- //
		Spark.get(NewSecretKeyGetRoute.PATH, ACCEPT_TYPE,
				new NewSecretKeyGetRoute(), JSON_TRANSFORMER);

		Spark.get(ApiGetRoute.PATH, ACCEPT_TYPE, new ApiGetRoute(),
				JSON_TRANSFORMER);

		// ---- POST FUNCTIONS ---- //
		Spark.post(UploadPostRoute.PATH, ACCEPT_TYPE, new UploadPostRoute(),
				JSON_TRANSFORMER);

		Spark.post(NewPublicKeyPostRoute.PATH, ACCEPT_TYPE,
				new NewPublicKeyPostRoute(), JSON_TRANSFORMER);

		Spark.post(NewReEncryptionKeyPostRoute.PATH, ACCEPT_TYPE,
				new NewReEncryptionKeyPostRoute(), JSON_TRANSFORMER);

		Spark.post(SharePostRoute.PATH, ACCEPT_TYPE, new SharePostRoute(),
				JSON_TRANSFORMER);

		Spark.post(DownloadPostRoute.PATH, ACCEPT_TYPE,
				new DownloadPostRoute(), JSON_TRANSFORMER);

		Spark.post(NewTorrentPostRoute.PATH, ACCEPT_TYPE,
				new NewTorrentPostRoute(), JSON_TRANSFORMER);
	}

	public void stop() {
		Spark.stop();
	}

	private static void setStaticFileLocation() {
		try {
			final String location = WebServiceUtils.getFileResource("script.js")
					.getParent();
			Spark.externalStaticFileLocation(location);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
