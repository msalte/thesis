package no.uis.msalte.thesis.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class InputStreamUtils {

	/**
	 * Parses an InputStream object to a String object
	 * 
	 * @param is
	 *            The InputStream object to parse
	 * @return The String object representing the InputStream's content
	 * @throws IOException
	 */
	public static String parse(InputStream is) throws IOException {
		final BufferedReader br = new BufferedReader(new InputStreamReader(is));
		final StringBuilder sb = new StringBuilder();

		String line;

		while ((line = br.readLine()) != null) {
			sb.append(line);
		}

		br.close();
		is.close();

		return sb.toString();
	}
}
