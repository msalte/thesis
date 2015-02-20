package no.uis.msalte.thesis.web_service.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

import javax.servlet.MultipartConfigElement;

import no.uis.msalte.thesis.secure_cloud.security.SecureCloudShareImpl;

public class WebServiceUtil {

	public static final SecureCloudShareImpl SECURE_CLOUD_SHARE = new SecureCloudShareImpl();
	public static final MultipartConfigElement MULTIPART_CONFIG = new MultipartConfigElement(
			"C:\\Users\\Morten\\Desktop\\tempdir");

	public static File getFileResource(String filename)
			throws URISyntaxException {
		return new File(WebServiceUtil.class.getClassLoader()
				.getResource(filename).toURI());
	}

	public static String parseInputStream(InputStream is) throws IOException {
		final BufferedReader br = new BufferedReader(new InputStreamReader(is));
		final StringBuilder response = new StringBuilder();

		String line;

		while ((line = br.readLine()) != null) {
			response.append(line);
		}

		br.close();
		is.close();

		return response.toString();
	}
}
