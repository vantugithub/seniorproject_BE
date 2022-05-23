package project.instagram.request;

public class CreateStaffRequest {
	
	private String email;
	private String password;
	private String roleName;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public CreateStaffRequest(String email, String password, String roleName) {
		this.email = email;
		this.password = password;
		this.roleName = roleName;
	}

	public CreateStaffRequest() {
	}

}
