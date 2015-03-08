package no.uis.msalte.thesis.web_service.model;

public interface WebServiceRoute {
	public static final String PARAM_FILE = "file";
	public static final String PARAM_FILE_EXTENSION = "extension";
	public static final String PARAM_SECRET_KEY = "secretKey";
	public static final String PARAM_PUBLIC_KEY = "publicKey";
	public static final String PARAM_FILE_NAME = "fileName";
	public static final String PARAM_RE_ENCRYPTION_KEY = "reEncryptionKey";

	public static final String FUNC_API = "api";
	public static final String FUNC_NEW_TORRENT = "newTorrent";
	public static final String FUNC_NEW_SECRET_KEY = "newSecretKey";
	public static final String FUNC_NEW_PUBLIC_KEY = "newPublicKey";
	public static final String FUNC_NEW_RE_ENCRYPTION_KEY = "newReEncryptionKey";
	public static final String FUNC_SHARE = "share";
	public static final String FUNC_UPLOAD = "upload";
	public static final String FUNC_DOWNLOAD = "download";
}
