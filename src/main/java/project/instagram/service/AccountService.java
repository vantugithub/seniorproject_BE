package project.instagram.service;

import org.springframework.http.ResponseEntity;

import project.instagram.request.LoginFormRequest;
import project.instagram.request.SignUpFormRequest;
import project.instagram.request.UpdateAccountRequest;
import project.instagram.response.MessageResponse;

public interface AccountService {
	
	ResponseEntity<MessageResponse> register(SignUpFormRequest signUpFormRequest);
	ResponseEntity<MessageResponse> verifiAccount(String id, String token);
	ResponseEntity<MessageResponse> loginForClient(LoginFormRequest loginFormRequest);
	ResponseEntity<MessageResponse> loginForStaff(LoginFormRequest loginFormRequest);
	ResponseEntity<MessageResponse> getAccount();
	ResponseEntity<MessageResponse> updateAccount(UpdateAccountRequest updateAccountRequest);
}
