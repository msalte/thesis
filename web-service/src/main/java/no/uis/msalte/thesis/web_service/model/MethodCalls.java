package no.uis.msalte.thesis.web_service.model;

import java.util.List;

public interface MethodCalls {
	public static final String UI = "/ui";

	public static final String NEW_TORRENT = String.format("/newTorrent/%s",
			MethodParams.BYTES);

	public static final String NEW_SECRET_KEY = "/newSecretKey";

	public static final String NEW_PUBLIC_KEY = String.format(
			"/newPublicKey/%s", MethodParams.SECRET_KEY);

	public static final String SHARE = String.format("/share/%s/%s/%s",
			MethodParams.ID, MethodParams.PUBLIC_KEY,
			MethodParams.RE_ENCRYPTION_KEY);

	public static final String UPLOAD = String.format("/upload/%s",
			MethodParams.BYTES);

	public static final String DOWNLOAD = String.format("/download/%s/%s",
			MethodParams.ID, MethodParams.PUBLIC_KEY);
	
	public List<InterfaceEntry> ui();

}
