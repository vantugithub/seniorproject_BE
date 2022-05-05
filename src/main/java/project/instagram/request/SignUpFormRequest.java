package project.instagram.request;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.lang.Nullable;

public class SignUpFormRequest {
	
    @Size(max = 50)
    @Nullable
    private String firstName;
    
    @Size(max = 50)
    @Nullable
    private String lastName;

    @Size(max = 60)
    @NotNull
    private String email;
    
    @Size(max = 11)
    @Nullable
    private String phone;
    
    @Size(max = 40)
    @NotNull
    private String password;
    
    @Size(max = 255)
    @Nullable
    private String address;
    
    @Size(max = 512)
    @Nullable
    private String biography;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBiography() {
		return biography;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}

	public SignUpFormRequest(@Size(max = 50) String firstName, @Size(max = 50) String lastName,
			@Size(max = 60) @Email String email, @Size(max = 11) String phone,
			@Size(min = 6, max = 40) String password, @Size(max = 255) String address,
			@Size(max = 512) String biography) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.password = password;
		this.address = address;
		this.biography = biography;
	}

	public SignUpFormRequest() {
	}
    
    
}
