package no.uis.msalte.thesis.web_service.model;

import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import no.uis.msalte.thesis.secure_cloud.security.SecureCloudShareImpl;
import no.uis.msalte.thesis.secure_cloud.util.FilesUtil;
import spark.Request;
import spark.Response;
import spark.Spark;

public class WebServiceImpl implements WebService {

	private static final SecureCloudShareImpl SECURE_CLOUD_SHARE = new SecureCloudShareImpl();

	@Override
	public WebServiceResponse ui(Request req, Response res) {
		final WebServiceResponse ui = getDefaultWebServiceResponse(res);

		final String message = "The user interface - All valid function calls return a JSON object";
		final ArrayList<InterfaceEntry> content = new ArrayList<InterfaceEntry>();

		content.add(new InterfaceEntry(HttpMethod.GET.name(), FUNC_UI, null,
				null,
				"Calling this function gives an overview of the user interface"));

		content.add(new InterfaceEntry(HttpMethod.POST.name(),
				FUNC_NEW_TORRENT, null, null, "Not implemented"));

		content.add(new InterfaceEntry(HttpMethod.GET.name(),
				FUNC_NEW_SECRET_KEY, null, null,
				"Calling this function generates a new secret key"));

		content.add(new InterfaceEntry(HttpMethod.POST.name(),
				FUNC_NEW_PUBLIC_KEY, new String[] { PARAM_SECRET_KEY },
				new String[] { "bytes" },
				"Calling this function generates a new public key"));

		content.add(new InterfaceEntry(HttpMethod.POST.name(), FUNC_SHARE,
				new String[] { PARAM_FILE_NAME, PARAM_PUBLIC_KEY,
						PARAM_RE_ENCRYPTION_KEY },
				new String[] { "string, bytes, bytes" },
				"Call this function to share a torrent with someone else"));

		content.add(new InterfaceEntry(HttpMethod.POST.name(), FUNC_UPLOAD,
				new String[] { PARAM_TORRENT }, new String[] { "bytes" },
				"Call this function to upload a torrent"));

		content.add(new InterfaceEntry(HttpMethod.POST.name(), FUNC_DOWNLOAD,
				new String[] { PARAM_FILE_NAME, PARAM_PUBLIC_KEY },
				new String[] { "string, bytes" },
				"Call this function to download a torrent"));

		ui.setMessage(message);
		ui.setContent(content);

		setRedirectTo(HttpMethod.GET, FUNC_UI);

		return ui;
	}

	@Override
	public WebServiceResponse newTorrent(Request req, Response res) {
		return getBadRequest(res);
	}

	@Override
	public WebServiceResponse newSecretKey(Request req, Response res) {
		final WebServiceResponse newSecretKey = getDefaultWebServiceResponse(res);

		final byte[] secretKeyBytes = SECURE_CLOUD_SHARE.newSecretKey();

		final String message = "Secret key generated";
		final String content = new BigInteger(secretKeyBytes).toString();

		newSecretKey.setMessage(message);
		newSecretKey.setContent(content);

		setRedirectTo(HttpMethod.GET, FUNC_NEW_SECRET_KEY);

		return newSecretKey;
	}

	@Override
	public WebServiceResponse newPublicKey(Request req, Response res) {
		final WebServiceResponse newPublicKey = getDefaultWebServiceResponse(res);

		final String secretKeyParam = req.queryParams(PARAM_SECRET_KEY);

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

		return getBadRequest(res);
	}

	@Override
	public WebServiceResponse share(Request req, Response res) {
		final WebServiceResponse share = getDefaultWebServiceResponse(res);

		final String fileName = req.queryParams(PARAM_FILE_NAME);
		final String publicKey = req.queryParams(PARAM_PUBLIC_KEY);
		final String reEncryptionKey = req.queryParams(PARAM_RE_ENCRYPTION_KEY);

		final boolean isParamsValid = fileName != null && publicKey != null
				&& reEncryptionKey != null;

		if (isParamsValid) {
			try {
				final boolean isShared = SECURE_CLOUD_SHARE.share(fileName,
						publicKey.getBytes(), reEncryptionKey.getBytes());

				if (isShared) {
					final String message = String.format("Torrent %s shared",
							fileName);
					final String content = String.valueOf(isShared);

					share.setMessage(message);
					share.setContent(content);

					return share;
				}

			} catch (Exception e) {
				// ignore
			}
		}

		return getBadRequest(res);
	}

	@Override
	public WebServiceResponse upload(Request req, Response res) {
		final WebServiceResponse upload = getDefaultWebServiceResponse(res);

		final String bytes = req.queryParams(PARAM_TORRENT);

		final byte[] file = FilesUtil.decode(bytes);

		final String fileName = SECURE_CLOUD_SHARE.upload(file);

		if (fileName != null) {
			final String message = String.format("Torrent %s uploaded",
					fileName);
			final String content = fileName;

			upload.setMessage(message);
			upload.setContent(content);

			return upload;
		}

		return getBadRequest(res);
	}

	@Override
	public WebServiceResponse download(Request req, Response res) {
		final WebServiceResponse download = getDefaultWebServiceResponse(res);

		final String fileName = req.queryParams(PARAM_FILE_NAME);
		final String publicKey = req.queryParams(PARAM_PUBLIC_KEY);

		final boolean isParamsValid = fileName != null && publicKey != null;

		if (isParamsValid) {
			try {
				final byte[] file = SECURE_CLOUD_SHARE.download(fileName,
						publicKey.getBytes());

				if (file != null) {
					final String message = "Download granted";
					final String content = FilesUtil.encode(file);

					download.setMessage(message);
					download.setContent(content);

					return download;
				}
			} catch (Exception e) {
				// ignore
			}
		}

		return getBadRequest(res);
	}

	private WebServiceResponse getDefaultWebServiceResponse(Response res) {
		res.status(HttpURLConnection.HTTP_OK);

		return new WebServiceResponse(res, null, null,
				HttpURLConnection.HTTP_OK);
	}

	/**
	 * This will ensure that a call to some.url/function/ will be redirected to
	 * some.url/function
	 * 
	 * @param method
	 *            The HTTP method
	 * @param function
	 *            The corresponding function name
	 */
	public void setRedirectTo(HttpMethod method, String function) {
		String path = String.format("%s/", function);
		String redirect = String.format("/%s", function);

		switch (method) {
		case POST:
			Spark.post(path, (req, res) -> {
				res.redirect(redirect);
				return null;
			});
			break;
		case GET:
			Spark.get(path, (req, res) -> {
				res.redirect(redirect);
				return null;
			});
			break;
		default:
			break;
		}
	}

	private WebServiceResponse getBadRequest(Response res) {
		return new WebServiceResponse(res, null, "Bad Request",
				HttpURLConnection.HTTP_BAD_REQUEST);
	}
}
