package project.instagram.service;

import org.springframework.http.ResponseEntity;

import project.instagram.response.MessageResponse;

public interface TransactionPackageService {
	ResponseEntity<MessageResponse> upgradePackageByClient(String packageId);
	ResponseEntity<MessageResponse> confirmUpgradePackageByClient(String packageId);
}
