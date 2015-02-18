package no.uis.msalte.thesis.web_service.routes;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import no.uis.msalte.thesis.web_service.model.HttpMethod;
import no.uis.msalte.thesis.web_service.model.ApiItem;
import no.uis.msalte.thesis.web_service.model.WebServiceResponse;
import no.uis.msalte.thesis.web_service.model.WebServiceRoute;
import spark.Request;
import spark.Response;
import spark.RouteImpl;

public class ApiGetRoute extends RouteImpl implements WebServiceRoute {
	public static final String PATH = String.format("/%s", FUNC_API);

	public ApiGetRoute() {
		super(PATH);
	}

	@Override
	public Object handle(Request request, Response response) throws Exception {
		int status = HttpURLConnection.HTTP_OK;

		response.status(status);

		final WebServiceResponse wsr = new WebServiceResponse();

		final String message = "The API - All valid function calls return a JSON object";
		final ArrayList<ApiItem> content = new ArrayList<ApiItem>();

		content.add(new ApiItem(HttpMethod.GET.name(), FUNC_API, null, null,
				"Calling this function gives an overview of the API"));

		content.add(new ApiItem(
				HttpMethod.POST.name(),
				FUNC_NEW_TORRENT,
				new String[] { PARAM_FILE, PARAM_FILE_EXT },
				new String[] { "bytes", "string" },
				"Call this function to generate a new torrent for the given file (Note: nothing is stored on the server)"));

		content.add(new ApiItem(HttpMethod.GET.name(), FUNC_NEW_SECRET_KEY,
				null, null, "Calling this function generates a new secret key"));

		content.add(new ApiItem(HttpMethod.POST.name(), FUNC_NEW_PUBLIC_KEY,
				new String[] { PARAM_SECRET_KEY }, new String[] { "bytes" },
				"Calling this function generates a new public key"));

		content.add(new ApiItem(HttpMethod.POST.name(), FUNC_SHARE,
				new String[] { PARAM_FILE_NAME, PARAM_PUBLIC_KEY,
						PARAM_RE_ENCRYPTION_KEY }, new String[] { "string",
						"bytes", "bytes" },
				"Call this function to share a torrent with someone else"));

		content.add(new ApiItem(HttpMethod.POST.name(), FUNC_UPLOAD,
				new String[] { PARAM_FILE }, new String[] { "bytes" },
				"Call this function to upload a torrent"));

		content.add(new ApiItem(HttpMethod.POST.name(), FUNC_DOWNLOAD,
				new String[] { PARAM_FILE_NAME, PARAM_PUBLIC_KEY },
				new String[] { "string", "bytes" },
				"Call this function to download a torrent"));

		wsr.setStatus(status);
		wsr.setMessage(message);
		wsr.setContent(content);

		return wsr;
	}
}