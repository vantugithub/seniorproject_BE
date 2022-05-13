package project.instagram.service;

import org.springframework.http.ResponseEntity;

import project.instagram.response.MessageResponse;

public interface ClientService {
	
	ResponseEntity<MessageResponse> getValidPackage();
}
