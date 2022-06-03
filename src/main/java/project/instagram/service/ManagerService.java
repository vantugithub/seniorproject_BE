package project.instagram.service;

import org.springframework.http.ResponseEntity;

import project.instagram.response.MessageResponse;
import project.instagram.response.PagedResponse;
import project.instagram.response.UserResponse;

public interface ManagerService {
	PagedResponse<UserResponse> findAllClients(int page, int size);

	ResponseEntity<MessageResponse> updateActiveClient(String clientId);

	ResponseEntity<MessageResponse> getClient(String clientId);
}
