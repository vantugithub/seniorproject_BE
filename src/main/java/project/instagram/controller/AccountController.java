package project.instagram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import project.instagram.request.LoginFormRequest;
import project.instagram.request.SignUpFormRequest;
import project.instagram.request.UpdateAccountRequest;
import project.instagram.response.MessageResponse;
import project.instagram.service.AccountService;
import project.instagram.service.ClientService;

@RestController
@RequestMapping(value = "/api/auth")
@CrossOrigin(origins = "*")
public class AccountController {

	@Autowired
	private AccountService accountService;
	
	@Autowired
	private ClientService clientService;

	@PostMapping(value = "/register")
	public ResponseEntity<MessageResponse> register(@Validated @RequestBody SignUpFormRequest signUpFormRequest) {

		return accountService.register(signUpFormRequest);
	}

	@GetMapping(value = "/register/active")
	public ResponseEntity<MessageResponse> accountVerification(@RequestParam(name = "id") String id,
			@RequestParam(name = "token") String token) {

		return accountService.verifiAccount(id, token);
	}

	@PostMapping(value = "/client/login")
	public ResponseEntity<MessageResponse> loginForClient(@RequestBody LoginFormRequest loginRequest)
			throws AuthenticationException {

		return accountService.loginForClient(loginRequest);
	}

	@PostMapping(value = "/staff/login")
	public ResponseEntity<MessageResponse> loginForStaff(@RequestBody LoginFormRequest loginFormRequest)
			throws AuthenticationException {

		return accountService.loginForStaff(loginFormRequest);
	}

	@GetMapping(value = "/infor")
	public ResponseEntity<MessageResponse> getAccount() throws AuthenticationException {

		return accountService.getAccount();
	}

	@PutMapping(value = "/update/infor")
	public ResponseEntity<MessageResponse> updateAccount(
			@RequestBody UpdateAccountRequest updateAccountRequest
			) throws AuthenticationException {

		return accountService.updateAccount(updateAccountRequest);
	}
	
	@GetMapping(value = "/client/confirm-request")
	public ResponseEntity<MessageResponse> requestsVerification(
			@RequestParam(name = "token", required = true) String token) {

		return clientService.verifiRequest(token);
	}

}
