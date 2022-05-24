package project.instagram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project.instagram.response.MessageResponse;
import project.instagram.service.ClientService;

@RestController
@RequestMapping(value = "/api/client")
@CrossOrigin(origins = "*")
public class ClientController {

	@Autowired
	private ClientService clientService;

	@GetMapping(value = "/package")
	public ResponseEntity<MessageResponse> getValidPackageOfClient() throws AuthenticationException {

		return clientService.getValidPackage();
	}

	@GetMapping(value = "/extrapackage")
	public ResponseEntity<MessageResponse> getAllValidExtraPackage() {

		return clientService.getValidExtraPackage();
	}

	@GetMapping(value = "/extrapackage/{transactionPackageId}")
	public ResponseEntity<MessageResponse> getDetailsValidExtraPackage(
			@PathVariable(name = "transactionPackageId", required = true) String transactionPackageId) {

		return clientService.getDetailsValidTransactionPackage(transactionPackageId);
	}
}
