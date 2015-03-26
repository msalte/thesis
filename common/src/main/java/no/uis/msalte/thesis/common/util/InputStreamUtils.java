package no.uis.msalte.thesis.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class InputStreamUtils {
	
	public static String parse(InputStream is) throws IOException {
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
