package no.uis.msalte.thesis.web_service.model;

public class InterfaceEntry {
	private String function;
	private String method;
	private String details;
	private String[] params;
	private String[] args;

	public InterfaceEntry(String method, String function, String[] params,
			String[] args, String message) {
		this.method = method;
		this.function = function;
		this.params = params;
		this.args = args;
		this.details = message;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public String[] getParams() {
		return params;
	}

	public void setParams(String[] params) {
		this.params = params;
	}

	public String[] getArgs() {
		return args;
	}

	public void setArgs(String[] args) {
		this.args = args;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

}
