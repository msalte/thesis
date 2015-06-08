package no.uis.msalte.thesis.common;

public class AppConstants {
	// ---- GENERAL ---- //
	/**
	 * The application's main directory, all other directories are
	 * sub-directories of this.
	 */
	public static final String DIR_APP = "C:\\Users\\Morten\\Desktop\\app_dir";
	/**
	 * True if logging is enabled, false otherwise.
	 */
	public static final boolean IS_LOG_ENABLED = true;

	// ---- TRANSPORT LAYER SECURITY (TLS) ---- //
	/**
	 * The directory containing all TLS related files.
	 */
	public static final String DIR_TLS = "tls";
	/**
	 * The Java KeyStore file.
	 */
	public static final String FILE_TLS_KEY_STORE = "web_service.jks";
	/**
	 * The file containing the Java KeyStore password.
	 */
	public static final String FILE_TLS_KEY_STORE_PW = "keystore.pw";

	// ---- WEB SERVICE ---- //
	/**
	 * The port on which the web service's web server will listen.
	 */
	public static final int WEB_SERVICE_PORT = 9090;
	
	// ---- DATABASE/PERSISTANCE LAYER ---- //
	/**
	 * The directory containing all database files.
	 */
	public static final String DIR_DB = "db";
	/**
	 * The database file.
	 */
	public static final String FILE_DB = "secure_share";

	// ---- CRYPTOGRAPHY ---- //
	/**
	 * The directory containing the curve files for the Java Pairing-Based
	 * Cryptography library.
	 */
	public static final String DIR_CURVES = "jpbc-curves";
	/**
	 * The curve in use by the AFGH implementation.
	 */
	public static final String FILE_CURVE = "a.properties";

	// ---- BITTORRENT ---- //
	/**
	 * The BitTorrent tracker's announce URI. This is used in the
	 * generation of torrent files. 
	 */
	public static final String ANNOUNCE_URI = "https://10.0.5.90:6969/announce";
	/**
	 * The BitTorrent piece length.
	 */
	public static final int PIECE_LENGTH = 256 * 1000;

	/**
	 * True if tracker is embedded, false otherwise
	 */
	public static final boolean IS_TRACKER_EMBEDDED = true;
}
