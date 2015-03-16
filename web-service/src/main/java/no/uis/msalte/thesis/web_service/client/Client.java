package no.uis.msalte.thesis.web_service.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import no.uis.msalte.thesis.web_service.model.WebServiceRoute;
import no.uis.msalte.thesis.web_service.server.Server;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;

public class Client {

	private static final String URL_TEMPLATE = "https://localhost:%d/%s";

	public static String get(String function) {
		final String url = String.format(URL_TEMPLATE, Server.HTTP_PORT,
				function);

		try {
			final GetMethod get = new GetMethod(url);
			final HttpClient client = new HttpClient();

			client.executeMethod(get);

			return new String(get.getResponseBody());

		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String post(String function, String[] params, String[] args,
			File file) {
		final String url = String.format(URL_TEMPLATE, Server.HTTP_PORT,
				function);

		int numParts = 0;

		final boolean shouldContainStringParts = params != null
				&& params.length != 0 && args != null && args.length != 0
				&& params.length == args.length;

		final boolean shouldContainFilePart = file != null;

		if (shouldContainStringParts) {
			numParts = params.length;
		}

		if (shouldContainFilePart) {
			numParts++;
		}

		try {
			Part[] parts = new Part[numParts];

			if (shouldContainStringParts) {
				for (int i = 0; i < params.length; i++) {
					parts[i] = new StringPart(params[i], args[i]);
				}
			}

			if (shouldContainFilePart) {
				parts[numParts - 1] = new FilePart(WebServiceRoute.PARAM_FILE,
						file);
			}

			final PostMethod post = new PostMethod(url);

			post.setRequestEntity(new MultipartRequestEntity(parts, post
					.getParams()));

			final HttpClient client = new HttpClient();

			client.executeMethod(post);

			return new String(post.getResponseBody());

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}
