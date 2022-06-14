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
import project.instagram.response.DataCrawlResponse;
import project.instagram.response.HashtagClientManagementResponse;
import project.instagram.response.HashtagRunningHistoryResponse;
import project.instagram.response.MessageResponse;
import project.instagram.response.PagedResponse;
import project.instagram.response.RequestResponse;
import project.instagram.response.TransactionPackageResponse;
import project.instagram.service.AnalysisService;
import project.instagram.service.ClientService;
import project.instagram.service.DataCrawlService;
import project.instagram.service.HashtagClientManagementService;
import project.instagram.service.HashtagRunningHistoryService;

@RestController
@RequestMapping(value = "/api/client")
@CrossOrigin(origins = "*")
public class ClientController {

	@Autowired
	private ClientService clientService;

	@Autowired
	private HashtagRunningHistoryService hashtagRunningHistoryService;

	@Autowired
	private DataCrawlService dataCrawlService;

	@Autowired
	private AnalysisService analysisService;

	@Autowired
	private HashtagClientManagementService hashtagClientManagementService;

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

	@GetMapping(value = "/data-crawls")
	public PagedResponse<DataCrawlResponse> getAllDataCrawls(
			@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
			@RequestParam(name = "hashtagRunningHistoryId", required = true) String hashtagRunningHistoryId) {

		return dataCrawlService.findAllDataCrawlsByHashtagRunningHistoryId(page, size, hashtagRunningHistoryId);
	}

	@GetMapping(value = "/hashtags/no-crawl")
	public PagedResponse<HashtagClientManagementResponse> getAllHashtagNoCrawl(
			@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {

		return hashtagClientManagementService.findAllHashtagNoCrawl(page, size);
	}

	@GetMapping(value = "/hashtags/crawl")
	public PagedResponse<HashtagClientManagementResponse> getAllHashtagCrawl(
			@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {

		return hashtagClientManagementService.findAllHashtagCrawl(page, size);
	}

	@PostMapping(value = "/search")
	public ResponseEntity<?> searchHashtag(@RequestParam(name = "hashtag", required = true) String hashtag) {

		return dataCrawlService.searchHashtag(hashtag);
	}

	@GetMapping(value = "/export/data-crawls")
	public PagedResponse<DataCrawlResponse> exportDataCrawls(
			@RequestParam(name = "hashtagRunningHistoryId", required = true) String hashtagRunningHistoryId) {

		return dataCrawlService.exportDataCrawls(hashtagRunningHistoryId);
	}

	@GetMapping(value = "/analysis/data-crawls")
	public ResponseEntity<MessageResponse> analysisDataCrawls(
			@RequestParam(name = "hashtagRunningHistoryId", required = true) String hashtagRunningHistoryId) {

		return analysisService.getAnalysisByDateAndClient(hashtagRunningHistoryId);
	}

	@GetMapping(value = "/transaction-packages")
	public PagedResponse<TransactionPackageResponse> getAllTransactionPackage(
			@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {

		return clientService.findAllTransactionPackage(page, size);
	}

	@GetMapping(value = "/hashtag-running/{hashtag}")
	public PagedResponse<HashtagRunningHistoryResponse> getAllHashtagRunningHistories(
			@PathVariable(name = "hashtag", required = true) String hashtag,
			@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {

		return hashtagRunningHistoryService.findAllByClientAndHashtag(page, size, hashtag);
	}

	@GetMapping(value = "/analysis")
	public ResponseEntity<MessageResponse> analysis(@RequestParam(name = "hashtag", required = true) String hashtagStr,
			@RequestParam(name = "startDate", required = true) String startDate,
			@RequestParam(name = "endDate", required = true) String endDate) {

		return analysisService.getAnalysisHashtagByPeriodOfTime(hashtagStr, startDate, endDate);
	}
	
	@GetMapping(value = "/pending-requests/histories")
	public PagedResponse<RequestResponse> getAllPendingRequestHistories(
			@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {

		return clientService.findAllPendingRequests(page, size);
	}
	
	@GetMapping(value = "/not-pending-requests/histories")
	public PagedResponse<RequestResponse> getAllNotPendingRequestHistories(
			@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {

		return clientService.findAllNotPendingRequests(page, size);
	}
	

}
