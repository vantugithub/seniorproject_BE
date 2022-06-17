package project.instagram.service;

import org.springframework.http.ResponseEntity;

import project.instagram.response.MessageResponse;
import project.instagram.response.PagedResponse;
import project.instagram.response.RequestResponse;
import project.instagram.response.UpdateRequestProcessing;

public interface StaffService {
	PagedResponse<RequestResponse> findAllNotPendingRequests(int page, int size);

	PagedResponse<RequestResponse> findAllPendingRequests(int page, int size);

	ResponseEntity<MessageResponse> getDetailsRequest(String requestId);

	ResponseEntity<MessageResponse> updateRequest(UpdateRequestProcessing updateRequestProcessing);

	ResponseEntity<RequestResponse> getPendingRequest();
}
