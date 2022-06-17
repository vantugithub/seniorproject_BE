package project.instagram.response;

import java.io.Serializable;

public class UpdateRequestProcessing implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String requestId;
	private String clientId;
	private String statusRequest;
	private String extraPackageName;

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getStatusRequest() {
		return statusRequest;
	}

	public void setStatusRequest(String statusRequest) {
		this.statusRequest = statusRequest;
	}

	public String getExtraPackageName() {
		return extraPackageName;
	}

	public void setExtraPackageName(String extraPackageName) {
		this.extraPackageName = extraPackageName;
	}

	public UpdateRequestProcessing(String requestId, String clientId, String statusRequest, String extraPackageName) {
		this.requestId = requestId;
		this.clientId = clientId;
		this.statusRequest = statusRequest;
		this.extraPackageName = extraPackageName;
	}

	public UpdateRequestProcessing() {
	}

}
