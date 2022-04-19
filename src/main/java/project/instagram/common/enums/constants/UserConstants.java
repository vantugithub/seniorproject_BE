package project.instagram.common.enums.constants;

import org.springframework.stereotype.Component;

@Component
public class UserConstants {
	
	public static final String ACCOUNT_NOT_BANNED = "Account not yet banned!";
	
	public static final String ACCOUNT_VERIFICATION_SUCCESSFUL = "Account verification successful";
	
	public static final String ACCOUNT_VERIFICATION_FAILED = "Account verification failed";

	public static final String ACCOUNT_BANNED = "Your account is banned";
	
	public static final String VERIFIED_ACCOUNT = "Your account was verified";

	public static final String ACCOUNT_NOT_EXISTS = "Account isn't exist";

	public static final String ACCOUNT_NOT_PERMISSION = "You don't have permission";

	public static final String ADDRESS_NULL = "Address is required!";
	
	public static final String BIOGRAPHY_NULL ="Biography is required";

	public static final String EMAIL_NOT_EXISTS = "Could not find the Email!";

	public static final String EMAIL_NULL = "Email is required!";

	public static final String EMAIL_EXISTS = "Email has been existed!";

	public static final String USER_NOT_EXISTS = "User is not existed!";
	
	public static final String ROLE_NOT_EXISTS = "Role is not existed";

	public static final String USERNAME_EXISTS = "Username has been existed!";

	public static final String USERNAME_NULL = "Username is required!";

	public static final String FIRST_NAME_NULL = "FirstName is required!";
	
	public static final String PHONE_NULL = "Phone is required!";

	public static final String IS_MATCHING_OLD_PASSWORD = "The new password is matching the old password!";

	public static final String INVALID_USERNAME_FORMAT = "Username must contain more than 6 characters and less than 12, without spaces and special characters!";

	public static final String INVALID_PASSWORD_FORMAT = "Password must contain more than 6 characters and less than 30, include uppercase character, lowcase character, digit, and special characters!";

	public static final String INVALID_EMAIL_FORMAT = "Incorrect email format";

	public static final String INVALID_ACCOUNT = "Email or password is wrong";

	public static final String INVALID_CODE = "invalid code";
	
	public static final String IMAGE_DEFAULT = "Default image was attached";

	public static final String LAST_NAME_NULL = "LastName is required!";

	public static final String NOT_ACTIVATED = "Your account isn't activated";

	public static final String PASSWORD_NULL = "Password is required!";

	public static final String PASSWORD_NEW_NULL = "Please enter new password!";

	public static final String WRONG_OLD_PASSWORD = "Please enter the valid old password!";

	public static final String UPDATE_SUCCESS = "Updated information successly";

	public static final String GET_INFORMATION = "Get information successly";
	
}
