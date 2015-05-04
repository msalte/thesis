package no.uis.msalte.thesis.web_service.routes;

import java.net.HttpURLConnection;

import javax.servlet.http.Part;

import no.uis.msalte.thesis.common.util.InputStreamUtils;
import no.uis.msalte.thesis.web_service.model.WebServiceResponse;
import no.uis.msalte.thesis.web_service.util.WebServiceUtils;
import spark.Request;
import spark.Response;

public class NewReEncryptionKeyPostRoute extends WebServiceRoute {
	public static final String PATH = String.format("/%s",
			FUNC_NEW_RE_ENCRYPTION_KEY);

	public NewReEncryptionKeyPostRoute() {
		super(PATH, true);
	}

	@Override
	public Object handle(Request request, Response response) throws Exception {
		final WebServiceResponse r = (WebServiceResponse) super.handle(request, response);

		String secretKey = "";
		String publicKey = "";

		for (Part part : request.raw().getParts()) {
			if (part.getName().equals(PARAM_SECRET_KEY)) {
				secretKey = InputStreamUtils.parse(part.getInputStream());
			} else if (part.getName().equals(PARAM_PUBLIC_KEY)) {
				publicKey = InputStreamUtils.parse(part.getInputStream());
			}
		}

		final boolean isParamValid = !secretKey.isEmpty()
				&& !publicKey.isEmpty();

		if (isParamValid) {
			try {
				final String reEncryptionKey = WebServiceUtils.SECURE_CLOUD_SHARE
						.newReEncryptionKey(secretKey, publicKey);

				if (reEncryptionKey != null) {
					// everything fine, treat as HTTP_OK
					final String message = "Re-encryption key generated";
					final String content = reEncryptionKey;

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
