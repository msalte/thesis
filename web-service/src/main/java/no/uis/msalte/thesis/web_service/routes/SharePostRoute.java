package no.uis.msalte.thesis.web_service.routes;

import java.net.HttpURLConnection;

import javax.servlet.http.Part;

import no.uis.msalte.thesis.common.util.InputStreamUtils;
import no.uis.msalte.thesis.web_service.model.WebServiceResponse;
import no.uis.msalte.thesis.web_service.util.WebServiceUtils;
import spark.Request;
import spark.Response;

public class SharePostRoute extends WebServiceRoute {

	public static final String PATH = String.format("/%s", FUNC_SHARE);

	public SharePostRoute() {
		super(PATH, true);
	}

	@Override
	public Object handle(Request request, Response response) throws Exception {
		final WebServiceResponse r = (WebServiceResponse) super.handle(request, response);

		String fileName = "";
		String publicKey = "";
		String reEncryptionKey = "";

		for (Part part : request.raw().getParts()) {
			String name = part.getName();

			if (name.equals(PARAM_FILE_NAME)) {
				fileName = InputStreamUtils.parse(part.getInputStream());
			} else if (name.equals(PARAM_PUBLIC_KEY)) {
				publicKey = InputStreamUtils.parse(part.getInputStream());
			} else if (name.equals(PARAM_RE_ENCRYPTION_KEY)) {
				reEncryptionKey = InputStreamUtils.parse(part.getInputStream());
			}
		}

		final boolean isParamsValid = !fileName.isEmpty()
				&& !publicKey.isEmpty() && !reEncryptionKey.isEmpty();

		if (isParamsValid) {
			try {
				final boolean isShareSuccess = WebServiceUtils.SECURE_CLOUD_SHARE
						.share(fileName, publicKey, reEncryptionKey);

				if (isShareSuccess) {
					final String message = String.format("Torrent %s shared",
							fileName);
					final String content = String.valueOf(isShareSuccess);

					// everything fine, treat as HTTP_OK
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
