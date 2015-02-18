package no.uis.msalte.thesis.web_service.server;

import java.net.URISyntaxException;

import no.uis.msalte.thesis.web_service.routes.ApiGetRoute;
import no.uis.msalte.thesis.web_service.routes.DownloadPostRoute;
import no.uis.msalte.thesis.web_service.routes.NewPublicKeyPostRoute;
import no.uis.msalte.thesis.web_service.routes.NewSecretKeyGetRoute;
import no.uis.msalte.thesis.web_service.routes.NewTorrentPostRoute;
import no.uis.msalte.thesis.web_service.routes.SharePostRoute;
import no.uis.msalte.thesis.web_service.routes.UploadPostRoute;
import no.uis.msalte.thesis.web_service.util.JsonRenderer;
import no.uis.msalte.thesis.web_service.util.WebServiceUtil;
import spark.Spark;

public class Server {

	private static final String ACCEPT_TYPE = "application/json";
	private static final JsonRenderer JSON_RENDERER = new JsonRenderer();

	public static final int HTTP_PORT = 9090;

	public void start() {
		// ---- SETUP ---- //
		Spark.port(HTTP_PORT);
		setStaticFileLocation();

		// ---- GET FUNCTIONS ---- //
		Spark.get(NewSecretKeyGetRoute.PATH, ACCEPT_TYPE,
				new NewSecretKeyGetRoute(), JSON_RENDERER);

		Spark.get(ApiGetRoute.PATH, ACCEPT_TYPE, new ApiGetRoute(),
				JSON_RENDERER);

		// ---- POST FUNCTIONS ---- //
		Spark.post(UploadPostRoute.PATH, ACCEPT_TYPE, new UploadPostRoute(),
				JSON_RENDERER);

		Spark.post(NewPublicKeyPostRoute.PATH, ACCEPT_TYPE,
				new NewPublicKeyPostRoute(), JSON_RENDERER);

		Spark.post(SharePostRoute.PATH, ACCEPT_TYPE, new SharePostRoute(),
				JSON_RENDERER);

		Spark.post(DownloadPostRoute.PATH, ACCEPT_TYPE,
				new DownloadPostRoute(), JSON_RENDERER);

		Spark.post(NewTorrentPostRoute.PATH, ACCEPT_TYPE,
				new NewTorrentPostRoute(), JSON_RENDERER);
	}

	public void stop() {
		Spark.stop();
	}

	private static void setStaticFileLocation() {
		try {
			String location = WebServiceUtil.getFileResource("script.js")
					.getParent();
			Spark.externalStaticFileLocation(location);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
