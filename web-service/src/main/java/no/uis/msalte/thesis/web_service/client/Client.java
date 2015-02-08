package no.uis.msalte.thesis.web_service.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Client {
	private static final String URL_TEMPLATE = "http://localhost:4567/%s";

	public static String call(String method, String param) {
		HttpURLConnection conn = null;

		if (param != null) {
			// convert /method/:parameter to /method/argument
			method = method.substring(0, method.indexOf(":")).concat(param);
		}

		try {
			conn = (HttpURLConnection) new URL(String.format(URL_TEMPLATE,
					method)).openConnection();

			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				return parseResponse(conn.getInputStream());
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "";
	}

	private static String parseResponse(InputStream stream) throws IOException {
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(stream));
		StringBuilder response = new StringBuilder();

		String line;

		while ((line = reader.readLine()) != null) {
			response.append(line);
		}

		return response.toString();
	}
}
