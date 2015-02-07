package no.uis.msalte.thesis.web_service;

import no.uis.msalte.thesis.web_service.util.JsonRenderer;
import spark.Spark;

public class App {
	private static final String ACCEPT_TYPE = "application/json";
	private static final JsonRenderer JSON = new JsonRenderer();

	public static void main(String[] args) {

		Spark.get("/hello", (req, res) -> "Hello");

		Spark.get("/test", ACCEPT_TYPE, (req, res) -> {
			return new SomeObject("Title", "Message");
		}, JSON);

	}

	static class SomeObject {
		String title;
		String content;

		SomeObject(String title, String content) {
			this.title = title;
			this.content = content;
		}
	}
}
