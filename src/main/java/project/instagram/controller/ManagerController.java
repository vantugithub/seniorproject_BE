package project.instagram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import project.instagram.common.enums.constants.AppConstants;
import project.instagram.response.MessageResponse;
import project.instagram.response.PagedResponse;
import project.instagram.response.UserResponse;
import project.instagram.service.ManagerService;

@RestController
@RequestMapping(value = "/api/manager/")
@CrossOrigin(origins = "*")
public class ManagerController {
	
	@Autowired
	private ManagerService managerService;
	
	@GetMapping(path = "/clients")
	public PagedResponse<UserResponse> findAllClients(
			@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {

		return managerService.findAllClients(page, size);
	}
	
	@PutMapping(path = "/client/updateactive/{staffId}")
	public ResponseEntity<MessageResponse> updateActiveClient(
			@PathVariable(name = "staffId", required = true) String staffId) {

		return managerService.updateActiveClient(staffId);
	}
	
	@GetMapping(path = "/client/{staffId}")
	public ResponseEntity<MessageResponse> getStaff(
			@PathVariable(name = "staffId", required = true) String staffId) {

		return managerService.getClient(staffId);
	}
	
}
