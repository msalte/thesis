package no.uis.msalte.thesis.web_service.routes;

import java.net.HttpURLConnection;

import javax.servlet.http.Part;

import no.uis.msalte.thesis.web_service.model.WebServiceResponse;
import no.uis.msalte.thesis.web_service.model.WebServiceRoute;
import no.uis.msalte.thesis.web_service.util.WebServiceUtils;
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

		// treating all post requests as multipart/form-data
		request.raw().setAttribute("org.eclipse.multipartConfig",
				WebServiceUtils.MULTIPART_CONFIG);

		String fileName = "";
		String publicKey = "";

		for (Part part : request.raw().getParts()) {
			String name = part.getName();

			if (name.equals(PARAM_FILE_NAME)) {
				fileName = WebServiceUtils.parseInputStream(part
						.getInputStream());
			} else if (name.equals(PARAM_PUBLIC_KEY)) {
				publicKey = WebServiceUtils.parseInputStream(part
						.getInputStream());
			}
		}

		final boolean isParamsValid = !fileName.isEmpty()
				&& !publicKey.isEmpty();

		if (isParamsValid) {
			try {
				final String file = WebServiceUtils.SECURE_CLOUD_SHARE.download(
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
