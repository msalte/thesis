package no.uis.msalte.thesis.web_service.routes;

import no.uis.msalte.thesis.web_service.model.WebServiceResponse;
import spark.Request;
import spark.Response;

public class AnnouncePostRoute extends WebServiceRoute {

	public static final String PATH = String.format("/%s", FUNC_ANNOUNCE);

	public AnnouncePostRoute() {
		super(PATH, true);
	}

	@Override
	public Object handle(Request request, Response response) throws Exception {
		final WebServiceResponse r = (WebServiceResponse) super.handle(request, response);
		
		// TODO
		
		return r;
	}

}
