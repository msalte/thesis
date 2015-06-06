package no.uis.msalte.thesis.common.security;

import static no.uis.msalte.thesis.common.AppConstants.DIR_APP;
import static no.uis.msalte.thesis.common.AppConstants.DIR_TLS;
import static no.uis.msalte.thesis.common.AppConstants.FILE_TLS_KEY_STORE;
import static no.uis.msalte.thesis.common.AppConstants.FILE_TLS_KEY_STORE_PW;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import no.uis.msalte.thesis.common.util.InputStreamUtils;

public class SecurityConstants {
	public static final SSLContext SSL_CONTEXT;

	static {
		SSL_CONTEXT = buildSSLContext();
	}

	private static SSLContext buildSSLContext() {
		final String keystoreLocation = String.format("%s\\%s\\%s", DIR_APP,
				DIR_TLS, FILE_TLS_KEY_STORE);

		final String passwordLocation = String.format("%s\\%s\\%s", DIR_APP,
				DIR_TLS, FILE_TLS_KEY_STORE_PW);

		FileInputStream fis = null;
		SSLContext context = null;

		char[] password = null;

		try {
			fis = new FileInputStream(passwordLocation);

			password = InputStreamUtils.parse(fis).toCharArray();

			fis.close();

			fis = new FileInputStream(keystoreLocation);

			KeyStore ks = KeyStore.getInstance("jks");
			KeyManagerFactory kmf = KeyManagerFactory
					.getInstance(KeyManagerFactory.getDefaultAlgorithm());

			ks.load(fis, password);
			kmf.init(ks, password);

			context = SSLContext.getInstance("TLS");
			context.init(kmf.getKeyManagers(), null, null);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return context;
	}
}
