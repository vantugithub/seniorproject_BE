package project.instagram.service;

import org.springframework.http.ResponseEntity;

import project.instagram.response.MessageResponse;

public interface TransactionPackageService {
	ResponseEntity<MessageResponse> upgradePackageByClient(String packageId);

	ResponseEntity<MessageResponse> confirmUpgradePackageWhenTheClientIsAMember(String packageId, String token);

	ResponseEntity<MessageResponse> confirmUpgradePackageWhenTheClientIsNotAMember(String packageId, String requestId);

	ResponseEntity<MessageResponse> purchaseExtraPackagesFromClient(String packageId);

	ResponseEntity<MessageResponse> getAnalysisClientUsingPackage(String yearStr, String typeOfPackageStr);
	
	ResponseEntity<Double> getSalesByYear(String yearStr);

	ResponseEntity<MessageResponse> getRevenueClientUsingPackages(String yearStr, String typeOfPackageStr);

	ResponseEntity<MessageResponse> getRevenueClientUsingExtraPackages(String yearStr);
	
	ResponseEntity<MessageResponse> getRevenueClientUsingExtraPackagesTemp(String yearStr,int numberOfTop);
}
