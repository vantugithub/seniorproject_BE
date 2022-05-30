package project.instagram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import project.instagram.common.enums.constants.AppConstants;
import project.instagram.request.PackageFormRequest;
import project.instagram.response.MessageResponse;
import project.instagram.response.PackageResponse;
import project.instagram.response.PagedResponse;
import project.instagram.service.PackageService;
import project.instagram.service.TransactionPackageService;
import project.instagram.service.TypeOfPackageService;

@RestController
@RequestMapping(value = "/api/")
@CrossOrigin(origins = "*")
public class PackageController {
	
	@Autowired
	private TypeOfPackageService typeOfPackageService;
	
	@Autowired
	private PackageService packageService;
	
	@Autowired
	private TransactionPackageService transactionPackageService;
	
	@PostMapping(value = "/manager/typeofpackage/create/{typeOfPackageName}")
	public ResponseEntity<MessageResponse> createTypeOfPackage(
			@PathVariable(name = "typeOfPackageName", required = true) String typeOfPackageName) {
		
		return typeOfPackageService.createTypeOfPackage(typeOfPackageName);
	}
	
	@PostMapping(value = "/manager/package/create")
	public ResponseEntity<MessageResponse> createPackage(@ModelAttribute PackageFormRequest packageFormRequest) {
		
		return packageService.createPackage(packageFormRequest);
	}
	
	@PutMapping(value = "/manager/package/{packageId}")
	public ResponseEntity<MessageResponse> updateActivePackage(
			@PathVariable(name = "packageId", required = true) String packageId) {
		
		return packageService.updateActivePackage(packageId);
	}
	
	@PostMapping(value = "/client/package/upgrade/{packageId}")
	public ResponseEntity<MessageResponse> upgradePackageByClient(
			@PathVariable(name = "packageId", required = true) String packageId) {
		
		return transactionPackageService.upgradePackageByClient(packageId);
	}
	
	@GetMapping(value = "/client/package/upgrade/confirm") 
	public ResponseEntity<MessageResponse> confirmUpgradePackageWhenClientIsNotAMember(
			@RequestParam(name = "packageId", required = true) String packageId,
			@RequestParam(name = "requestId", required = true) String requestId
			) {
		
		return transactionPackageService.confirmUpgradePackageWhenTheClientIsNotAMember(packageId, requestId);
	}
	
	@GetMapping(value = "/client/package/upgrade/member/confirm") 
	public ResponseEntity<MessageResponse> confirmUpgradePackageWhenClientIsAMember(
			@RequestParam(name = "packageId", required = true) String packageId,
			@RequestParam(name = "token", required = true) String token
			) {
		
		return transactionPackageService.confirmUpgradePackageWhenTheClientIsAMember(packageId, token);
	}
	
	@PostMapping(value = "/client/extraPackage/purchase/{extraPackageId}")
	public ResponseEntity<MessageResponse> purchaseExtraPackageFromClient(
			@PathVariable(name = "extraPackageId", required = true) String extraPackageId
			) {
		
		return transactionPackageService.purchaseExtraPackagesFromClient(extraPackageId);
	}
	
	@GetMapping(path = "/packages")
	public PagedResponse<PackageResponse> findAllPackages(
			@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {

		return packageService.findAllPackagesForClient(page, size);
	}
	
	@GetMapping(path = "/extrapackages")
	public PagedResponse<PackageResponse> findAllExtraPackages(
			@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {

		return packageService.findAllExtraPackagesForClient(page, size);
	}
	
}
