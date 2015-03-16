package no.uis.msalte.thesis.web_service.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;

import no.uis.msalte.thesis.secure_cloud.security.SecureCloudShareImpl;

public class WebServiceUtils {

	public static final SecureCloudShareImpl SECURE_CLOUD_SHARE = new SecureCloudShareImpl();
	public static final MultipartConfigElement MULTIPART_CONFIG = new MultipartConfigElement(
			"C:\\Users\\Morten\\Desktop\\tempdir");

	public static File getFileResource(String filename)
			throws URISyntaxException {
		return new File(WebServiceUtils.class.getClassLoader()
				.getResource(filename).toURI());
	}

	public static String parseFileNameFromHeader(Part filePart) {
		final String header = filePart.getHeader("content-disposition");

		if (header == null) {
			return null;
		}

		String fileName = "";

		for (String s : header.split(";")) {
			String item = s.trim();

			if (item.startsWith("filename")) {
				item = item.replaceAll("\"", ""); // remove quotes

				fileName = item.substring(item.indexOf("=") + 1);
			}
		}

		return fileName;
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
