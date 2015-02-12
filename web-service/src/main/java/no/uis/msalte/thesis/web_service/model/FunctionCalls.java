package no.uis.msalte.thesis.web_service.model;

import spark.Request;
import spark.Response;

public interface FunctionCalls {
	public static final String PARAM_TORRENT = "torrent";
	public static final String PARAM_SECRET_KEY = "secretKey";
	public static final String PARAM_PUBLIC_KEY = "publicKey";
	public static final String PARAM_ID = "id";
	public static final String PARAM_RE_ENCRYPTION_KEY = "reEncryptionKey";
	
	public static final String FUNC_UI = "ui";
	public static final String FUNC_NEW_TORRENT = "newTorrent";
	public static final String FUNC_NEW_SECRET_KEY = "newSecretKey";
	public static final String FUNC_NEW_PUBLIC_KEY = "newPublicKey";
	public static final String FUNC_SHARE = "share";
	public static final String FUNC_UPLOAD = "upload";
	public static final String FUNC_DOWNLOAD = "download";
	
	public CallResponse ui(Request req, Response res);
	public CallResponse newTorrent(Request req, Response res);
	public CallResponse newSecretKey(Request req, Response res);
	public CallResponse newPublicKey(Request req, Response res);
	public CallResponse share(Request req, Response res);
	public CallResponse upload(Request req, Response res);
	public CallResponse download(Request req, Response res);
}
