package no.uis.msalte.thesis.web_service.server;

import no.uis.msalte.thesis.web_service.model.HttpMethod;
import no.uis.msalte.thesis.web_service.model.WebService;
import no.uis.msalte.thesis.web_service.model.WebServiceImpl;
import no.uis.msalte.thesis.web_service.util.JsonRenderer;
import spark.Spark;

public class Server {

	private static final WebServiceImpl WEB_SERVICE = new WebServiceImpl();
	private static final String ACCEPT_TYPE = "application/json";
	private static final JsonRenderer JSON_RENDERER = new JsonRenderer();

	public static final int HTTP_PORT = 9090;

	public void start() {
		// ---- SETUP ---- //
		Spark.port(HTTP_PORT);

		// ---- GET FUNCTIONS ---- //
		Spark.get(getPath(WebService.FUNC_NEW_SECRET_KEY), ACCEPT_TYPE, (req,
				res) -> {
			return WEB_SERVICE.newSecretKey(req, res);
		}, JSON_RENDERER);

		WEB_SERVICE.setRedirectTo(HttpMethod.GET,
				WebService.FUNC_NEW_SECRET_KEY);

		Spark.get(getPath(WebService.FUNC_API), ACCEPT_TYPE, (req, res) -> {
			return WEB_SERVICE.ui(req, res);
		}, JSON_RENDERER);

		WEB_SERVICE.setRedirectTo(HttpMethod.GET, WebService.FUNC_API);

		// ---- POST FUNCTIONS ---- //
		Spark.post(getPath(WebService.FUNC_UPLOAD), ACCEPT_TYPE,
				(req, res) -> {
					return WEB_SERVICE.upload(req, res);
				}, JSON_RENDERER);

		Spark.post(getPath(WebService.FUNC_NEW_PUBLIC_KEY), ACCEPT_TYPE, (req,
				res) -> {
			return WEB_SERVICE.newPublicKey(req, res);
		}, JSON_RENDERER);

		Spark.post(getPath(WebService.FUNC_SHARE), ACCEPT_TYPE, (req, res) -> {
			return WEB_SERVICE.share(req, res);
		}, JSON_RENDERER);

		Spark.post(getPath(WebService.FUNC_DOWNLOAD), ACCEPT_TYPE,
				(req, res) -> {
					return WEB_SERVICE.download(req, res);
				}, JSON_RENDERER);

		Spark.post(getPath(WebService.FUNC_NEW_TORRENT), ACCEPT_TYPE,
				(req, res) -> {
					return WEB_SERVICE.newTorrent(req, res);
				}, JSON_RENDERER);
	}

	public void stop() {
		Spark.stop();
	}

	private static String getPath(String function) {
		return String.format("/%s", function);
	}
}
