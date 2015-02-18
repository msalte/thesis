package no.uis.thesis.web_service.routes;

import java.net.HttpURLConnection;

import no.uis.msalte.thesis.web_service.model.WebServiceResponse;
import no.uis.msalte.thesis.web_service.model.WebServiceRoute;
import no.uis.msalte.thesis.web_service.util.WebServiceUtil;
import spark.Request;
import spark.Response;
import spark.RouteImpl;

public class SharePostRoute extends RouteImpl implements WebServiceRoute {

	public static final String PATH = String.format("/%s", FUNC_SHARE);

	public SharePostRoute() {
		super(PATH);
	}

	@Override
	public Object handle(Request request, Response response) throws Exception {
		final WebServiceResponse r = new WebServiceResponse();

		// by default, treat as BAD_REQUEST
		r.setStatus(HttpURLConnection.HTTP_BAD_REQUEST);
		r.setMessage("Invalid parameters or file does not exist");
		r.setContent(null);

		final String fileName = request.queryParams(PARAM_FILE_NAME);
		final String publicKey = request.queryParams(PARAM_PUBLIC_KEY);
		final String reEncryptionKey = request
				.queryParams(PARAM_RE_ENCRYPTION_KEY);

		final boolean isParamsValid = fileName != null && !fileName.isEmpty()
				&& publicKey != null && !publicKey.isEmpty()
				&& reEncryptionKey != null && !reEncryptionKey.isEmpty();

		if (isParamsValid) {
			try {
				final boolean isShareSuccess = WebServiceUtil.SECURE_CLOUD_SHARE
						.share(fileName, publicKey, reEncryptionKey);

				if (isShareSuccess) {
					final String message = String.format("Torrent %s shared",
							fileName);
					final String content = String.valueOf(isShareSuccess);

					// everything fine, treat as HTTP_OK
					r.setStatus(HttpURLConnection.HTTP_OK);
					r.setMessage(message);
					r.setContent(content);
				}
			} catch (Exception e) {
				// ignore
			}
		}

		return r;
	}
}
