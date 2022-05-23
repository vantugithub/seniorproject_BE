package project.instagram.service;

import org.springframework.http.ResponseEntity;

import project.instagram.request.CreateStaffRequest;
import project.instagram.request.UpdateStaffRequest;
import project.instagram.response.MessageResponse;
import project.instagram.response.PagedResponse;
import project.instagram.response.UserResponse;

public interface AdminService {
	PagedResponse<UserResponse> findAllClients(int page, int size);
	PagedResponse<UserResponse> findAllStaffs(int page, int size);
	PagedResponse<UserResponse> findAllManagers(int page, int size);
	ResponseEntity<MessageResponse> createStaff(CreateStaffRequest createStaffRequest);
	ResponseEntity<MessageResponse> findAllRoles();
	ResponseEntity<MessageResponse> lockingStaff(String staffId);
	ResponseEntity<MessageResponse> updateStaff(UpdateStaffRequest updateStaffRequest);
}
