package project.instagram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import project.instagram.common.enums.constants.AppConstants;
import project.instagram.response.PagedResponse;
import project.instagram.response.UserResponse;
import project.instagram.service.AdminService;

@RestController
@RequestMapping(value = "/api/admin/")
@CrossOrigin(origins = "*")
public class AdminController {
	
	@Autowired
	private AdminService adminService;

	@GetMapping(path = "/clients")
	public PagedResponse<UserResponse> findAllClients(
			@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {
		
		return adminService.findAllClients(page, size);
	}

}
