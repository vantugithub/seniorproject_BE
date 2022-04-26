package project.instagram.service.impl;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import project.instagram.common.enums.RequestName;
import project.instagram.common.enums.StatusRequestName;
import project.instagram.common.enums.constants.PackageConstants;
import project.instagram.entity.Client;
import project.instagram.entity.Package;
import project.instagram.entity.Request;
import project.instagram.entity.StatusOfRequest;
import project.instagram.entity.TransactionPackage;
import project.instagram.entity.TypeOfRequest;
import project.instagram.repository.ClientRepository;
import project.instagram.repository.PackageRepository;
import project.instagram.repository.RequestRepository;
import project.instagram.repository.TransactionPackageRepository;
import project.instagram.response.MessageResponse;
import project.instagram.security.SecurityAuditorAware;
import project.instagram.service.TransactionPackageService;
import project.instagram.utils.DateTimeZoneUtils;

@Service
public class TransactionPackageServiceImpl implements TransactionPackageService {
	
	@Value("${path.url}")
    private String URL;
	
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
	
	String createUrlPathConfirmPackage(Package upgradePackage) {
		StringBuilder urlPathConfirmUpgradePackage = new StringBuilder(URL+"api/client/package/upgrade/confirm?packageId=");
		urlPathConfirmUpgradePackage.append(upgradePackage.getId());
		
		return urlPathConfirmUpgradePackage.toString();
	}
	
	MessageResponse existsPackage(UUID packageId) {
		MessageResponse messageResponse = new MessageResponse();
		Optional<Package> existsPackage = packageRepository.findById(packageId);
		
		if (existsPackage.isEmpty()) {
			messageResponse.setMessage(PackageConstants.PACKAGE_NOT_EXISTS);
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());
		}
		
		return messageResponse;
	}
	
	MessageResponse existsValidPackageOfClient(Date currentDate, Client client) {
		MessageResponse messageResponse = new MessageResponse();
		Optional<TransactionPackage> existsValidPackageOfClient =  transactionPackageRepository
				.findByExpiredDateGreaterThanEqualAndClient(currentDate, client);
		
		if (!existsValidPackageOfClient.isEmpty()) {
			messageResponse.setStatus(HttpStatus.OK.value());
			messageResponse.setMessage(PackageConstants.YOUR_CURRENT_PACKAGE_IS_VALID);
			messageResponse.setData(existsValidPackageOfClient);
		}
			
		return messageResponse;
	}
	
	MessageResponse createRequest(Date currentDate, Client client) {
		
		MessageResponse messageResponse = new MessageResponse();
		
		Request newRequest = new Request();
		newRequest.setId(currentDate.toString()+"_"+client.getId().toString());
		newRequest.setTitle(PackageConstants.UPGRADE_PACKAGE);
		newRequest.setClientRequest(client);
		StatusOfRequest statusOfRequest = new StatusOfRequest(StatusRequestName.PENDING);
		newRequest.setStatusOfRequest(statusOfRequest);
		TypeOfRequest typeOfRequest = new TypeOfRequest(RequestName.SYSTEM_REQUEST);
		newRequest.setTypeOfRequest(typeOfRequest);
		
		newRequest = requestRepository.save(newRequest);
		
		if (newRequest == null) {
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());
			messageResponse.setMessage(PackageConstants.CREATED_REQUEST_FAILED);
		}
		
		return messageResponse;
	}
	
	ResponseEntity<MessageResponse> validateUpgradePackageRequestByClient(UUID packageId) {
		MessageResponse messageResponse = new MessageResponse();
		
		messageResponse = existsPackage(packageId);
		if ( messageResponse.getMessage() != null )
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		
		Client client = clientRepository.findByEmail(securityAuditorAware.getCurrentAuditor().get()).get();
		Date currentDate = dateTimeZoneUtils.getDateTimeZoneLondon();
		
		messageResponse = existsValidPackageOfClient(currentDate, client);
		if ( messageResponse.getMessage() != null )
			return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
		
		messageResponse = createRequest(currentDate, client);
		if ( messageResponse.getMessage() != null )
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		
		return null;
	}

	@Override
	public ResponseEntity<MessageResponse> upgradePackageByClient(String id) {
		UUID packageId = UUID.fromString(id);
		MessageResponse messageResponse = new MessageResponse();
		Optional<Package> upgradePackage = packageRepository.findById(packageId);
		
		if (validateUpgradePackageRequestByClient(packageId) != null) {
			return validateUpgradePackageRequestByClient(packageId);
		}
		
		String urlPathConfirmUpgradePackage = createUrlPathConfirmPackage(upgradePackage.get());
		messageResponse.setStatus(HttpStatus.OK.value());
		messageResponse.setMessage(PackageConstants.CONFIRM_TO_UPGRADE_PACKAGE);
		messageResponse.setData(urlPathConfirmUpgradePackage);
		
		return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
	}

	@Override
	public ResponseEntity<MessageResponse> confirmUpgradePackageByClient(String id) {
		MessageResponse messageResponse = new MessageResponse();
		UUID packageId = UUID.fromString(id);
		
		messageResponse = existsPackage(packageId);
		if ( messageResponse.getMessage() != null )
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		
		/*
		 * this is where code payment service
		 * .	.	.
		 * .	.	.
		 * .	.	.
		 */
		
		
		
		return null;
	}

}
