package project.instagram.response;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

public class MessageResponse implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Object data;

	private String error;

	private String message;

	private int status;

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public MessageResponse(Object data, String message, HttpStatus status) {
		this.data = data;
		this.message = message;
		this.status = status.value();
	}
	
	public MessageResponse(String message, HttpStatus status) {
		this.message = message;
		this.status = status.value();
	}

	public MessageResponse() {
	}
	
}
