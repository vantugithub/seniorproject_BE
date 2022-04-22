package project.instagram.service;

import org.springframework.http.ResponseEntity;

import project.instagram.response.MessageResponse;

public interface TypeOfPackageService {
	ResponseEntity<MessageResponse> createTypeOfPackage(String typeOfPackageName);
}
