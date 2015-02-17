package no.uis.msalte.thesis.web_service.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

public class WebServiceUtil {
	public static File getFileResource(String filename) throws URISyntaxException {
		return new File(WebServiceUtil.class.getClassLoader().getResource(filename)
				.toURI());
	}
	
	public static String parseInputStream(InputStream is) throws IOException {
		final BufferedReader br = new BufferedReader(new InputStreamReader(is));
		final StringBuilder response = new StringBuilder();

		String line;

		while ((line = br.readLine()) != null) {
			response.append(line);
		}

		return response.toString();
	}
}
