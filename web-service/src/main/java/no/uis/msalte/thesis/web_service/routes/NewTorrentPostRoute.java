package no.uis.msalte.thesis.web_service.routes;

import java.io.File;
import java.net.HttpURLConnection;
import java.nio.file.Paths;

import javax.servlet.http.Part;

import no.uis.msalte.thesis.web_service.model.WebServiceResponse;
import no.uis.msalte.thesis.web_service.model.WebServiceRoute;
import no.uis.msalte.thesis.web_service.util.WebServiceUtils;
import spark.Request;
import spark.Response;
import spark.RouteImpl;

public class NewTorrentPostRoute extends RouteImpl implements WebServiceRoute {

	public static final String PATH = String.format("/%s", FUNC_NEW_TORRENT);

	public NewTorrentPostRoute() {
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

				final String torrent = WebServiceUtils.SECURE_CLOUD_SHARE
						.newTorrent(file);

				if (torrent != null) {
					// everything fine, treat as HTTP_OK
					final String message = "New torrent generated";
					final String content = torrent;

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
