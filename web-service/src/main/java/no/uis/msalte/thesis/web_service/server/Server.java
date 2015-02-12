package no.uis.msalte.thesis.web_service.server;

import java.io.FileOutputStream;
import java.util.Base64;

import no.uis.msalte.thesis.web_service.model.FunctionCalls;
import no.uis.msalte.thesis.web_service.model.FunctionCallsImpl;
import no.uis.msalte.thesis.web_service.util.JsonRenderer;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

public class Server {

	private static final FunctionCallsImpl METHOD_CALLS = new FunctionCallsImpl();
	private static final String ACCEPT_TYPE = "application/json";
	private static final JsonRenderer JSON_RENDERER = new JsonRenderer();

	public static final int HTTP_PORT = 9090;

	public void start() {
		Spark.port(HTTP_PORT);

		// TODO use http post for argumented calls
		// TODO evaluate using halt() for errors instead of returning bad
		// request response
		Spark.get(FunctionCalls.PATH_UI, ACCEPT_TYPE, (req, res) -> {
			return METHOD_CALLS.ui(req, res);
		}, JSON_RENDERER);

		Spark.get(FunctionCalls.PATH_NEW_SECRET_KEY, ACCEPT_TYPE, (req, res) -> {
			return METHOD_CALLS.newSecretKey(req, res);
		}, JSON_RENDERER);

		Spark.get(FunctionCalls.PATH_NEW_PUBLIC_KEY, ACCEPT_TYPE, (req, res) -> {
			return METHOD_CALLS.newPublicKey(req, res);
		}, JSON_RENDERER);

		Spark.get(FunctionCalls.PATH_NEW_TORRENT, ACCEPT_TYPE, (req, res) -> {
			return METHOD_CALLS.newTorrent(req, res);
		}, JSON_RENDERER);

		Spark.get(FunctionCalls.PATH_UPLOAD, ACCEPT_TYPE, (req, res) -> {
			return METHOD_CALLS.upload(req, res);
		}, JSON_RENDERER);

		Spark.get(FunctionCalls.PATH_SHARE, ACCEPT_TYPE, (req, res) -> {
			return METHOD_CALLS.share(req, res);
		}, JSON_RENDERER);

		Spark.get(FunctionCalls.PATH_DOWNLOAD, ACCEPT_TYPE, (req, res) -> {
			return METHOD_CALLS.download(req, res);
		}, JSON_RENDERER);

		Spark.post("/upload", ACCEPT_TYPE, new Route() {

			@Override
			public Object handle(Request request, Response response)
					throws Exception {
				String bytes = request.queryParams("bytes");

				byte[] data = Base64.getDecoder().decode(bytes);

				FileOutputStream fos = new FileOutputStream(
						"C:\\Users\\Morten\\Desktop\\new.pdf.torrent");
				fos.write(data);
				fos.close();

				return bytes;
			}
		}, JSON_RENDERER);

	}
}
