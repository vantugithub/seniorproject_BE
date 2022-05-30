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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import project.instagram.common.enums.constants.AppConstants;
import project.instagram.request.RequestFormRequest;
import project.instagram.response.HashtagRunningHistoryResponse;
import project.instagram.response.MessageResponse;
import project.instagram.response.PagedResponse;
import project.instagram.service.ClientService;
import project.instagram.service.HashtagRunningHistoryService;

@RestController
@RequestMapping(value = "/api/client")
@CrossOrigin(origins = "*")
public class ClientController {

	@Autowired
	private ClientService clientService;

	@Autowired
	private HashtagRunningHistoryService hashtagRunningHistoryService;

	@GetMapping(value = "/package/current")
	public ResponseEntity<MessageResponse> getCurrentValidPackageOfClient() throws AuthenticationException {

		return clientService.getValidPackage();
	}

	@GetMapping(value = "/extra-packages")
	public ResponseEntity<MessageResponse> getAllValidExtraPackages() {

		return clientService.getValidExtraPackages();
	}

	@GetMapping(value = "/extra-package/{transactionPackageId}")
	public ResponseEntity<MessageResponse> getDetailsValidExtraPackage(
			@PathVariable(name = "transactionPackageId", required = true) String transactionPackageId) {

		return clientService.getDetailsValidTransactionPackage(transactionPackageId);
	}

	@PostMapping(value = "/requests")
	public ResponseEntity<MessageResponse> createRequest(@RequestBody RequestFormRequest requestFormRequest) {

		return clientService.createRequest(requestFormRequest);
	}

	@GetMapping(value = "/hashtag-running/histories")
	public PagedResponse<HashtagRunningHistoryResponse> getAllHashtagRunningHistories(
			@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {

		return hashtagRunningHistoryService.findAllByClientByRunningTimeDesc(page, size);
	}
}
