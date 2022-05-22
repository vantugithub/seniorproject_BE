package project.instagram.service.impl;

import java.util.Date;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import project.instagram.common.enums.constants.AccountConstants;
import project.instagram.common.enums.constants.PackageConstants;
import project.instagram.entity.Client;
import project.instagram.entity.Package;
import project.instagram.entity.TransactionPackage;
import project.instagram.repository.ClientRepository;
import project.instagram.repository.TransactionPackageRepository;
import project.instagram.response.MessageResponse;
import project.instagram.response.PackageResponse;
import project.instagram.response.TransactionPackageResponse;
import project.instagram.security.SecurityAuditorAware;
import project.instagram.service.ClientService;
import project.instagram.utils.DateTimeZoneUtils;

@Service
public class ClientServiceImpl implements ClientService {
	
	@Autowired
	private SecurityAuditorAware securityAuditorAware;
	
	@Autowired
	private DateTimeZoneUtils dateTimeZoneUtils;

	@Autowired
	private TransactionPackageRepository transactionPackageRepository;
	
	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
    private ModelMapper mapper;
	
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
	
	void updateMessageResponse(MessageResponse messageResponse, Object data, String message, HttpStatus httpStatus) {
		messageResponse.setMessage(message);
		messageResponse.setStatus(httpStatus.value());
		messageResponse.setData(data);
	}

	MessageResponse existsValidPackageOfClient(Date currentDate, Client client) {
		MessageResponse messageResponse = new MessageResponse();

		Optional<TransactionPackage> transactionPackage = transactionPackageRepository
				.findByExpiredDateGreaterThanEqualAndClientAndChildPackage(PackageConstants.PACKAGE_TYPE,
						client.getId().toString(), currentDate);

		if (checkExistsPackageOfClient(transactionPackage) == null) {
			return messageResponse;
		}

		TransactionPackageResponse transactionPackageResponse = checkExistsPackageOfClient(transactionPackage);

		updateMessageResponse(messageResponse, transactionPackageResponse,
				PackageConstants.YOUR_CURRENT_PACKAGE_IS_VALID, HttpStatus.OK);

		return messageResponse;
	}
	

	@Override
	public ResponseEntity<MessageResponse> getValidPackage() {
		MessageResponse messageResponse = new MessageResponse();
		Client client = clientRepository.findByEmail(securityAuditorAware.getCurrentAuditor().get()).get();
		Date currentDate = dateTimeZoneUtils.getDateTimeZoneGMT();
		
		if ( existsValidPackageOfClient(currentDate, client) == null ) {
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());
			messageResponse.setMessage(AccountConstants.CLIENT_IS_NOT_A_MEMBER);
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}
		messageResponse = existsValidPackageOfClient(currentDate, client);
		
		return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
	}

}
