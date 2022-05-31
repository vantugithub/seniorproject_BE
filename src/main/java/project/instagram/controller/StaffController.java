package project.instagram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import project.instagram.common.enums.constants.AppConstants;
import project.instagram.request.PackageFormRequest;
import project.instagram.response.MessageResponse;
import project.instagram.response.PackageResponse;
import project.instagram.response.PagedResponse;
import project.instagram.response.RequestResponse;
import project.instagram.service.PackageService;
import project.instagram.service.StaffService;

@RestController
@RequestMapping(value = "/api/staff/")
@CrossOrigin(origins = "*")
public class StaffController {

	@Autowired
	private StaffService staffService;
	
	@Autowired
	private PackageService packageService;

	@GetMapping(path = "/requests")
	public PagedResponse<RequestResponse> findAllRequests(
			@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {

		return staffService.findAllNotPendingRequests(page, size);
	}

	@GetMapping(path = "/requests/pending")
	public PagedResponse<RequestResponse> findAllPendingRequests(
			@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {

		return staffService.findAllPendingRequests(page, size);
	}

	@GetMapping(path = "/request/handle/{requestId}")
	public ResponseEntity<MessageResponse> getDetailsHandleRequest(
			@PathVariable(name = "requestId", required = true) String requestId) {

		return staffService.getDetailsRequest(requestId);
	}
	
	@GetMapping(path = "/extra-packages")
	public PagedResponse<PackageResponse> findAllExtraPackages(
			@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {

		return packageService.findAllExtraPackagesForStaff(page, size);
	}
	
	@PostMapping(value = "/extra-package/create")
	public ResponseEntity<MessageResponse> createExtraPackage(@ModelAttribute PackageFormRequest packageFormRequest) {
		
		return packageService.createExtraPackageByStaff(packageFormRequest);
	}
	
	
	@GetMapping(path = "/packages")
	public PagedResponse<PackageResponse> findAllPackages(
			@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {

		return packageService.findAllPackagesForStaff(page, size);
	}
}
