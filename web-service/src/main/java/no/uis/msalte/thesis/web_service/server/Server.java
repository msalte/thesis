package no.uis.msalte.thesis.web_service.server;

import java.math.BigInteger;
import java.net.HttpURLConnection;

import no.uis.msalte.thesis.secure_cloud.model.SecureCloudShareImpl;
import no.uis.msalte.thesis.web_service.model.CallResponse;
import no.uis.msalte.thesis.web_service.util.JsonRenderer;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

public class Server {
	private static final SecureCloudShareImpl SECURE_CLOUD_SHARE = new SecureCloudShareImpl();
	private static final String ACCEPT_TYPE = "application/json";
	private static final JsonRenderer JSON_RENDERER = new JsonRenderer();

	public static final int HTTP_PORT = 9090;

	public static void start() {
		Spark.port(HTTP_PORT);

		Spark.get(Methods.NEW_SECRET_KEY, ACCEPT_TYPE, new Route() {
			@Override
			public Object handle(Request request, Response response)
					throws Exception {
				final BigInteger secretKey = new BigInteger(SECURE_CLOUD_SHARE
						.newSecretKey());

				return new CallResponse(response, String
						.format("%s", secretKey), "New secret key generated");
			}
		}, JSON_RENDERER);

		addRedirect(Methods.NEW_SECRET_KEY);

		Spark.get(Methods.NEW_PUBLIC_KEY, ACCEPT_TYPE, new Route() {
			@Override
			public Object handle(Request request, Response response)
					throws Exception {
				final String sk = request.params(":sk");

				if (sk != null) {
					BigInteger secretKey = new BigInteger(sk);

					BigInteger publicKey = new BigInteger(SECURE_CLOUD_SHARE
							.newPublicKey(secretKey.toByteArray()));

					return new CallResponse(response, String.format("%s",
							publicKey), "New public key generated");
				}

				return httpNotFound(response);
			}
		}, JSON_RENDERER);

		Spark.get(Methods.NEW_TORRENT, ACCEPT_TYPE, new Route() {
			@Override
			public Object handle(Request request, Response response)
					throws Exception {
				final String bytes = request.params(":bytes");

				if (bytes != null) {
					return new CallResponse(response, String
							.valueOf((int) (Math.random() * 100)),
							"Added torrent");
				}

				return httpNotFound(response);
			}
		}, JSON_RENDERER);

		Spark.get(Methods.SHARE, ACCEPT_TYPE, new Route() {
			@Override
			public Object handle(Request request, Response response)
					throws Exception {

				final String id = request.params(":id");
				final String pk = request.params(":pk");
				final String rek = request.params(":rek");

				return new CallResponse(
						response,
						null,
						String.format(
								"Shared torrent: [%s] with public key holder: [%s] (re-encrypted by: [%s])",
								id, pk, rek));
			}
		}, JSON_RENDERER);
	}

	public interface Methods {
		public static final String NEW_TORRENT = "/newTorrent/:bytes";
		public static final String NEW_SECRET_KEY = "/newSecretKey";
		public static final String NEW_PUBLIC_KEY = "/newPublicKey/:sk";
		public static final String SHARE = "/share/:id/:pk/:rek";
	}

	private static CallResponse httpNotFound(Response response) {
		return new CallResponse(response, "", "An error occurred",
				HttpURLConnection.HTTP_NOT_FOUND);
	}

	/**
	 * This will ensure that a call to some.url/method/ will be redirected to
	 * some.url/method
	 * 
	 * @param method
	 *            The corresponding method name
	 */
	private static void addRedirect(String method) {
		String path = String.format("%s/", method);

		Spark.get(path, new Route() {

			@Override
			public Object handle(Request request, Response response)
					throws Exception {
				response.redirect(method);
				return null;
			}
		});
	}
}
