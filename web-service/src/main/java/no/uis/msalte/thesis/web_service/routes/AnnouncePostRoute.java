package no.uis.msalte.thesis.web_service.routes;

import java.io.File;
import java.net.HttpURLConnection;
import java.nio.file.Paths;

import javax.servlet.http.Part;

import no.uis.msalte.thesis.web_service.model.WebServiceResponse;
import no.uis.msalte.thesis.web_service.util.WebServiceUtils;
import spark.Request;
import spark.Response;

public class AnnouncePostRoute extends WebServiceRoute {

	public static final String PATH = String.format("/%s", FUNC_ANNOUNCE);

	public AnnouncePostRoute() {
		super(PATH, true);
	}

	@Override
	public Object handle(Request request, Response response) throws Exception {
		final WebServiceResponse r = (WebServiceResponse) super.handle(request,
				response);

		Part filePart = null;

		for (Part part : request.raw().getParts()) {
			if (part.getName().equals(PARAM_FILE)) {
				filePart = part;
			}
		}

		final boolean isParamsValid = filePart != null;

		if (isParamsValid) {
			File file = null;
			try {
				String fileName = WebServiceUtils
						.parseFileNameFromHeader(filePart);

				filePart.write(fileName);

				file = Paths.get(
						String.format("%s//%s",
								WebServiceUtils.MULTIPART_CONFIG.getLocation(),
								fileName)).toFile();

				final String result = WebServiceUtils.SECURE_CLOUD_SHARE
						.announce(file);

				final boolean isAnnounceSuccess = result.equals(fileName);

				if (isAnnounceSuccess) {
					// everything fine, treat as HTTP_OK
					final String message = "Torrent announced on the tracker";
					final String content = result;

					r.setStatus(HttpURLConnection.HTTP_OK);
					r.setMessage(message);
					r.setContent(content);
				}
			} catch (Exception e) {
				// Ignore
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
