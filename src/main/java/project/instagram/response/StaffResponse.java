package project.instagram.response;

import java.io.Serializable;

public class StaffResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -542490159488817151L;
	private String staffId;
	private String email;

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public StaffResponse(String staffId, String email) {
		this.staffId = staffId;
		this.email = email;
	}

	public StaffResponse() {
	}

}
