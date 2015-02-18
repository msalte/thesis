package no.uis.msalte.thesis.web_service.routes;

import java.net.HttpURLConnection;

import no.uis.msalte.thesis.web_service.model.WebServiceResponse;
import no.uis.msalte.thesis.web_service.model.WebServiceRoute;
import no.uis.msalte.thesis.web_service.util.WebServiceUtil;
import spark.Request;
import spark.Response;
import spark.RouteImpl;

public class DownloadPostRoute extends RouteImpl implements WebServiceRoute {

	public static final String PATH = String.format("/%s", FUNC_DOWNLOAD);

	public DownloadPostRoute() {
		super(PATH);
	}

	@Override
	public Object handle(Request request, Response response) throws Exception {
		final WebServiceResponse r = new WebServiceResponse();

		// by default, treat as BAD_REQUEST
		r.setStatus(HttpURLConnection.HTTP_BAD_REQUEST);
		r.setMessage("Invalid parameters");
		r.setContent(null);

		final String fileName = request.queryParams(PARAM_FILE_NAME);
		final String publicKey = request.queryParams(PARAM_PUBLIC_KEY);

		final boolean isParamsValid = fileName != null && !fileName.isEmpty()
				&& publicKey != null && !publicKey.isEmpty();

		if (isParamsValid) {
			try {
				final String file = WebServiceUtil.SECURE_CLOUD_SHARE.download(
						fileName, publicKey);

				final boolean hasAccess = file != null;

				String message = "";
				String content = "";
				int status = 0;

				if (hasAccess) {
					status = HttpURLConnection.HTTP_OK;
					message = "Download granted";
					content = file;
				} else {
					status = HttpURLConnection.HTTP_BAD_REQUEST;
					message = "No access or file does not exist";
					content = null;
				}

				// everything fine, treat as HTTP_OK
				r.setStatus(status);
				r.setMessage(message);
				r.setContent(content);

			} catch (Exception e) {
				// ignore
			}
		}

		return r;
	}

}
