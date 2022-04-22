package project.instagram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project.instagram.response.MessageResponse;
import project.instagram.service.TypeOfPackageService;

@RestController
@RequestMapping(value = "/api/admin/")
@CrossOrigin
public class PackageController {
	
	@Autowired
	private TypeOfPackageService typeOfPackageService;
	
	@PostMapping(value = "/typeofpackage/create/{typeOfPackageName}")
	public ResponseEntity<MessageResponse> createTypeOfPackage(
			@PathVariable(name = "typeOfPackageName", required = true) String typeOfPackageName) {
		
		return typeOfPackageService.createTypeOfPackage(typeOfPackageName);
	}
}
