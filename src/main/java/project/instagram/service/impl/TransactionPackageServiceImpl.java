package project.instagram.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import project.instagram.common.enums.RequestName;
import project.instagram.common.enums.StatusRequestName;
import project.instagram.common.enums.constants.AccountConstants;
import project.instagram.common.enums.constants.PackageConstants;
import project.instagram.common.enums.constants.RequestConstants;
import project.instagram.common.enums.constants.TransactionConstants;
import project.instagram.entity.Client;
import project.instagram.entity.Package;
import project.instagram.entity.Request;
import project.instagram.entity.StatusOfRequest;
import project.instagram.entity.TransactionPackage;
import project.instagram.entity.TypeOfPackage;
import project.instagram.entity.TypeOfRequest;
import project.instagram.repository.ClientRepository;
import project.instagram.repository.PackageRepository;
import project.instagram.repository.RequestRepository;
import project.instagram.repository.TransactionPackageRepository;
import project.instagram.repository.TypeOfPackageRepository;
import project.instagram.response.MessageResponse;
import project.instagram.response.PackageResponse;
import project.instagram.response.TransactionPackageResponse;
import project.instagram.security.SecurityAuditorAware;
import project.instagram.service.TransactionPackageService;
import project.instagram.utils.BCryptUtils;
import project.instagram.utils.DateTimeZoneUtils;

@Service
@Transactional
public class TransactionPackageServiceImpl implements TransactionPackageService {
	
	@Value("${path.url}")
    private String URL;
	
	private static final String PACKAGE_TYPE = "Package";
	
	private static final String EXTRA_PACKAGE_TYPE = "Extra package";
	
	@Autowired
    private ModelMapper mapper;
	
	@Autowired
	private SecurityAuditorAware securityAuditorAware;
	
	@Autowired
	private TransactionPackageRepository transactionPackageRepository;
	
	@Autowired
	private DateTimeZoneUtils dateTimeZoneUtils;
	
	@Autowired
	private RequestRepository requestRepository;
	
	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private PackageRepository packageRepository;
	
	@Autowired
	private TypeOfPackageRepository typeOfPackageRepository;
	
	@Autowired
	private BCryptUtils bCryptUtils;
	
	void updateMessageResponse(MessageResponse messageResponse, Object data, String message, HttpStatus httpStatus) {
		messageResponse.setMessage(message);
		messageResponse.setStatus(httpStatus.value());
		messageResponse.setData(data);
	}
	
	void updateMessageResponse(MessageResponse messageResponse, String message, HttpStatus httpStatus) {
		messageResponse.setMessage(message);
		messageResponse.setStatus(httpStatus.value());
	}
	
	void updateMessageResponse(MessageResponse messageResponse, Object data,String error ,String message, HttpStatus httpStatus) {
		messageResponse.setMessage(message);
		messageResponse.setError(error);
		messageResponse.setStatus(httpStatus.value());
		messageResponse.setData(data);
	}
	
	String createUrlPathConfirmPackage(UUID packageId, Request request) {
		Optional<Package> upgradePackage = packageRepository.findById(packageId);
		
		StringBuilder urlPathConfirmUpgradePackage = new StringBuilder(URL+"api/client/package/upgrade/confirm?packageId=");
		urlPathConfirmUpgradePackage.append(upgradePackage.get().getId()+"&requestId=");
		urlPathConfirmUpgradePackage.append(request.getId());
		
		return urlPathConfirmUpgradePackage.toString();
	}
	
	String createUrlPathConfirmPackage(UUID packageId, Client client) {
		Optional<Package> upgradePackage = packageRepository.findById(packageId);
		
		StringBuilder urlPathConfirmUpgradePackage = new StringBuilder(URL+"api/client/package/upgrade/confirm?packageId=");
		urlPathConfirmUpgradePackage.append(upgradePackage.get().getId().toString());
		urlPathConfirmUpgradePackage.append("&token=" + bCryptUtils.bcryptEncoder(upgradePackage.get().getId().toString()));
		urlPathConfirmUpgradePackage.append("_"+bCryptUtils.bcryptEncoder(client.getEmail()));
		
		return urlPathConfirmUpgradePackage.toString();
	}
	
	Boolean isExistentPackage(Optional<Package> existsPackage) {
		if ( existsPackage.isEmpty() ) {
			return true;
		}
		
		return false;
	}
	
	Boolean existsExtraPackage(Optional<Package> existsPackage) {
		if ( existsPackage.isEmpty() ) {
			return true;
		}
		
		return false;
	}
	
	TransactionPackageResponse checkExistsPackageOfClient(Optional<TransactionPackage> transactionPackage) {
		if ( transactionPackage.isEmpty() ) {
			return null;
		}
		TransactionPackageResponse transactionPackageResponse = 
				mapper.map(transactionPackage.get(), TransactionPackageResponse.class);
		Package packageOfClient = transactionPackage.get().getParentPackage();
		PackageResponse packageResponse = 
				mapper.map(packageOfClient, PackageResponse.class);
		transactionPackageResponse.setValidPackage(packageResponse);
		
		return transactionPackageResponse;
	}
	
	MessageResponse existsValidPackageOfClient(Date currentDate, Client client, UUID packageUUID) {
		MessageResponse messageResponse = new MessageResponse();
		
		Optional<TransactionPackage> transactionPackage = transactionPackageRepository.
				findByExpiredDateGreaterThanEqualAndClientAndChildPackage(PACKAGE_TYPE, client.getId().toString(), currentDate);
		
		if ( checkExistsPackageOfClient(transactionPackage) == null ) {
			return messageResponse;
		}
		
		TransactionPackageResponse transactionPackageResponse = checkExistsPackageOfClient(transactionPackage);

		String urlPathConfirmUpgradePackage = createUrlPathConfirmPackage(packageUUID, client);
		updateMessageResponse(messageResponse, transactionPackageResponse, 
				PackageConstants.YOUR_CURRENT_PACKAGE_IS_VALID, urlPathConfirmUpgradePackage, HttpStatus.OK);
			
		return messageResponse;
	}
	
	Boolean isAMember(Date currentDate, Client client) {
		Optional<TransactionPackage> transactionPackage = transactionPackageRepository.
				findByExpiredDateGreaterThanEqualAndClientAndChildPackage(PACKAGE_TYPE, client.getId().toString(), currentDate);
		
		TransactionPackageResponse transactionPackageResponse = checkExistsPackageOfClient(transactionPackage);
		
		if ( transactionPackageResponse == null ) {
			return false;
		}
		
		return true;
	}
	
	Request createRequest(Date currentDate, Client client, RequestName requestName, StatusRequestName statusRequestName) {
		Request newRequest = new Request();
		
		StringBuilder newRequestId = new StringBuilder(currentDate.toString().replace(" ", "")+"_"+client.getId().toString());
		newRequest.setId(newRequestId.toString());
		newRequest.setTitle(PackageConstants.UPGRADE_PACKAGE);
		newRequest.setClientRequest(client);
		StatusOfRequest statusOfRequest = new StatusOfRequest(statusRequestName);
		newRequest.setStatusOfRequest(statusOfRequest);
		TypeOfRequest typeOfRequest = new TypeOfRequest(requestName);
		newRequest.setTypeOfRequest(typeOfRequest);
		
		newRequest = requestRepository.save(newRequest);
		
		return newRequest;
	}
	
	ResponseEntity<MessageResponse> validateUpgradePackageRequestByClient(UUID packageUUID, Date currentDate, Client client) {
		MessageResponse messageResponse = new MessageResponse();
		Optional<Package> existsPackage = packageRepository.findById(packageUUID);
		
		if ( isExistentPackage(existsPackage) ) {
			updateMessageResponse(messageResponse, PackageConstants.PACKAGE_NOT_EXISTS, HttpStatus.BAD_REQUEST);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}
		
		messageResponse = existsValidPackageOfClient(currentDate, client, packageUUID);
		
		if ( messageResponse.getMessage() != null ) {
			return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
		}
		
		return null;
	}
	
	Calendar calculateCalendarOfPackage(Optional<Package> existsPackage) {
		Calendar calendar = null;
		if ( existsPackage.get().getNumberOfMonths() == 0 ) {
			return calendar;
		}
		calendar = Calendar.getInstance();
		Date currentDate = dateTimeZoneUtils.getDateTimeZoneGMT();
		calendar.setTime(currentDate);
		calendar.add(Calendar.DATE, existsPackage.get().getNumberOfMonths()*30);
		
		return calendar;
	}
	
	Calendar calculatePlusDaysForPackage(Optional<Package> existsPackage, TransactionPackage transactionPackage) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(transactionPackage.getExpiredDate());
		calendar.add(Calendar.DATE, existsPackage.get().getNumberOfMonths()*30);
		
		return calendar;
	}
	
	TransactionPackage createTransactionPackage(Optional<Package> existsPackage) {
		TransactionPackage newTransactionPackage = new TransactionPackage();
		Client client = clientRepository.findByEmail(securityAuditorAware.getCurrentAuditor().get()).get();
		Date currentDate = dateTimeZoneUtils.getDateTimeZoneGMT();
		
		newTransactionPackage.setClient(client);
		newTransactionPackage.setParentPackage(existsPackage.get());
		newTransactionPackage.setIssuedeDate(currentDate);
		
		Calendar calendar = calculateCalendarOfPackage(existsPackage);
		if ( calculateCalendarOfPackage(existsPackage) == null ) {
			newTransactionPackage.setExpiredDate(null);
		} else {
			newTransactionPackage.setExpiredDate(calendar.getTime());
		}
		newTransactionPackage =  transactionPackageRepository.save(newTransactionPackage);
		
		return newTransactionPackage;
	}
	
	TransactionPackage createTransactionPackage(Optional<Package> existsPackage, Optional<TransactionPackage> oldTransactionPackage) {
		
		TransactionPackage newTransactionPackage = new TransactionPackage();
		Client client = clientRepository.findByEmail(securityAuditorAware.getCurrentAuditor().get()).get();
		
		Calendar calendar = calculatePlusDaysForPackage(existsPackage, oldTransactionPackage.get());
		
		newTransactionPackage.setClient(client);
		newTransactionPackage.setParentPackage(existsPackage.get());
		newTransactionPackage.setExpiredDate(calendar.getTime());
		System.out.println(oldTransactionPackage.get().getExpiredDate().toString() + "concac");
		newTransactionPackage.setIssuedeDate(oldTransactionPackage.get().getExpiredDate());
		newTransactionPackage =  transactionPackageRepository.save(newTransactionPackage);
		
		return newTransactionPackage;
	}
	
	void updateStatusOfRequest(Optional<Request> requestOfClient,StatusRequestName statusRequestName) {
		requestOfClient.get().setStatusOfRequest(new StatusOfRequest(statusRequestName));
		requestRepository.save(requestOfClient.get());
	}
	
	boolean checkTokenUpgradePackageFromClient(String token, Client client, UUID packageUUID) {
		String[] tokens = token.split("_");
		if ( !bCryptUtils.compare(packageUUID.toString(), tokens[0]) ) {
			return false;
		}
		
		if ( !bCryptUtils.compare(client.getEmail(), tokens[1]) ) {
			return false;
		}
		
		return true;
	}
	
	ResponseEntity<MessageResponse> upgradePackageWhenTheClientIsAMember(String packageId, String token) {
		MessageResponse messageResponse = new MessageResponse();
		Client client = clientRepository.findByEmail(securityAuditorAware.getCurrentAuditor().get()).get();
		UUID packageUUID = UUID.fromString(packageId);
		
		if ( !checkTokenUpgradePackageFromClient(token, client, packageUUID) ) {
			updateMessageResponse(messageResponse, RequestConstants.REQUEST_FAILED, HttpStatus.BAD_REQUEST);
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}
		
		Optional<TypeOfPackage> packageType = typeOfPackageRepository.findByName(PACKAGE_TYPE);
		if ( packageType.isEmpty() ) {
			updateMessageResponse(messageResponse, PackageConstants.PACKAGE_NOT_EXISTS, HttpStatus.BAD_REQUEST);
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}
		
		Optional<Package> existsPackage = packageRepository.findPackageByIdAndTypeOfPackage(packageUUID, packageType.get());
		
		if ( isExistentPackage(existsPackage) ) {
			updateMessageResponse(messageResponse, PackageConstants.PACKAGE_NOT_EXISTS, HttpStatus.BAD_REQUEST);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}
			
		/*
		 * this is where code payment service
		 * .	.	 .
		 * .	.	 .
		 * .	.	 .
		 */
		
		Date currentDate = dateTimeZoneUtils.getDateTimeZoneGMT();
		Request newRequest = createRequest(currentDate, client, RequestName.SYSTEM_REQUEST, StatusRequestName.ACCEPTED);
		
		if ( newRequest == null ) {
			updateMessageResponse(messageResponse, RequestConstants.CREATED_REQUEST_FAILED, HttpStatus.BAD_REQUEST);
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}
		Optional<TransactionPackage> oldTransactionPackage = 
				transactionPackageRepository.findByExpiredDateGreaterThanEqualAndClientAndChildPackage(
						PACKAGE_TYPE, client.getId().toString(), currentDate);
		TransactionPackage newTransactionPackage = createTransactionPackage(existsPackage, oldTransactionPackage);
		
		if ( newTransactionPackage == null ) {
			updateMessageResponse(messageResponse, TransactionConstants.CREATED_TRANSACTION_PACKAGE_FAILED, 
					HttpStatus.BAD_REQUEST);
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}
		updateMessageResponse(messageResponse, PackageConstants.UPGRADE_PACKAGE_SUCCESSFULLY, HttpStatus.OK);
		
		return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
	}
	
	ResponseEntity<MessageResponse> upgradePackageWhenTheClientIsNotAMember(String packageId, String requestId) {
		MessageResponse messageResponse = new MessageResponse();
		UUID packageUUID = UUID.fromString(packageId);
		
		Optional<TypeOfPackage> packageType = typeOfPackageRepository.findByName(PACKAGE_TYPE);
		if ( packageType.isEmpty() ) {
			updateMessageResponse(messageResponse, PackageConstants.PACKAGE_NOT_EXISTS, HttpStatus.BAD_REQUEST);
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}
		
		Optional<Package> existsPackage = packageRepository.findPackageByIdAndTypeOfPackage(packageUUID, packageType.get());
		
		if ( isExistentPackage(existsPackage) ) {
			updateMessageResponse(messageResponse, PackageConstants.PACKAGE_NOT_EXISTS, HttpStatus.BAD_REQUEST);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}
			
		/*
		 * this is where code payment service
		 * .	.	 .
		 * .	.	 .
		 * .	.	 .
		 */
		
		Optional<Request> requestOfClient = requestRepository.findById(requestId);
		if ( requestOfClient.isEmpty() ) {
			messageResponse.setMessage(RequestConstants.REQUEST_NOT_EXISTS);
			updateMessageResponse(messageResponse, RequestConstants.REQUEST_NOT_EXISTS, HttpStatus.BAD_REQUEST);
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}
		
		updateStatusOfRequest(requestOfClient, StatusRequestName.ACCEPTED);
		
		TransactionPackage newTransactionPackage = createTransactionPackage(existsPackage);
		if ( newTransactionPackage == null ) {
			updateMessageResponse(messageResponse, TransactionConstants.UPDATE_TRANSACTION_PACKAGE_FAILED, HttpStatus.BAD_REQUEST);
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}
		updateMessageResponse(messageResponse, PackageConstants.UPGRADE_PACKAGE_SUCCESSFULLY, HttpStatus.OK);
		
		return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
	}
	
	@Override
	public ResponseEntity<MessageResponse> upgradePackageByClient(String id) {
		MessageResponse messageResponse = new MessageResponse();
		UUID packageId = UUID.fromString(id);
		
		Client client = clientRepository.findByEmail(securityAuditorAware.getCurrentAuditor().get()).get();
		Date currentDate = dateTimeZoneUtils.getDateTimeZoneGMT();
		
		if ( validateUpgradePackageRequestByClient(packageId, currentDate, client) != null ) {
			return validateUpgradePackageRequestByClient(packageId, currentDate, client);
		}
		
		Request newRequest = createRequest(currentDate, client, RequestName.SYSTEM_REQUEST, StatusRequestName.PENDING);
		
		if ( newRequest == null ) {
			updateMessageResponse(messageResponse, RequestConstants.CREATED_REQUEST_FAILED, HttpStatus.BAD_REQUEST);
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}
		
		String urlPathConfirmUpgradePackage = createUrlPathConfirmPackage(packageId, newRequest);
		
		updateMessageResponse(messageResponse, 
				urlPathConfirmUpgradePackage, PackageConstants.CONFIRM_TO_UPGRADE_PACKAGE, HttpStatus.OK);
		
		return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
	}

	@Override
	public ResponseEntity<MessageResponse> confirmUpgradePackageWhenTheClientIsAMember(String packageId, String token) {
		
		return upgradePackageWhenTheClientIsAMember(packageId, token);
	}

	@Override
	public ResponseEntity<MessageResponse> purchaseExtraPackagesFromClient(String packageId) {
		MessageResponse messageResponse = new MessageResponse();
		
		UUID packageUUID = UUID.fromString(packageId);
		Date currentDate = dateTimeZoneUtils.getDateTimeZoneGMT();
		Client client = clientRepository.findByEmail(securityAuditorAware.getCurrentAuditor().get()).get();
		
		if ( !isAMember(currentDate, client) ) {
			updateMessageResponse(messageResponse, AccountConstants.CLIENT_IS_NOT_A_MEMBER, HttpStatus.BAD_REQUEST);
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}
		
		Optional<TypeOfPackage> extraPackageType = typeOfPackageRepository.findByName(EXTRA_PACKAGE_TYPE);
		if ( extraPackageType.isEmpty() ) {
			updateMessageResponse(messageResponse, PackageConstants.TYPE_OF_PACKAGE_NOT_EXISTS, HttpStatus.BAD_REQUEST);
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}
		
		Optional<Package> existsExtraPackage = packageRepository.findPackageByIdAndTypeOfPackage(packageUUID, extraPackageType.get());
		
		if ( existsExtraPackage(existsExtraPackage) ) {
			updateMessageResponse(messageResponse, PackageConstants.PACKAGE_NOT_EXISTS, HttpStatus.BAD_REQUEST);
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}
		
		/*
		 * this is where code payment service
		 * .	.	 .
		 * .	.	 .
		 * .	.	 .
		 */
		
		Request newRequest = createRequest(currentDate, client, RequestName.SYSTEM_REQUEST, StatusRequestName.ACCEPTED);
		
		if ( newRequest == null ) {
			updateMessageResponse(messageResponse, RequestConstants.CREATED_REQUEST_FAILED, HttpStatus.BAD_REQUEST);
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}
		
		TransactionPackage newTransactionPackage = createTransactionPackage(existsExtraPackage);
		if ( newTransactionPackage == null ) {
			updateMessageResponse(messageResponse, TransactionConstants.CREATED_TRANSACTION_PACKAGE_FAILED, 
					HttpStatus.BAD_REQUEST);
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}
		updateMessageResponse(messageResponse, PackageConstants.PURCHASED_THE_EXTRA_PACKAGE_SUCCESSFULLY, HttpStatus.OK);
		
		return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
	}

	@Override
	public ResponseEntity<MessageResponse> confirmUpgradePackageWhenTheClientIsNotAMember(String packageId,
			String requestId) {
		
		return upgradePackageWhenTheClientIsNotAMember(packageId, requestId);
	}

}
