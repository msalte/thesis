package no.uis.msalte.thesis.web_service.routes;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import no.uis.msalte.thesis.web_service.model.ApiItem;
import no.uis.msalte.thesis.web_service.model.WebServiceResponse;
import no.uis.msalte.thesis.web_service.model.WebServiceRoute;
import spark.Request;
import spark.Response;
import spark.RouteImpl;

public class ApiGetRoute extends RouteImpl implements WebServiceRoute {
	public static final String PATH = String.format("/%s", FUNC_API);

	private static final String METHOD_GET = "GET";
	private static final String METHOD_POST = "POST";

	public ApiGetRoute() {
		super(PATH);
	}

	@Override
	public Object handle(Request request, Response response) throws Exception {
		int status = HttpURLConnection.HTTP_OK;

		response.status(status);

		final WebServiceResponse r = new WebServiceResponse();

		final String message = "The API - All valid function calls return a JSON object";
		final ArrayList<ApiItem> content = new ArrayList<ApiItem>();

		content.add(new ApiItem(METHOD_GET, FUNC_API, null, null,
				"Calling this function gives an overview of the API.", null));

		content.add(new ApiItem(
				METHOD_POST,
				FUNC_NEW_TORRENT,
				new String[] { PARAM_FILE },
				new String[] { "binary" },
				"Call this function to generate a new torrent for the given file. (Note: nothing is stored on the server)",
				"A new torrent file encoded as a Base64 string."));

		content.add(new ApiItem(METHOD_GET, FUNC_NEW_SECRET_KEY, null, null,
				"Calling this function generates a new secret key.",
				"The new secret key."));

		content.add(new ApiItem(METHOD_POST, FUNC_NEW_PUBLIC_KEY,
				new String[] { PARAM_SECRET_KEY }, new String[] { "string" },
				"Calling this function generates a new public key.",
				"The new public key."));

		content.add(new ApiItem(METHOD_POST, FUNC_NEW_RE_ENCRYPTION_KEY,
				new String[] { PARAM_SECRET_KEY, PARAM_PUBLIC_KEY },
				new String[] { "string", "string" },
				"Calling this function generates a new re-encryption key.",
				"The new re-encryption key."));

		content.add(new ApiItem(
				METHOD_POST,
				FUNC_DECRYPT,
				new String[] { PARAM_CIPHERTEXT, PARAM_SECRET_KEY },
				new String[] { "string", "string" },
				"Calling this function attempts to decrypt the given ciphertext with the given secret key.",
				"The resulting plaintext."));

		content.add(new ApiItem(
				METHOD_POST,
				FUNC_SHARE,
				new String[] { PARAM_FILE_NAME, PARAM_PUBLIC_KEY,
						PARAM_RE_ENCRYPTION_KEY },
				new String[] { "string", "string", "string" },
				"Call this function to share a torrent with the given public key holder. The corresponding re-encryption key should derive from the source's secret key and the destination's public key.",
				"True if share was successful, false otherwise."));

		content.add(new ApiItem(
				METHOD_POST,
				FUNC_UPLOAD,
				new String[] { PARAM_FILE, PARAM_PUBLIC_KEY },
				new String[] { "binary", "string" },
				"Call this function to upload a torrent file. The torrent will be encrypted with the given public key.",
				"The uploaded torrent's new unique file name."));

		content.add(new ApiItem(
				METHOD_POST,
				FUNC_DOWNLOAD,
				new String[] { PARAM_FILE_NAME, PARAM_PUBLIC_KEY },
				new String[] { "string", "string" },
				"Call this function to download an encrypted torrent. The public key refers to the destination's public key and must reflect a corresponding call to the share function.",
				"The torrent file encrypted under the given public key and encoded as a Base64 string."));

		r.setStatus(status);
		r.setMessage(message);
		r.setContent(content);

		return r;
	}
}
