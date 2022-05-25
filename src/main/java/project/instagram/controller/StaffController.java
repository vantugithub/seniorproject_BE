package project.instagram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import project.instagram.common.enums.constants.AppConstants;
import project.instagram.response.MessageResponse;
import project.instagram.response.PagedResponse;
import project.instagram.response.RequestResponse;
import project.instagram.service.StaffService;

@RestController
@RequestMapping(value = "/api/staff/")
@CrossOrigin(origins = "*")
public class StaffController {

	@Autowired
	private StaffService staffService;

	@GetMapping(path = "/requests")
	public PagedResponse<RequestResponse> findAllRequests(
			@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {

		return staffService.findAllNotPendingRequests(page, size);
	}

	@GetMapping(path = "/pendingRequests")
	public PagedResponse<RequestResponse> findAllPendingRequests(
			@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {

		return staffService.findAllPendingRequests(page, size);
	}

	@GetMapping(path = "/request/{requestId}")
	public ResponseEntity<MessageResponse> getDetailsRequest(
			@PathVariable(name = "requestId", required = true) String requestId) {

		return staffService.getDetailsRequest(requestId);
	}
}
