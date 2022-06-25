package project.instagram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import project.instagram.response.MessageResponse;
import project.instagram.service.HashtagRunningHistoryService;
import project.instagram.service.TransactionPackageService;

@RestController
@RequestMapping(value = "/api/manager/analysis")
@CrossOrigin(origins = "*")
public class AnalysisController {

	@Autowired
	private TransactionPackageService transactionPackageService;
	
	@Autowired
	private HashtagRunningHistoryService hashtagRunningHistoryService;

	@GetMapping(path = "/client-using-package")
	public ResponseEntity<MessageResponse> getAnalysisClientUsingPackages(
			@RequestParam(name = "yearStr", required = true) String yearStr,
			@RequestParam(name = "typeOfPackageName", required = true) String typeOfPackageName) {

		return transactionPackageService.getAnalysisClientUsingPackage(yearStr, typeOfPackageName);
	}

	@GetMapping(path = "/sales-all/{yearStr}")
	public ResponseEntity<Double> getAllSaleByYear(@PathVariable(name = "yearStr", required = true) String yearStr) {

		return transactionPackageService.getSalesByYear(yearStr);
	}

	@GetMapping(path = "/revenue-package")
	public ResponseEntity<MessageResponse> getRevenueClientUsingPackages(
			@RequestParam(name = "yearStr", required = true) String yearStr,
			@RequestParam(name = "typeOfPackageName", required = true) String typeOfPackageName) {

		return transactionPackageService.getRevenueClientUsingPackages(yearStr, typeOfPackageName);
	}

	@GetMapping(path = "/reveneu-extra-package")
	public ResponseEntity<MessageResponse> getRevenueClientUsingExtraPackages(
			@RequestParam(name = "yearStr", required = true) String yearStr) {

		return transactionPackageService.getRevenueClientUsingExtraPackagesTemp(yearStr, 3);
	}

	@GetMapping(path = "/hashtags")
	public ResponseEntity<MessageResponse> getAnalysisHashtags(@RequestParam(name = "yearStr", required = true) String yearStr){ 
		
		return hashtagRunningHistoryService.getAnalysisHashtags(yearStr);
	}
}
