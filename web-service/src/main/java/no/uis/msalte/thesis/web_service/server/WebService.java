package no.uis.msalte.thesis.web_service.model;

import spark.Request;
import spark.Response;

public interface WebService {
	public static final String PARAM_FILE = "file";
	public static final String PARAM_FILE_EXT = "extension";
	public static final String PARAM_SECRET_KEY = "secretKey";
	public static final String PARAM_PUBLIC_KEY = "publicKey";
	public static final String PARAM_FILE_NAME = "fileName";
	public static final String PARAM_RE_ENCRYPTION_KEY = "reEncryptionKey";

	public static final String FUNC_API = "api";
	public static final String FUNC_NEW_TORRENT = "newTorrent";
	public static final String FUNC_NEW_SECRET_KEY = "newSecretKey";
	public static final String FUNC_NEW_PUBLIC_KEY = "newPublicKey";
	public static final String FUNC_SHARE = "share";
	public static final String FUNC_UPLOAD = "upload";
	public static final String FUNC_DOWNLOAD = "download";

	public WebServiceResponse ui(Request req, Response res);

	public WebServiceResponse newTorrent(Request req, Response res);

	public WebServiceResponse newSecretKey(Request req, Response res);

	public WebServiceResponse newPublicKey(Request req, Response res);

	public WebServiceResponse share(Request req, Response res);

	public WebServiceResponse upload(Request req, Response res);

	public WebServiceResponse download(Request req, Response res);
}
