package no.uis.msalte.thesis.web_service.model;

public class InterfaceEntry {
	private String path;
	private String args;
	private String returns;

	public InterfaceEntry(String path, String args, String returns) {
		this.path = path;
		this.args = args;
		this.returns = returns;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getArgs() {
		return args;
	}

	public void setArgs(String args) {
		this.args = args;
	}

	public String getReturns() {
		return returns;
	}

	public void setReturns(String returns) {
		this.returns = returns;
	}

}
