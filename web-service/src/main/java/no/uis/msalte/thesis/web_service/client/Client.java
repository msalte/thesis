package no.uis.msalte.thesis.web_service.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import no.uis.msalte.thesis.web_service.server.Server;
import no.uis.msalte.thesis.web_service.util.JsonRenderer;

public class Client {

	private static final String URL_TEMPLATE = "http://localhost:%d/%s";

	public static String call(String method, String param) {
		HttpURLConnection conn = null;

		if (param != null) {
			// TODO support multiple parameters

			// convert /method/:parameter to /method/argument
			method = method.substring(0, method.indexOf(":")).concat(param);
		}

		try {
			conn = (HttpURLConnection) new URL(String.format(URL_TEMPLATE,
					Server.HTTP_PORT, method)).openConnection();

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

	public static String post(String method, String[] params, String[] args) {
		final boolean hasParameters = params != null && params.length > 0
				&& args != null && args.length > 0;
		final String url = String
				.format(URL_TEMPLATE, Server.HTTP_PORT, method);

		final StringBuilder paramString = new StringBuilder();

		try {
			if (hasParameters) {
				for (int i = 0; i < params.length; i++) {
					paramString.append(params[i]);
					paramString.append("=");
					paramString.append(URLEncoder.encode(args[i], "UTF-8"));

					if (i + 1 < params.length) {
						/*
						 * if more parameters, append "&"
						 */
						paramString.append("&");
					}
				}
			}

			HttpURLConnection conn = (HttpURLConnection) new URL(url.toString())
					.openConnection();

			conn.setRequestMethod("POST");
			conn.setDoOutput(true);

			DataOutputStream stream = new DataOutputStream(
					conn.getOutputStream());

			stream.writeBytes(paramString.toString());
			stream.flush();
			stream.close();

			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				
				String result = JsonRenderer.RENDERER.fromJson(
						parseResponse(conn.getInputStream()), String.class);

				System.out.println(result);
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

		StringBuffer response = new StringBuffer();

		String line;

		while ((line = reader.readLine()) != null) {
			response.append(line);
		}

		return response.toString();
	}
}
