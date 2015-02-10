package no.uis.msalte.thesis.web_service.model;

import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import no.uis.msalte.thesis.secure_cloud.model.SecureCloudShareImpl;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

public class MethodCallsImpl implements MethodCalls {

	private static final SecureCloudShareImpl SECURE_CLOUD_SHARE = new SecureCloudShareImpl();

	@Override
	public CallResponse ui(Request req, Response res) {
		final CallResponse ui = getDefaultCallResponse(res);

		final String message = "The user interface";
		final ArrayList<InterfaceEntry> content = new ArrayList<InterfaceEntry>();

		content.add(new InterfaceEntry(PATH_UI, null, "this"));
		content.add(new InterfaceEntry(PATH_NEW_TORRENT, "bytes", "int"));
		content.add(new InterfaceEntry(PATH_NEW_SECRET_KEY, null, "bytes"));
		content.add(new InterfaceEntry(PATH_NEW_PUBLIC_KEY, "bytes", "bytes"));
		content.add(new InterfaceEntry(PATH_SHARE, "int/bytes/bytes", "boolean"));
		content.add(new InterfaceEntry(PATH_UPLOAD, "bytes", "int"));
		content.add(new InterfaceEntry(PATH_DOWNLOAD, "int/bytes", "bytes"));

		ui.setMessage(message);
		ui.setContent(content);

		setRedirectTo(PATH_UI);

		return ui;
	}

	@Override
	public CallResponse newTorrent(Request req, Response res) {
		final CallResponse newTorrent = getDefaultCallResponse(res);

		final String bytes = req.params(MethodParams.BYTES);

		if (bytes != null) {
			// TODO validate bytes
			// TODO put in torrent directory

			final String message = "Torrent added";
			final String content = String
					.valueOf((int) (Math.random() * 100) + 1); // random 1-100

			newTorrent.setMessage(message);
			newTorrent.setContent(content);

			return newTorrent;
		}

		return setHttpBadRequest(res);
	}

	@Override
	public CallResponse newSecretKey(Request req, Response res) {
		final CallResponse newSecretKey = getDefaultCallResponse(res);

		final byte[] secretKeyBytes = SECURE_CLOUD_SHARE.newSecretKey();

		final String message = "Secret key generated";
		final String content = new BigInteger(secretKeyBytes).toString();

		newSecretKey.setMessage(message);
		newSecretKey.setContent(content);

		setRedirectTo(PATH_NEW_SECRET_KEY);

		return newSecretKey;
	}

	@Override
	public CallResponse newPublicKey(Request req, Response res) {
		final CallResponse newPublicKey = getDefaultCallResponse(res);

		final String secretKeyParam = req.params(MethodParams.SECRET_KEY);

		try {
			final BigInteger secretKey = new BigInteger(secretKeyParam);
			final byte[] publicKeyBytes = SECURE_CLOUD_SHARE
					.newPublicKey(secretKey.toByteArray());

			final String message = "Public key generated";
			final String content = new BigInteger(publicKeyBytes).toString();

			newPublicKey.setMessage(message);
			newPublicKey.setContent(content);

			return newPublicKey;
		} catch (Exception e) {
			// ignore
		}

		return setHttpBadRequest(res);
	}

	@Override
	public CallResponse share(Request req, Response res) {
		final CallResponse share = getDefaultCallResponse(res);

		final String idParam = req.params(MethodParams.ID);
		final String publicKeyParam = req.params(MethodParams.PUBLIC_KEY);
		final String reEncryptionKeyParam = req
				.params(MethodParams.RE_ENCRYPTION_KEY);

		final boolean isParamsValid = idParam != null && publicKeyParam != null
				&& reEncryptionKeyParam != null;

		if (isParamsValid) {
			try {
				final int torrentId = Integer.parseInt(idParam);

				final boolean isShared = SECURE_CLOUD_SHARE.share(torrentId,
						publicKeyParam.getBytes(),
						reEncryptionKeyParam.getBytes());

				if (isShared) {
					final String message = String.format(
							"Torrent with id %d shared", torrentId);
					final String content = String.valueOf(isShared);

					share.setMessage(message);
					share.setContent(content);

					return share;
				}

			} catch (Exception e) {
				// ignore
			}
		}

		return setHttpBadRequest(res);
	}

	@Override
	public CallResponse upload(Request req, Response res) {
		final CallResponse upload = getDefaultCallResponse(res);

		final String bytesParam = req.params(MethodParams.BYTES);

		if (bytesParam != null) {
			final int torrentId = SECURE_CLOUD_SHARE.upload(bytesParam
					.getBytes());

			final String message = "Torrent uploaded";
			final String content = String.valueOf(torrentId);

			upload.setMessage(message);
			upload.setContent(content);

			return upload;
		}

		return setHttpBadRequest(res);
	}

	@Override
	public CallResponse download(Request req, Response res) {
		final CallResponse download = getDefaultCallResponse(res);

		final String idParam = req.params(MethodParams.ID);
		final String publicKeyParam = req.params(MethodParams.PUBLIC_KEY);

		final boolean isParamsValid = idParam != null && publicKeyParam != null;

		if (isParamsValid) {
			try {
				final int torrentId = Integer.parseInt(idParam);
				final byte[] publicKey = publicKeyParam.getBytes();

				final String message = "Download granted";
				final String content = SECURE_CLOUD_SHARE.download(torrentId,
						publicKey).toString();

				download.setMessage(message);
				download.setContent(content);

				return download;
			} catch (Exception e) {
				// ignore
			}

		}

		return setHttpBadRequest(res);
	}

	private CallResponse getDefaultCallResponse(Response res) {
		res.status(HttpURLConnection.HTTP_OK);

		return new CallResponse(res, null, null, HttpURLConnection.HTTP_OK);
	}

	/**
	 * This will ensure that a call to some.url/method/ will be redirected to
	 * some.url/method
	 * 
	 * @param method
	 *            The corresponding method name
	 */
	private void setRedirectTo(String method) {
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

	private CallResponse setHttpBadRequest(Response res) {
		return new CallResponse(res, null, "Bad request",
				HttpURLConnection.HTTP_BAD_REQUEST);
	}
}
