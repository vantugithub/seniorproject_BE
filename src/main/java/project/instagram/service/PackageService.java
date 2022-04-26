package project.instagram.service;

import org.springframework.http.ResponseEntity;

import project.instagram.request.PackageFormRequest;
import project.instagram.response.MessageResponse;

public interface PackageService {
	ResponseEntity<MessageResponse> createPackage(PackageFormRequest packageFormRequest);
	ResponseEntity<MessageResponse> updateActivePackage(String packageId);
}
