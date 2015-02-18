package no.uis.thesis.web_service.routes;

import java.net.HttpURLConnection;

import no.uis.msalte.thesis.web_service.model.WebServiceResponse;
import no.uis.msalte.thesis.web_service.model.WebServiceRoute;
import no.uis.msalte.thesis.web_service.util.WebServiceUtil;
import spark.Request;
import spark.Response;
import spark.RouteImpl;

public class UploadPostRoute extends RouteImpl implements WebServiceRoute {

	public static final String PATH = String.format("/%s", FUNC_UPLOAD);

	public UploadPostRoute() {
		super(PATH);
	}

	@Override
	public Object handle(Request request, Response response) throws Exception {
		final WebServiceResponse r = new WebServiceResponse();

		// by default, treat as BAD_REQUEST
		r.setStatus(HttpURLConnection.HTTP_BAD_REQUEST);
		r.setMessage("Invalid parameters");
		r.setContent(null);

		final String file = request.queryParams(PARAM_FILE);

		final boolean isParamValid = file != null && !file.isEmpty();

		if (isParamValid) {
			try {
				final String fileName = WebServiceUtil.SECURE_CLOUD_SHARE
						.upload(file);

				if (fileName != null) {
					// everything fine, treat as HTTP_OK
					final String message = "Torrent uploaded";
					final String content = fileName;

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
