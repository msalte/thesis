package no.uis.msalte.thesis.web_service.routes;

import java.io.File;
import java.net.HttpURLConnection;
import java.nio.file.Paths;

import javax.servlet.http.Part;

import no.uis.msalte.thesis.common.util.InputStreamUtils;
import no.uis.msalte.thesis.web_service.model.WebServiceResponse;
import no.uis.msalte.thesis.web_service.model.WebServiceRoute;
import no.uis.msalte.thesis.web_service.util.WebServiceUtils;
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

		// treating all post requests as multipart/form-data
		request.raw().setAttribute("org.eclipse.multipartConfig",
				WebServiceUtils.MULTIPART_CONFIG);

		Part filePart = null;
		String publicKey = "";

		for (Part part : request.raw().getParts()) {
			if (part.getName().equals(PARAM_FILE)) {
				filePart = part;
			} else if (part.getName().equals(PARAM_PUBLIC_KEY)) {
				publicKey = InputStreamUtils.parse(part.getInputStream());
			}
		}

		final boolean isParamsValid = filePart != null && !publicKey.isEmpty();

		if (isParamsValid) {
			File file = null;

			try {
				final String fileName = WebServiceUtils
						.parseFileNameFromHeader(filePart);

				filePart.write(fileName);

				file = Paths.get(
						String.format("%s//%s",
								WebServiceUtils.MULTIPART_CONFIG.getLocation(),
								fileName)).toFile();

				final String torrent = WebServiceUtils.SECURE_CLOUD_SHARE
						.upload(file, publicKey);

				if (torrent != null) {
					// everything fine, treat as HTTP_OK
					final String message = "Torrent uploaded successfuly";
					final String content = torrent;

					r.setStatus(HttpURLConnection.HTTP_OK);
					r.setMessage(message);
					r.setContent(content);
				}
			} catch (Exception e) {
				// ignore
			} finally {
				if (file != null) {
					file.delete();
				}
				if (filePart != null) {
					filePart.delete();
				}
			}
		}

		return r;
	}

}
