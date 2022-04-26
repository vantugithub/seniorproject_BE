package project.instagram.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import project.instagram.common.enums.constants.PackageConstants;
import project.instagram.entity.TypeOfPackage;
import project.instagram.repository.TypeOfPackageRepository;
import project.instagram.response.MessageResponse;
import project.instagram.service.TypeOfPackageService;

@Service
public class TypeOfPackageServiceImpl implements TypeOfPackageService {
	
	@Autowired
	private TypeOfPackageRepository typeOfPackageRepository;

	@Override
	public ResponseEntity<MessageResponse> createTypeOfPackage(String typeOfPackageName) {
		MessageResponse messageResponse = new MessageResponse();
		
		if (typeOfPackageRepository.existsByName(typeOfPackageName)) {
			messageResponse.setMessage(PackageConstants.TYPE_OF_PACKAGE_EXISTS);
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}
		
		TypeOfPackage typeOfPackage = typeOfPackageRepository.save(new TypeOfPackage(typeOfPackageName));
		
		if(typeOfPackage != null) {
			messageResponse.setMessage(PackageConstants.CREATED_TYPE_OF_PACKAGE_SUCCESSFULLY);
			messageResponse.setStatus(HttpStatus.OK.value());
			
			return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
		}
		
		messageResponse.setMessage(PackageConstants.CREATED_TYPE_OF_PACKAGE_FAILED);
		messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
	}

}
