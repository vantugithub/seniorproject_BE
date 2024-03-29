package project.instagram.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
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
import project.instagram.common.enums.constants.PackageConstants;
import project.instagram.common.enums.constants.UserConstants;
import project.instagram.common.enums.constants.Validation;
import project.instagram.entity.Client;
import project.instagram.entity.Package;
import project.instagram.entity.Role;
import project.instagram.entity.Staff;
import project.instagram.entity.TransactionPackage;
import project.instagram.entity.TypeOfPackage;
import project.instagram.repository.ClientRepository;
import project.instagram.repository.PackageRepository;
import project.instagram.repository.RoleRepository;
import project.instagram.repository.StaffRepository;
import project.instagram.repository.TransactionPackageRepository;
import project.instagram.repository.TypeOfPackageRepository;
import project.instagram.request.LoginFormRequest;
import project.instagram.request.SignUpFormRequest;
import project.instagram.request.UpdateAccountRequest;
import project.instagram.response.MessageResponse;
import project.instagram.response.UserResponse;
import project.instagram.security.JwtInMemoryClientDetailsService;
import project.instagram.security.JwtInMemoryStaffDetailsService;
import project.instagram.security.JwtTokenUtil;
import project.instagram.security.SecurityAuditorAware;
import project.instagram.service.AccountService;
import project.instagram.utils.BCryptUtils;
import project.instagram.utils.DateTimeZoneUtils;

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

	@Autowired
	private SecurityAuditorAware securityAuditorAware;

	@Autowired
	private TypeOfPackageRepository typeOfPackageRepository;

	@Autowired
	private StaffRepository staffRepository;

	@Autowired
	private PackageRepository packageRepository;

	@Autowired
	private DateTimeZoneUtils dateTimeZoneUtils;

	@Autowired
	private TransactionPackageRepository transactionPackageRepository;

	private Calendar calculateCalendarOfPackage(Optional<Package> existsPackage) {
		Calendar calendar = null;
		if (existsPackage.get().getNumberOfMonths() == 0) {
			return calendar;
		}
		calendar = Calendar.getInstance();
		Date currentDate = dateTimeZoneUtils.getDateTimeZoneGMT();
		calendar.setTime(currentDate);
		calendar.add(Calendar.DATE, existsPackage.get().getNumberOfMonths() * 30);

		return calendar;
	}

	private void createTransactionPackage(UUID idClient, Optional<Package> existsPackage) {
		TransactionPackage newTransactionPackage = new TransactionPackage();

		Client client = clientRepository.findById(idClient).get();

		Date currentDate = dateTimeZoneUtils.getDateTimeZoneGMT();

		newTransactionPackage.setClient(client);
		newTransactionPackage.setParentPackage(existsPackage.get());
		newTransactionPackage.setIssuedeDate(currentDate);

		Calendar calendar = calculateCalendarOfPackage(existsPackage);
		if (calculateCalendarOfPackage(existsPackage) == null) {
			newTransactionPackage.setExpiredDate(null);
		} else {
			newTransactionPackage.setExpiredDate(calendar.getTime());
		}
		
		transactionPackageRepository.save(newTransactionPackage);
	}

	private void addClientToFree(UUID idClient) {

		Package freePackage = packageRepository.findPackageByName(PackageConstants.FREE_PACKAGE).get();

		Optional<TypeOfPackage> packageType = typeOfPackageRepository.findByName(PackageConstants.PACKAGE_TYPE);
		if (packageType.isEmpty()) {
			return;
		}

		Optional<Package> existsPackage = packageRepository.findPackageByIdAndTypeOfPackage(freePackage.getId(),
				packageType.get());

		/*
		 * this is where code payment service . . . . . . . . .
		 */

		createTransactionPackage(idClient, existsPackage);
	}

	private MessageResponse validateLoginFormRequest(LoginFormRequest loginFormRequest) {
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

		if (messageResponse.getMessage() != null) {
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());
		}

		return messageResponse;
	}

	private MessageResponse validationSignUpFormRequest(SignUpFormRequest signUpFormRequest) {
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

		else if (!clientRepository.findByEmail(signUpFormRequest.getEmail()).isEmpty()) {
			messageResponse.setMessage(UserConstants.EMAIL_EXISTS);
		}

		else if (roleRepository.findByName(RoleName.ROLE_CLIENT) == null) {
			messageResponse.setMessage(UserConstants.ROLE_NOT_EXISTS);
		}

		if (messageResponse.getMessage() != null) {
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());
		}

		return messageResponse;
	}

	private boolean sendVerificationMail(SignUpFormRequest signUpFormRequest, Client client) {
		try {
			StringBuilder linkVerification = new StringBuilder(URL + "api/auth/register/active?id=");
			linkVerification.append(client.getId().toString());
			linkVerification.append("&token=");
			StringBuilder token = new StringBuilder(
					bCryptUtils.bcryptEncoder(client.getEmail() + client.getId().toString()));
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
		} catch (Exception e) {
			clientRepository.deleteById(client.getId());
			return false;
		}

		return true;
	}

	private ResponseEntity<MessageResponse> login(LoginFormRequest loginFormRequest,
			UserDetailsService userDetailsService) {
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

	private UserResponse createUserResponse(Client client) {
		UserResponse userResponse = mapper.map(client, UserResponse.class);
		userResponse.setId(client.getId().toString());

		return userResponse;
	}

	private UserResponse createUserResponse(Staff staff) {
		UserResponse userResponse = mapper.map(staff, UserResponse.class);
		userResponse.setId(staff.getId().toString());

		return userResponse;
	}

	@Override
	public ResponseEntity<MessageResponse> register(SignUpFormRequest signUpFormRequest) {

		MessageResponse messageResponse = validationSignUpFormRequest(signUpFormRequest);

		if (messageResponse.getMessage() != null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);

		Role roleClient = roleRepository.findByName(RoleName.ROLE_CLIENT);
		Client client = mapper.map(signUpFormRequest, Client.class);
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

		StringBuilder realToken = new StringBuilder(client.getEmail() + client.getId().toString());
		if (bCryptUtils.compare(realToken.toString(), token)) {
			client.setActive(true);
			clientRepository.save(client);
			messageResponse.setMessage(UserConstants.ACCOUNT_VERIFICATION_SUCCESSFUL);
			messageResponse.setStatus(HttpStatus.OK.value());

			addClientToFree(idClient);

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

	@Override
	public ResponseEntity<MessageResponse> getAccount() {
		MessageResponse messageResponse = new MessageResponse();
		Optional<Client> client = clientRepository.findByEmail(securityAuditorAware.getCurrentAuditor().get());

		if (!client.isEmpty()) {
			messageResponse.setMessage(UserConstants.GET_INFORMATION_SUCCESSFULLY);
			messageResponse.setStatus(HttpStatus.OK.value());
			UserResponse userResponse = createUserResponse(client.get());
			messageResponse.setData(userResponse);

			return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
		}
		Optional<Staff> staff = staffRepository
				.findByEmailAndActiveTrue(securityAuditorAware.getCurrentAuditor().get());

		if (!staff.isEmpty()) {
			messageResponse.setMessage(UserConstants.GET_INFORMATION_SUCCESSFULLY);
			messageResponse.setStatus(HttpStatus.OK.value());
			UserResponse userResponse = createUserResponse(staff.get());
			messageResponse.setData(userResponse);

			return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
		}

		messageResponse.setMessage(UserConstants.ACCOUNT_NOT_EXISTS);
		messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);

	}

	@Override
	public ResponseEntity<MessageResponse> updateAccount(UpdateAccountRequest updateAccountRequest) {
		MessageResponse messageResponse = new MessageResponse();
		Optional<Client> client = clientRepository.findByEmail(securityAuditorAware.getCurrentAuditor().get());

		if (!client.isEmpty()) {
			updateAccount(updateAccountRequest, client.get());
			messageResponse.setMessage(UserConstants.UPDATE_SUCCESS);
			messageResponse.setStatus(HttpStatus.OK.value());

			return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
		}

		Optional<Staff> staff = staffRepository
				.findByEmailAndActiveTrue(securityAuditorAware.getCurrentAuditor().get());

		if (!staff.isEmpty()) {
			updateAccount(updateAccountRequest, staff.get());
			messageResponse.setMessage(UserConstants.UPDATE_SUCCESS);
			messageResponse.setStatus(HttpStatus.OK.value());

			return ResponseEntity.status(HttpStatus.OK).body(messageResponse);

		}
		messageResponse.setMessage(UserConstants.UPDATE_FAIL);
		messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
	}

	private void updateAccount(UpdateAccountRequest updateAccountRequest, Client client) {

		client.setAddress(updateAccountRequest.getAddress());
		client.setBiography(updateAccountRequest.getBiography());
		client.setFirstName(updateAccountRequest.getFirstName());
		client.setLastName(updateAccountRequest.getLastName());
		client.setPhone(updateAccountRequest.getPhone());

		client = clientRepository.save(client);
	}

	private void updateAccount(UpdateAccountRequest updateAccountRequest, Staff staff) {

		staff.setAddress(updateAccountRequest.getAddress());
		staff.setFirstName(updateAccountRequest.getFirstName());
		staff.setLastName(updateAccountRequest.getLastName());
		staff.setPhone(updateAccountRequest.getPhone());

		staff = staffRepository.save(staff);

	}

}
