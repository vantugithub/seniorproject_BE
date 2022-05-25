package project.instagram.service;

import org.springframework.http.ResponseEntity;

import project.instagram.request.RequestFormRequest;
import project.instagram.response.MessageResponse;

public interface ClientService {
	ResponseEntity<MessageResponse> getValidPackage();
	ResponseEntity<MessageResponse> getValidExtraPackages();
	ResponseEntity<MessageResponse> getDetailsValidTransactionPackage(String transactionPackageId);
	ResponseEntity<MessageResponse> createRequest(RequestFormRequest requestFormRequest);
}
