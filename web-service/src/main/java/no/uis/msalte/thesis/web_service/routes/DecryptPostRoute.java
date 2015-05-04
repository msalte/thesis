package no.uis.msalte.thesis.web_service.routes;

import java.net.HttpURLConnection;

import javax.servlet.http.Part;

import no.uis.msalte.thesis.common.util.InputStreamUtils;
import no.uis.msalte.thesis.web_service.model.WebServiceResponse;
import no.uis.msalte.thesis.web_service.util.WebServiceUtils;
import spark.Request;
import spark.Response;

public class DecryptPostRoute extends WebServiceRoute {

	public static final String PATH = String.format("/%s", FUNC_DECRYPT);

	public DecryptPostRoute() {
		super(PATH, true);
	}

	@Override
	public Object handle(Request request, Response response) throws Exception {
		final WebServiceResponse r = (WebServiceResponse) super.handle(request, response);

		String ciphertext = "";
		String secretKey = "";

		for (Part part : request.raw().getParts()) {
			String name = part.getName();

			if (name.equals(PARAM_CIPHERTEXT)) {
				ciphertext = InputStreamUtils.parse(part.getInputStream());
			} else if (name.equals(PARAM_SECRET_KEY)) {
				secretKey = InputStreamUtils.parse(part.getInputStream());
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
