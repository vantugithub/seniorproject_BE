package project.instagram.response;

import java.io.Serializable;

public class ClientResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8624514552058980169L;
	private String clientId;
	private String email;

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public ClientResponse(String clientId, String email) {
		this.clientId = clientId;
		this.email = email;
	}

	public ClientResponse() {
	}

}
