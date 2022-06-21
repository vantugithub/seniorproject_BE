package project.instagram.response;

import java.io.Serializable;

public class BlackHashtagResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private boolean active;
	private StaffResponse staffResponse;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public StaffResponse getStaffResponse() {
		return staffResponse;
	}

	public void setStaffResponse(StaffResponse staffResponse) {
		this.staffResponse = staffResponse;
	}

	public BlackHashtagResponse() {
		super();
	}

	public BlackHashtagResponse(String name, boolean active, StaffResponse staffResponse) {
		this.name = name;
		this.active = active;
		this.staffResponse = staffResponse;
	}

}
