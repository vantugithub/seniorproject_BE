package project.instagram.socket.config;

public class Message {
	private String title;
	private String message;
	private Object object;

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Message(String title, String message) {
		this.title = title;
		this.message = message;
	}

	public Message() {
	}

	public Message(String title, String message, Object object) {
		this.title = title;
		this.message = message;
		this.object = object;
	}

}
