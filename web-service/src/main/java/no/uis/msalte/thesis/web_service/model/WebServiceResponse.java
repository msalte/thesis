package no.uis.msalte.thesis.web_service.model;

public class WebServiceResponse {
	private int status;
	private String message;
	private Object content;

	public WebServiceResponse(int status, String message, Object content) {
		this.content = content;
		this.message = message;
		this.status = status;
	}

	public WebServiceResponse() {
		// empty
	};

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

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		
		sb.append(String.format("Status: %d\n", getStatus()));
		sb.append(String.format("Message: %s\n", getMessage()));
		sb.append(String.format("Content: %s\n", getContent()));
		
		return sb.toString();
	}
}
