package no.uis.msalte.thesis.web_service.routes;

import java.net.HttpURLConnection;

import javax.servlet.http.Part;

import no.uis.msalte.thesis.web_service.model.WebServiceResponse;
import no.uis.msalte.thesis.web_service.model.WebServiceRoute;
import no.uis.msalte.thesis.web_service.util.WebServiceUtils;
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

		// treating all post requests as multipart/form-data
		request.raw().setAttribute("org.eclipse.multipartConfig",
				WebServiceUtils.MULTIPART_CONFIG);

		String secretKey = "";

		for (Part part : request.raw().getParts()) {
			if (part.getName().equals(PARAM_SECRET_KEY)) {
				secretKey = WebServiceUtils.parseInputStream(part
						.getInputStream());
			}
		}

		final boolean isParamValid = !secretKey.isEmpty();

		if (isParamValid) {
			try {
				final String publicKey = WebServiceUtils.SECURE_CLOUD_SHARE
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
