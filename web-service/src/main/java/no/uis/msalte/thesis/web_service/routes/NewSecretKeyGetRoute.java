package no.uis.msalte.thesis.web_service.routes;

import java.net.HttpURLConnection;

import no.uis.msalte.thesis.web_service.model.WebServiceResponse;
import no.uis.msalte.thesis.web_service.util.WebServiceUtils;
import spark.Request;
import spark.Response;

public class NewSecretKeyGetRoute extends WebServiceRoute {

	public static final String PATH = String.format("/%s", FUNC_NEW_SECRET_KEY);

	public NewSecretKeyGetRoute() {
		super(PATH, false);
	}

	@Override
	public Object handle(Request request, Response response) throws Exception {
		final WebServiceResponse r = (WebServiceResponse) super.handle(request, response);

		final String secretKey = WebServiceUtils.SECURE_CLOUD_SHARE
				.newSecretKey();

		if (secretKey != null) {
			final String message = "Secret key generated";
			final String content = secretKey;

			r.setStatus(HttpURLConnection.HTTP_OK);
			r.setMessage(message);
			r.setContent(content);
		}

		return r;
	}

}
