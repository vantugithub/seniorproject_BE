package project.instagram.service;

import project.instagram.response.PagedResponse;
import project.instagram.response.UserResponse;

public interface AdminService {
	PagedResponse<UserResponse> findAllClients(int page, int size);
}
