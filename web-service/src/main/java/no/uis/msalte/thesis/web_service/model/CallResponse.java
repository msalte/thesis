package no.uis.msalte.thesis.web_service.model;

import java.net.HttpURLConnection;

import spark.Response;

public class CallResponse {
	private int status;
	private String content;
	private String message;

	public CallResponse(Response response, String content, String message) {
		this.content = content;
		this.message = message;

		response.status(this.status = HttpURLConnection.HTTP_OK);
	}

	public CallResponse(Response response, String content, String message,
			int status) {
		this.content = content;
		this.message = message;
		this.status = status;

		response.status(status);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
