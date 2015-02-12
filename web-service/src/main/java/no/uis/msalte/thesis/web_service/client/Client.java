package no.uis.msalte.thesis.web_service.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import no.uis.msalte.thesis.web_service.model.HttpMethod;
import no.uis.msalte.thesis.web_service.server.Server;

public class Client {

	private static final String URL_TEMPLATE = "http://localhost:%d/%s";

	public static String call(HttpMethod method, String function,
			String[] params, String[] args) {

		String url = String.format(URL_TEMPLATE, Server.HTTP_PORT, function);

		try {
			if (method == HttpMethod.GET) {
				// Write parameters as part of the URL
				url.concat(String.format("?%s",
						getParameterString(params, args)));
			}

			final HttpURLConnection conn = (HttpURLConnection) new URL(
					url.toString()).openConnection();

			conn.setRequestMethod(method.name());
			conn.setDoInput(true);
			conn.setDoOutput(true);

			if (method == HttpMethod.POST) {
				// Write the parameters in the body of the POST request

				final DataOutputStream stream = new DataOutputStream(
						conn.getOutputStream());

				stream.writeBytes(getParameterString(params, args));
				stream.flush();
				stream.close();
			}

			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				return getResponse(conn.getInputStream());
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "";
	}

	private static String getParameterString(String[] params, String[] args)
			throws UnsupportedEncodingException {

		final boolean hasParameters = params != null && params.length > 0
				&& args != null && args.length > 0;

		final StringBuilder paramString = new StringBuilder();

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

		return paramString.toString();
	}

	private static String getResponse(InputStream is) throws IOException {
		final BufferedReader br = new BufferedReader(new InputStreamReader(is));
		final StringBuilder response = new StringBuilder();

		String line;

		while ((line = br.readLine()) != null) {
			response.append(line);
		}

		return response.toString();
	}
}
