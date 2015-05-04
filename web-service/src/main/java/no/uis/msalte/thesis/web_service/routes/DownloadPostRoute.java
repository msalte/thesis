package no.uis.msalte.thesis.web_service.routes;

import java.net.HttpURLConnection;

import javax.servlet.http.Part;

import no.uis.msalte.thesis.common.util.InputStreamUtils;
import no.uis.msalte.thesis.web_service.model.WebServiceResponse;
import no.uis.msalte.thesis.web_service.util.WebServiceUtils;
import spark.Request;
import spark.Response;

public class DownloadPostRoute extends WebServiceRoute {

	public static final String PATH = String.format("/%s", FUNC_DOWNLOAD);

	public DownloadPostRoute() {
		super(PATH, true);
	}

	@Override
	public Object handle(Request request, Response response) throws Exception {
		final WebServiceResponse r = (WebServiceResponse) super.handle(request, response);

		String fileName = "";
		String publicKey = "";

		for (Part part : request.raw().getParts()) {
			String name = part.getName();

			if (name.equals(PARAM_FILE_NAME)) {
				fileName = InputStreamUtils.parse(part.getInputStream());
			} else if (name.equals(PARAM_PUBLIC_KEY)) {
				publicKey = InputStreamUtils.parse(part.getInputStream());
			}
		}

		final boolean isParamsValid = !fileName.isEmpty()
				&& !publicKey.isEmpty();

		if (isParamsValid) {
			try {
				final String file = WebServiceUtils.SECURE_CLOUD_SHARE
						.download(fileName, publicKey);

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
