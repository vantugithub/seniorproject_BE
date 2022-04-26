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

import project.instagram.request.PackageFormRequest;
import project.instagram.response.MessageResponse;
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
	
	@PostMapping(value = "/admin/typeofpackage/create/{typeOfPackageName}")
	public ResponseEntity<MessageResponse> createTypeOfPackage(
			@PathVariable(name = "typeOfPackageName", required = true) String typeOfPackageName) {
		
		return typeOfPackageService.createTypeOfPackage(typeOfPackageName);
	}
	
	@PostMapping(value = "/admin/package/create")
	public ResponseEntity<MessageResponse> createPackage(@ModelAttribute PackageFormRequest packageFormRequest) {
		
		return packageService.createPackage(packageFormRequest);
	}
	
	@PutMapping(value = "/admin/package/{packageId}")
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
	public ResponseEntity<MessageResponse> confirmUpgradePackageByClient(
			@RequestParam(name = "packageId", required = true) String packageId) {
		
		return null;
	}
}
