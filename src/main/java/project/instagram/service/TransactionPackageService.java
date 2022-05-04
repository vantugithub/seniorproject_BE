package project.instagram.service;

import org.springframework.http.ResponseEntity;

import project.instagram.response.MessageResponse;

public interface TransactionPackageService {
	ResponseEntity<MessageResponse> upgradePackageByClient(String packageId);
	ResponseEntity<MessageResponse> confirmUpgradePackageWhenTheClientIsAMember(String packageId,String token);
	ResponseEntity<MessageResponse> confirmUpgradePackageWhenTheClientIsNotAMember(String packageId,String requestId);
	ResponseEntity<MessageResponse> purchaseExtraPackagesFromClient(String packageId);
}
