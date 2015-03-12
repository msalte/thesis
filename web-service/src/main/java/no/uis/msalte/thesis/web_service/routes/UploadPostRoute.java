package no.uis.msalte.thesis.web_service.routes;

import java.io.File;
import java.net.HttpURLConnection;
import java.nio.file.Paths;

import javax.servlet.http.Part;

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

		// treating all post requests as multipart/form-data
		request.raw().setAttribute("org.eclipse.multipartConfig",
				WebServiceUtil.MULTIPART_CONFIG);

		Part filePart = null;
		String publicKey = "";

		for (Part part : request.raw().getParts()) {
			if (part.getName().equals(PARAM_FILE)) {
				filePart = part;
			} else if (part.getName().equals(PARAM_PUBLIC_KEY)) {
				publicKey = WebServiceUtil.parseInputStream(part
						.getInputStream());
			}
		}

		final boolean isParamsValid = filePart != null && !publicKey.isEmpty();

		if (isParamsValid) {
			File tempFile = null;
			String extension = ".torrent";

			try {
				// TODO remove System.currentTimeMillis() in name?
				final String tempFileName = String.format("Temp%d.%s",
						System.currentTimeMillis(), extension);

				filePart.write(tempFileName);

				tempFile = Paths.get(
						String.format("%s//%s",
								WebServiceUtil.MULTIPART_CONFIG.getLocation(),
								tempFileName)).toFile();

				final String torrent = WebServiceUtil.SECURE_CLOUD_SHARE
						.upload(tempFile, publicKey);

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
				if (tempFile != null) {
					tempFile.delete();
				}
			}
		}

		return r;
	}

}
