package no.uis.msalte.thesis.web_service.routes;

import java.net.HttpURLConnection;

import no.uis.msalte.thesis.web_service.model.WebServiceResponse;
import no.uis.msalte.thesis.web_service.model.WebServiceRoute;
import no.uis.msalte.thesis.web_service.util.WebServiceUtil;
import spark.Request;
import spark.Response;
import spark.RouteImpl;

public class NewPublicKeyPostRoute extends RouteImpl implements WebServiceRoute {

	public static final String PATH = String.format("/%s", FUNC_NEW_PUBLIC_KEY);

	public NewPublicKeyPostRoute() {
		super(PATH);
	}

	@Override
	public Object handle(Request request, Response response) throws Exception {
		final WebServiceResponse r = new WebServiceResponse();

		// by default, treat as BAD_REQUEST
		r.setStatus(HttpURLConnection.HTTP_BAD_REQUEST);
		r.setMessage("Invalid parameter");
		r.setContent(null);

		final String secretKey = request.queryParams(PARAM_SECRET_KEY);

		final boolean isParamValid = secretKey != null && !secretKey.isEmpty();

		if (isParamValid) {
			try {
				final String publicKey = WebServiceUtil.SECURE_CLOUD_SHARE
						.newPublicKey(secretKey);

				if (publicKey != null) {
					// everything fine, treat as HTTP_OK
					final String message = "Public key generated";
					final String content = publicKey;

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
