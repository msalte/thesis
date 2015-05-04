package no.uis.msalte.thesis.web_service.routes;

import java.io.File;
import java.net.HttpURLConnection;
import java.nio.file.Paths;

import javax.servlet.http.Part;

import no.uis.msalte.thesis.web_service.model.WebServiceResponse;
import no.uis.msalte.thesis.web_service.util.WebServiceUtils;
import spark.Request;
import spark.Response;

public class NewTorrentPostRoute extends WebServiceRoute {

	public static final String PATH = String.format("/%s", FUNC_NEW_TORRENT);

	public NewTorrentPostRoute() {
		super(PATH, true);
	}

	@Override
	public Object handle(Request request, Response response) throws Exception {
		final WebServiceResponse r = (WebServiceResponse) super.handle(request, response);

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
