package no.uis.thesis.web_service.routes;

import java.io.File;
import java.io.FileInputStream;

import no.uis.msalte.thesis.web_service.util.WebServiceUtil;
import spark.Request;
import spark.Response;
import spark.RouteImpl;

public class JsApiGetRoute extends RouteImpl {

	public static final String PATH = "/jsapi";

	public JsApiGetRoute() {
		super(PATH);
	}

	@Override
	public Object handle(Request request, Response response) throws Exception {
		final File html = WebServiceUtil.getFileResource("jsclient.html");
		final FileInputStream is = new FileInputStream(html);

		// returns a HTML file rendered by the browser
		return WebServiceUtil.parseInputStream(is);
	}

}
