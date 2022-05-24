package project.instagram.service;

import org.springframework.http.ResponseEntity;

import project.instagram.response.MessageResponse;

public interface ClientService {
	ResponseEntity<MessageResponse> getValidPackage();
	ResponseEntity<MessageResponse> getValidExtraPackage();
	ResponseEntity<MessageResponse> getDetailsValidTransactionPackage(String transactionPackageId);
}
