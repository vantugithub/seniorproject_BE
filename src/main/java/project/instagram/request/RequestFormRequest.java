package project.instagram.request;

public class RequestFormRequest {
	private String reason;
	private String title;

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public RequestFormRequest() {
	}

	public RequestFormRequest(String reason, String title) {
		this.reason = reason;
		this.title = title;
	}
}
