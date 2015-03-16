package no.uis.msalte.thesis.web_service.routes;

import java.net.HttpURLConnection;

import javax.servlet.http.Part;

import no.uis.msalte.thesis.web_service.model.WebServiceResponse;
import no.uis.msalte.thesis.web_service.model.WebServiceRoute;
import no.uis.msalte.thesis.web_service.util.WebServiceUtils;
import spark.Request;
import spark.Response;
import spark.RouteImpl;

public class DecryptPostRoute extends RouteImpl implements WebServiceRoute {

	public static final String PATH = String.format("/%s", FUNC_DECRYPT);

	public DecryptPostRoute() {
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

		String ciphertext = "";
		String secretKey = "";

		for (Part part : request.raw().getParts()) {
			String name = part.getName();

			if (name.equals(PARAM_CIPHERTEXT)) {
				ciphertext = WebServiceUtils.parseInputStream(part
						.getInputStream());
			} else if (name.equals(PARAM_SECRET_KEY)) {
				secretKey = WebServiceUtils.parseInputStream(part
						.getInputStream());
			}
		}

		final boolean isParamsValid = !ciphertext.isEmpty()
				&& !secretKey.isEmpty();

		if (isParamsValid) {
			try {
				final String plaintext = WebServiceUtils.SECURE_CLOUD_SHARE
						.decrypt(ciphertext, secretKey);

				if (plaintext != null) {
					r.setStatus(HttpURLConnection.HTTP_OK);
					r.setMessage("Decryption successful");
					r.setContent(plaintext);
				}

			} catch (Exception e) {
				// ignore
			}
		}

		return r;
	}

}
