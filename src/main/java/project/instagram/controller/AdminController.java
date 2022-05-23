package project.instagram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import project.instagram.common.enums.constants.AppConstants;
import project.instagram.request.CreateStaffRequest;
import project.instagram.request.UpdateStaffRequest;
import project.instagram.response.MessageResponse;
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

	@GetMapping(path = "/staffs")
	public PagedResponse<UserResponse> findAllStaffs(
			@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {

		return adminService.findAllStaffs(page, size);
	}

	@GetMapping(path = "/managers")
	public PagedResponse<UserResponse> findAllManagers(
			@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {

		return adminService.findAllManagers(page, size);
	}

	@PostMapping(path = "/createstaff")
	public ResponseEntity<MessageResponse> createStaff(@RequestBody CreateStaffRequest createStaffRequest) {

		return adminService.createStaff(createStaffRequest);
	}

	@GetMapping(path = "/roles")
	public ResponseEntity<MessageResponse> findAllRoles() {
		
		return adminService.findAllRoles();
	}

	@PutMapping(path = "/lockingstaff/{staffId}")
	public ResponseEntity<MessageResponse> lockingStaff(
			@PathVariable(name = "staffId", required = true) String staffId) {

		return adminService.lockingStaff(staffId);
	}

	@PutMapping(path = "/update")
	public ResponseEntity<MessageResponse> updateStaff(
			@RequestBody UpdateStaffRequest updateStaffRequest) {

		return adminService.updateStaff(updateStaffRequest);
	}
	
	@GetMapping(path = "/staff/{staffId}")
	public ResponseEntity<MessageResponse> getStaff(
			@PathVariable(name = "staffId", required = true) String staffId) {

		return adminService.getStaff(staffId);
	}
}
