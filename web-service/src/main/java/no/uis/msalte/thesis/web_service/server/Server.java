package no.uis.msalte.thesis.web_service.server;

import java.net.HttpURLConnection;

import no.uis.msalte.thesis.web_service.model.TestObject;
import no.uis.msalte.thesis.web_service.util.JsonRenderer;
import spark.Spark;

public class Server {
	private static final String ACCEPT_TYPE = "application/json";
	private static final JsonRenderer JSON_RENDERER = new JsonRenderer();

	public static void start() {
		Spark.get(Methods.TEST, ACCEPT_TYPE, (req, res) -> {
			res.status(HttpURLConnection.HTTP_OK);
			return new TestObject("Title", "Message");
		}, JSON_RENDERER);
	}

	public interface Methods {
		public static final String TEST = "/test";
	}
}
