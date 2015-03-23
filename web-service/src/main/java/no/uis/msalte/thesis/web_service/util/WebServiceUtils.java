package no.uis.msalte.thesis.web_service.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;

import no.uis.msalte.thesis.bit_torrent.App;
import no.uis.msalte.thesis.secure_cloud.security.SecureCloudShareImpl;

public class WebServiceUtils {
	private static final String DIR_TEMP = "temp";

	public static final SecureCloudShareImpl SECURE_CLOUD_SHARE = new SecureCloudShareImpl();
	public static final MultipartConfigElement MULTIPART_CONFIG = new MultipartConfigElement(
			String.format("%s\\%s", App.DIR, DIR_TEMP));

	public static File getFileResource(String fileName)
			throws URISyntaxException {
		return new File(WebServiceUtils.class.getClassLoader()
				.getResource(fileName).toURI());
	}

	public static String parseFileNameFromHeader(Part filePart) {
		final String cd = filePart.getHeader("content-disposition");

		if (cd == null) {
			return null;
		}

		String fileName = "";

		for (String h : cd.split(";")) {
			String header = h.trim();

			if (header.startsWith("filename")) {
				header = header.replaceAll("\"", ""); // remove quotes

				fileName = header.substring(header.indexOf("=") + 1);
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
