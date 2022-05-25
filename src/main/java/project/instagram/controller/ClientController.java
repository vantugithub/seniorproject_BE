package project.instagram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project.instagram.request.RequestFormRequest;
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

	@GetMapping(value = "/extraPackage")
	public ResponseEntity<MessageResponse> getAllValidExtraPackage() {

		return clientService.getValidExtraPackages();
	}

	@GetMapping(value = "/extraPackage/{transactionPackageId}")
	public ResponseEntity<MessageResponse> getDetailsValidExtraPackage(
			@PathVariable(name = "transactionPackageId", required = true) String transactionPackageId) {

		return clientService.getDetailsValidTransactionPackage(transactionPackageId);
	}

	@PostMapping(value = "/requests")
	public ResponseEntity<MessageResponse> createRequest(@RequestBody RequestFormRequest requestFormRequest) {

		return clientService.createRequest(requestFormRequest);
	}
}
