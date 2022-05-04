package project.instagram.service.impl;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import project.instagram.common.enums.RoleName;
import project.instagram.common.enums.constants.CommonConstants;
import project.instagram.common.enums.constants.UserConstants;
import project.instagram.common.enums.constants.Validation;
import project.instagram.entity.Client;
import project.instagram.entity.Role;
import project.instagram.repository.ClientRepository;
import project.instagram.repository.RoleRepository;
import project.instagram.request.LoginFormRequest;
import project.instagram.request.SignUpFormRequest;
import project.instagram.response.MessageResponse;
import project.instagram.security.JwtInMemoryClientDetailsService;
import project.instagram.security.JwtInMemoryStaffDetailsService;
import project.instagram.security.JwtTokenUtil;
import project.instagram.service.AccountService;
import project.instagram.utils.BCryptUtils;

@Service
public class AccountServiceImpl implements AccountService {
	
	@Value("${path.url}")
    private String URL;
	
	@Autowired
    private ModelMapper mapper;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private JwtInMemoryClientDetailsService jwtInMemoryClientDetailsService;
	
	@Autowired
	private JwtInMemoryStaffDetailsService jwtInMemoryStaffDetailsService;
	
	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private BCryptUtils bCryptUtils;
	
	@Autowired
    private JavaMailSender emailSender;
	
	@Autowired
	private RoleRepository roleRepository;
	
	MessageResponse validateLoginFormRequest(LoginFormRequest loginFormRequest) 
	{
		MessageResponse messageResponse = new MessageResponse();
		
		if (loginFormRequest.getEmail().equals("")) {
			messageResponse.setMessage(UserConstants.EMAIL_NULL);
		}
		
		else if (!loginFormRequest.getEmail().matches(Validation.EMAIL_PATTERN)) {
			messageResponse.setMessage(UserConstants.INVALID_EMAIL_FORMAT);
		}
		
		else if (loginFormRequest.getPassword().equals("")) {
			messageResponse.setMessage(UserConstants.PASSWORD_NULL);
		}
		
		if(messageResponse.getMessage() != null) {
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());
		}
		
		return messageResponse;
	}
	
	MessageResponse validationSignUpFormRequest(SignUpFormRequest signUpFormRequest)
	{
		MessageResponse messageResponse = new MessageResponse();
		
		if (signUpFormRequest.getEmail().equals("")) {
			messageResponse.setMessage(UserConstants.EMAIL_NULL);
		}
		
		else if (!signUpFormRequest.getEmail().matches(Validation.EMAIL_PATTERN)) {
			messageResponse.setMessage(UserConstants.INVALID_EMAIL_FORMAT);
		}
		
		else if (signUpFormRequest.getPassword().equals("")) {
			messageResponse.setMessage(UserConstants.PASSWORD_NULL);
		}
		
		else if (signUpFormRequest.getAddress().equals("")) {
			messageResponse.setMessage(UserConstants.ADDRESS_NULL);
		}
		
		else if (signUpFormRequest.getBiography().equals("")) {
			messageResponse.setMessage(UserConstants.BIOGRAPHY_NULL);
		}
		
		else if (signUpFormRequest.getFirstName().equals("")) {
			messageResponse.setMessage(UserConstants.FIRST_NAME_NULL);
		}
		
		else if (signUpFormRequest.getLastName().equals(""))	{
			messageResponse.setMessage(UserConstants.LAST_NAME_NULL);
		}
		
		else if (signUpFormRequest.getPhone().equals("")) {
			messageResponse.setMessage(UserConstants.PHONE_NULL);
		}
		
		else if (!clientRepository.findByEmail(signUpFormRequest.getEmail()).isEmpty()) {
			messageResponse.setMessage(UserConstants.EMAIL_EXISTS);
		}
		
		else if (roleRepository.findByName(RoleName.ROLE_CLIENT) == null) {
			messageResponse.setMessage(UserConstants.ROLE_NOT_EXISTS);
		}
		
		if(messageResponse.getMessage() != null) {
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());
		}
		
		return messageResponse;
	}
	
	boolean sendVerificationMail(SignUpFormRequest signUpFormRequest, Client client) {
		try {
			StringBuilder linkVerification = new StringBuilder(URL+"api/auth/register/active?id=");
			linkVerification.append(client.getId().toString());
			linkVerification.append("&token=");
			StringBuilder token = new StringBuilder(bCryptUtils.bcryptEncoder(client.getEmail()+client.getId().toString()));
			linkVerification.append(token);
			
			StringBuilder message = new StringBuilder("Wellcome ");
			message.append(signUpFormRequest.getEmail());
			message.append("\nYour link Verification is: ");
			message.append(linkVerification);
			message.append("\nPlease enter link on website to complete register");
			
			SimpleMailMessage messageEmail = new SimpleMailMessage();
			messageEmail.setTo(signUpFormRequest.getEmail());
			messageEmail.setSubject("Verification Link");
			messageEmail.setText(message.toString());
			emailSender.send(messageEmail);
		}
		catch (Exception e) {
			clientRepository.deleteById(client.getId());
			return false;
		}
		
		return true;
	}
	
	ResponseEntity<MessageResponse> login(LoginFormRequest loginFormRequest, UserDetailsService userDetailsService) {
		MessageResponse messageResponse = validateLoginFormRequest(loginFormRequest);
		if (messageResponse.getMessage() != null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		
		 UserDetails userDetails = userDetailsService.loadUserByUsername(loginFormRequest.getEmail());
		
		if (!bCryptUtils.compare(loginFormRequest.getPassword(), userDetails.getPassword())) {
			messageResponse.setMessage(UserConstants.INVALID_ACCOUNT);
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}

		final String token = jwtTokenUtil.generateToken(userDetails);
	    messageResponse.setData(token);
	    messageResponse.setMessage(CommonConstants.LOGIN_SUCCESS);
	    messageResponse.setStatus(HttpStatus.OK.value());
	    
		return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
	}

	@Override
	public ResponseEntity<MessageResponse> register(SignUpFormRequest signUpFormRequest) {
		
		MessageResponse messageResponse = validationSignUpFormRequest(signUpFormRequest);
		
		if (messageResponse.getMessage() != null )
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		
		Role roleClient = roleRepository.findByName(RoleName.ROLE_CLIENT);
		Client client = mapper.map(signUpFormRequest, Client.class);
		System.out.println(signUpFormRequest.getPassword());
		client.setPassword(bCryptUtils.bcryptEncoder(signUpFormRequest.getPassword()));
		client.setRole(roleClient);
		client = clientRepository.save(client);
		
		if (!sendVerificationMail(signUpFormRequest, client))
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		
		messageResponse.setMessage(CommonConstants.SUCCESS);
		messageResponse.setStatus(HttpStatus.OK.value());
		
		return ResponseEntity.ok(messageResponse);
	}

	@Override
	public ResponseEntity<MessageResponse> verifiAccount(String id, String token) {
		
		MessageResponse messageResponse = new MessageResponse();
		
		UUID idClient = UUID.fromString(id);
		Client client = clientRepository.findById(idClient).get();
		
		if (client == null) {
			messageResponse.setMessage(UserConstants.EMAIL_NOT_EXISTS);
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}
		if (client.isActive()) {
			messageResponse.setMessage(UserConstants.VERIFIED_ACCOUNT);
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}
		
		StringBuilder realToken = new StringBuilder(client.getEmail()+client.getId().toString());
		if(bCryptUtils.compare(realToken.toString(), token)) {
			client.setActive(true);
			clientRepository.save(client);
			messageResponse.setMessage(UserConstants.ACCOUNT_VERIFICATION_SUCCESSFUL);
			messageResponse.setStatus(HttpStatus.OK.value());
			return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
		}
		messageResponse.setMessage(UserConstants.ACCOUNT_VERIFICATION_FAILED);
		messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
	}

	@Override
	public ResponseEntity<MessageResponse> loginForClient(LoginFormRequest loginFormRequest) {
	    return login(loginFormRequest, jwtInMemoryClientDetailsService);
	}

	@Override
	public ResponseEntity<MessageResponse> loginForStaff(LoginFormRequest loginFormRequest) {
	    return login(loginFormRequest, jwtInMemoryStaffDetailsService);
	}

}
