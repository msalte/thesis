package no.uis.msalte.thesis.web_service.model;

import spark.Request;
import spark.Response;

public interface MethodCalls {
	public static final String PATH_UI = "/ui";

	public static final String PATH_NEW_TORRENT = String.format(
			"/newTorrent/%s", MethodParams.BYTES);

	public static final String PATH_NEW_SECRET_KEY = "/newSecretKey";

	public static final String PATH_NEW_PUBLIC_KEY = String.format(
			"/newPublicKey/%s", MethodParams.SECRET_KEY);

	public static final String PATH_SHARE = String.format("/share/%s/%s/%s",
			MethodParams.ID, MethodParams.PUBLIC_KEY,
			MethodParams.RE_ENCRYPTION_KEY);

	public static final String PATH_UPLOAD = String.format("/upload/%s",
			MethodParams.BYTES);

	public static final String PATH_DOWNLOAD = String.format("/download/%s/%s",
			MethodParams.ID, MethodParams.PUBLIC_KEY);

	public CallResponse ui(Request req, Response res);
	public CallResponse newTorrent(Request req, Response res);
	public CallResponse newSecretKey(Request req, Response res);
	public CallResponse newPublicKey(Request req, Response res);
	public CallResponse share(Request req, Response res);
	public CallResponse upload(Request req, Response res);
	public CallResponse download(Request req, Response res);
}
