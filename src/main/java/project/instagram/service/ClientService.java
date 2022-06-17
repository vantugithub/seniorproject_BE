package project.instagram.service;

import org.springframework.http.ResponseEntity;

import project.instagram.request.RequestFormRequest;
import project.instagram.response.MessageResponse;
import project.instagram.response.PagedResponse;
import project.instagram.response.RequestResponse;
import project.instagram.response.TransactionPackageResponse;

public interface ClientService {
	ResponseEntity<MessageResponse> getValidPackage();

	ResponseEntity<MessageResponse> getValidExtraPackages();

	ResponseEntity<MessageResponse> getDetailsValidTransactionPackage(String transactionPackageId);

	ResponseEntity<MessageResponse> createRequest(RequestFormRequest requestFormRequest);

	PagedResponse<TransactionPackageResponse> findAllTransactionPackage(int page, int size);

	PagedResponse<RequestResponse> findAllNotPendingRequests(int page, int size);

	PagedResponse<RequestResponse> findAllPendingRequests(int page, int size);
	
	ResponseEntity<MessageResponse> verifiRequest(String token);
}
