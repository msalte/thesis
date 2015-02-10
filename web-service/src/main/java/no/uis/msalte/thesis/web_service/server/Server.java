package no.uis.msalte.thesis.web_service.server;

import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import no.uis.msalte.thesis.secure_cloud.model.SecureCloudShareImpl;
import no.uis.msalte.thesis.web_service.model.CallResponse;
import no.uis.msalte.thesis.web_service.model.InterfaceEntry;
import no.uis.msalte.thesis.web_service.model.MethodCalls;
import no.uis.msalte.thesis.web_service.model.MethodParams;
import no.uis.msalte.thesis.web_service.util.JsonRenderer;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

public class Server implements MethodCalls {

	private static final SecureCloudShareImpl SECURE_CLOUD_SHARE = new SecureCloudShareImpl();
	private static final String ACCEPT_TYPE = "application/json";
	private static final JsonRenderer JSON_RENDERER = new JsonRenderer();

	public static final int HTTP_PORT = 9090;

	public void start() {
		Spark.port(HTTP_PORT);

		Spark.get(UI, ACCEPT_TYPE, new Route() {
			@Override
			public Object handle(Request request, Response response)
					throws Exception {
				return new CallResponse(response, ui(),
						"This is the user interface");
			}
		}, JSON_RENDERER);

		addRedirect(UI);

		Spark.get(NEW_SECRET_KEY, ACCEPT_TYPE, new Route() {
			@Override
			public Object handle(Request request, Response response)
					throws Exception {
				final BigInteger secretKey = new BigInteger(SECURE_CLOUD_SHARE
						.newSecretKey());

				return new CallResponse(response, String
						.format("%s", secretKey), "New secret key generated");
			}
		}, JSON_RENDERER);

		addRedirect(NEW_SECRET_KEY);

		Spark.get(NEW_PUBLIC_KEY, ACCEPT_TYPE, new Route() {
			@Override
			public Object handle(Request request, Response response)
					throws Exception {
				final String sk = request.params(MethodParams.SECRET_KEY);

				if (sk != null) {
					BigInteger secretKey = new BigInteger(sk);

					BigInteger publicKey = new BigInteger(SECURE_CLOUD_SHARE
							.newPublicKey(secretKey.toByteArray()));

					return new CallResponse(response, String.format("%s",
							publicKey), "New public key generated");
				}

				return httpBadRequest(response);
			}
		}, JSON_RENDERER);

		Spark.get(NEW_TORRENT, ACCEPT_TYPE, new Route() {
			@Override
			public Object handle(Request request, Response response)
					throws Exception {
				final String bytes = request.params(MethodParams.BYTES);

				if (bytes != null) {
					return new CallResponse(response, String
							.valueOf((int) (Math.random() * 100)),
							"Added torrent");
				}

				return httpBadRequest(response);
			}
		}, JSON_RENDERER);

		Spark.get(UPLOAD, ACCEPT_TYPE, new Route() {
			@Override
			public Object handle(Request request, Response response)
					throws Exception {
				final String bytes = request.params(MethodParams.BYTES);

				if (bytes != null) {
					int result = SECURE_CLOUD_SHARE.upload(bytes.getBytes());

					return new CallResponse(response, String.valueOf(result),
							"Upload stored");
				}

				return httpBadRequest(response);
			}
		}, JSON_RENDERER);

		Spark.get(SHARE, ACCEPT_TYPE, new Route() {
			@Override
			public Object handle(Request request, Response response)
					throws Exception {

				final String id = request.params(MethodParams.ID);
				final String pk = request.params(MethodParams.PUBLIC_KEY);
				final String rek = request
						.params(MethodParams.RE_ENCRYPTION_KEY);

				boolean shared = SECURE_CLOUD_SHARE.share(Integer.parseInt(id),
						pk.getBytes(), rek.getBytes());

				if (shared) {
					return new CallResponse(
							response,
							shared,
							String.format(
									"Shared torrent: [%s] with public key holder: [%s] (re-encrypted by: [%s])",
									id, pk, rek));
				}

				return httpBadRequest(response);

			}
		}, JSON_RENDERER);

		Spark.get(DOWNLOAD, ACCEPT_TYPE, new Route() {

			@Override
			public Object handle(Request request, Response response)
					throws Exception {
				final String id = request.params(MethodParams.ID);
				final String publicKey = request
						.params(MethodParams.PUBLIC_KEY);

				final byte[] bytes = SECURE_CLOUD_SHARE.download(
						Integer.parseInt(id), publicKey.getBytes());

				if (bytes != null) {
					return new CallResponse(response, bytes.toString(),
							"Download granted");
				}

				return httpBadRequest(response);
			}
		}, JSON_RENDERER);

	}

	private CallResponse httpBadRequest(Response response) {
		return new CallResponse(response, null, "Bad request",
				HttpURLConnection.HTTP_BAD_REQUEST);
	}

	/**
	 * This will ensure that a call to some.url/method/ will be redirected to
	 * some.url/method
	 * 
	 * @param method
	 *            The corresponding method name
	 */
	private void addRedirect(String method) {
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

	@Override
	public List<InterfaceEntry> ui() {
		final ArrayList<InterfaceEntry> ui = new ArrayList<InterfaceEntry>();

		ui.add(new InterfaceEntry(UI, null, "this"));
		ui.add(new InterfaceEntry(NEW_TORRENT, "bytes", "int"));
		ui.add(new InterfaceEntry(NEW_SECRET_KEY, null, "bytes"));
		ui.add(new InterfaceEntry(NEW_PUBLIC_KEY, "bytes", "bytes"));
		ui.add(new InterfaceEntry(SHARE, "int/bytes/bytes", "boolean"));
		ui.add(new InterfaceEntry(UPLOAD, "bytes", "int"));
		ui.add(new InterfaceEntry(DOWNLOAD, "int/bytes", "bytes"));

		return ui;
	}
}
