package no.uis.msalte.thesis.web_service.server;

import no.uis.msalte.thesis.web_service.model.FunctionCalls;
import no.uis.msalte.thesis.web_service.model.FunctionCallsImpl;
import no.uis.msalte.thesis.web_service.model.HttpMethod;
import no.uis.msalte.thesis.web_service.util.JsonRenderer;
import spark.Spark;

public class Server {

	private static final FunctionCallsImpl FUNCTION_CALLS = new FunctionCallsImpl();
	private static final String ACCEPT_TYPE = "application/json";
	private static final JsonRenderer JSON_RENDERER = new JsonRenderer();

	public static final int HTTP_PORT = 9090;

	public void start() {
		// ---- SETUP ---- //
		Spark.port(HTTP_PORT);

		// ---- GET FUNCTIONS ---- //
		Spark.get(getPath(FunctionCalls.FUNC_NEW_SECRET_KEY), ACCEPT_TYPE, (
				req, res) -> {
			return FUNCTION_CALLS.newSecretKey(req, res);
		}, JSON_RENDERER);

		FUNCTION_CALLS.setRedirectTo(HttpMethod.GET,
				FunctionCalls.FUNC_NEW_SECRET_KEY);

		Spark.get(getPath(FunctionCalls.FUNC_UI), ACCEPT_TYPE, (req, res) -> {
			return FUNCTION_CALLS.ui(req, res);
		}, JSON_RENDERER);

		FUNCTION_CALLS.setRedirectTo(HttpMethod.GET, FunctionCalls.FUNC_UI);

		// ---- POST FUNCTIONS ---- //
		Spark.post(getPath(FunctionCalls.FUNC_UPLOAD), ACCEPT_TYPE,
				(req, res) -> {
					return FUNCTION_CALLS.upload(req, res);
				}, JSON_RENDERER);

		Spark.post(getPath(FunctionCalls.FUNC_NEW_PUBLIC_KEY), ACCEPT_TYPE, (
				req, res) -> {
			return FUNCTION_CALLS.newPublicKey(req, res);
		}, JSON_RENDERER);

		Spark.post(getPath(FunctionCalls.FUNC_SHARE), ACCEPT_TYPE,
				(req, res) -> {
					return FUNCTION_CALLS.share(req, res);
				}, JSON_RENDERER);

		Spark.post(getPath(FunctionCalls.FUNC_DOWNLOAD), ACCEPT_TYPE,
				(req, res) -> {
					return FUNCTION_CALLS.download(req, res);
				}, JSON_RENDERER);

	}

	private static String getPath(String function) {
		return String.format("/%s", function);
	}
}
