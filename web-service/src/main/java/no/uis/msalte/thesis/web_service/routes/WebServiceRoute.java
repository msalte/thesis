package no.uis.msalte.thesis.web_service.routes;

import java.net.HttpURLConnection;

import no.uis.msalte.thesis.web_service.model.WebServiceResponse;
import no.uis.msalte.thesis.web_service.util.WebServiceUtils;
import spark.Request;
import spark.Response;
import spark.RouteImpl;

public abstract class WebServiceRoute extends RouteImpl {

	public static final String PARAM_FILE = "file";
	public static final String PARAM_SECRET_KEY = "secretKey";
	public static final String PARAM_PUBLIC_KEY = "publicKey";
	public static final String PARAM_FILE_NAME = "fileName";
	public static final String PARAM_RE_ENCRYPTION_KEY = "reEncryptionKey";
	public static final String PARAM_CIPHERTEXT = "ciphertext";

	public static final String FUNC_API = "api";
	public static final String FUNC_DECRYPT = "decrypt";
	public static final String FUNC_NEW_TORRENT = "newTorrent";
	public static final String FUNC_NEW_SECRET_KEY = "newSecretKey";
	public static final String FUNC_NEW_PUBLIC_KEY = "newPublicKey";
	public static final String FUNC_NEW_RE_ENCRYPTION_KEY = "newReEncryptionKey";
	public static final String FUNC_SHARE = "share";
	public static final String FUNC_UPLOAD = "upload";
	public static final String FUNC_DOWNLOAD = "download";
	public static final String FUNC_ANNOUNCE = "announce";
	
	private boolean isPostRoute;
	
	public WebServiceRoute(String path, boolean isPostRoute) {
		super(path);
		
		this.isPostRoute = isPostRoute;
	}
	
	@Override
	public Object handle(Request request, Response response) throws Exception {
		final WebServiceResponse r = new WebServiceResponse();
		
		// by default, treat as BAD_REQUEST
		r.setStatus(HttpURLConnection.HTTP_BAD_REQUEST);
		r.setMessage("Invalid parameter(s)");
		r.setContent(null);
		
		if(isPostRoute) {
			// treating all post requests as multipart/form-data
			request.raw().setAttribute("org.eclipse.multipartConfig",
					WebServiceUtils.MULTIPART_CONFIG);
		}
		
		return r;
	}

}
