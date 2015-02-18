package no.uis.thesis.web_service.routes;

import java.net.HttpURLConnection;

import no.uis.msalte.thesis.web_service.model.WebServiceResponse;
import no.uis.msalte.thesis.web_service.model.WebServiceRoute;
import no.uis.msalte.thesis.web_service.util.WebServiceUtil;
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

		final String file = request.queryParams(PARAM_FILE);
		final String extension = request.queryParams(PARAM_FILE_EXT);

		final boolean isParamsValid = file != null && !file.isEmpty()
				&& extension != null && !extension.isEmpty();

		if (isParamsValid) {
			try {
				final String torrent = WebServiceUtil.SECURE_CLOUD_SHARE
						.newTorrent(file, extension);

				if (torrent != null) {
					// everything fine, treat as HTTP_OK
					final String message = "New torrent generated";
					final String content = torrent;

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
