package project.instagram.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import project.instagram.common.enums.RequestName;
import project.instagram.common.enums.StatusRequestName;
import project.instagram.common.enums.constants.AccountConstants;
import project.instagram.common.enums.constants.JobConstants;
import project.instagram.common.enums.constants.PackageConstants;
import project.instagram.common.enums.constants.RequestConstants;
import project.instagram.common.enums.constants.TransactionConstants;
import project.instagram.common.enums.constants.UserConstants;
import project.instagram.entity.Client;
import project.instagram.entity.Package;
import project.instagram.entity.Request;
import project.instagram.entity.RunningSummary;
import project.instagram.entity.Staff;
import project.instagram.entity.StatusOfRequest;
import project.instagram.entity.TransactionPackage;
import project.instagram.entity.TypeOfPackage;
import project.instagram.entity.TypeOfRequest;
import project.instagram.repository.ClientRepository;
import project.instagram.repository.PackageRepository;
import project.instagram.repository.RequestRepository;
import project.instagram.repository.RunningSummaryRepository;
import project.instagram.repository.StaffRepository;
import project.instagram.repository.TransactionPackageRepository;
import project.instagram.repository.TypeOfPackageRepository;
import project.instagram.request.RequestFormRequest;
import project.instagram.response.ClientResponse;
import project.instagram.response.DetailsTransactionPackageResponse;
import project.instagram.response.HashtagRunningHistoryResponse;
import project.instagram.response.MessageResponse;
import project.instagram.response.PackageResponse;
import project.instagram.response.PagedResponse;
import project.instagram.response.RemainingQuantityResponse;
import project.instagram.response.RequestResponse;
import project.instagram.response.RunningSummaryResponse;
import project.instagram.response.StaffResponse;
import project.instagram.response.TransactionPackageResponse;
import project.instagram.schedule.job.HashtagClientManagementJob;
import project.instagram.schedule.job.Job;
import project.instagram.security.SecurityAuditorAware;
import project.instagram.service.ClientService;
import project.instagram.socket.config.Message;
import project.instagram.utils.DateTimeZoneUtils;

@Service
public class ClientServiceImpl implements ClientService {

	@Autowired
	private SecurityAuditorAware securityAuditorAware;

	@Autowired
	private DateTimeZoneUtils dateTimeZoneUtils;

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	private TransactionPackageRepository transactionPackageRepository;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private RunningSummaryRepository runningSummaryRepository;

	@Autowired
	private PackageRepository packageRepository;

	@Autowired
	private TypeOfPackageRepository typeOfPackageRepository;

	@Autowired
	private RequestRepository requestRepository;

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private StaffRepository staffRepository;

	TransactionPackageResponse checkExistsPackageOfClient(Optional<TransactionPackage> transactionPackage) {
		if (transactionPackage.isEmpty()) {
			return null;
		}
		TransactionPackageResponse transactionPackageResponse = mapper.map(transactionPackage.get(),
				TransactionPackageResponse.class);

		Package packageOfClient = transactionPackage.get().getParentPackage();

		PackageResponse packageResponse = mapper.map(packageOfClient, PackageResponse.class);

		transactionPackageResponse.setValidPackage(packageResponse);

		return transactionPackageResponse;
	}

	void updateMessageResponse(MessageResponse messageResponse, Object data, String message, HttpStatus httpStatus) {
		messageResponse.setMessage(message);
		messageResponse.setStatus(httpStatus.value());
		messageResponse.setData(data);
	}

	MessageResponse existsValidPackageOfClient(Date currentDate, Client client) {
		MessageResponse messageResponse = new MessageResponse();

		Optional<TransactionPackage> transactionPackage = transactionPackageRepository
				.findByExpiredDateGreaterThanEqualAndClientAndChildPackage(PackageConstants.PACKAGE_TYPE,
						client.getId().toString(), currentDate);

		if (checkExistsPackageOfClient(transactionPackage) == null) {
			return messageResponse;
		}

		TransactionPackageResponse transactionPackageResponse = checkExistsPackageOfClient(transactionPackage);

		updateMessageResponse(messageResponse, transactionPackageResponse,
				PackageConstants.YOUR_CURRENT_PACKAGE_IS_VALID, HttpStatus.OK);

		return messageResponse;
	}

	private DetailsTransactionPackageResponse createDetailsTransactionPackageResponse(
			Package packageOfTransactionPackage, Optional<RunningSummary> runningSummary, String transactionPackageId) {

		TypeOfPackage typeOfPackage = typeOfPackageRepository
				.getById(packageOfTransactionPackage.getTypeOfPackage().getId());

		DetailsTransactionPackageResponse detailsTransactionPackageResponse = new DetailsTransactionPackageResponse();
		RunningSummaryResponse runningSummaryResponse = new RunningSummaryResponse();
		RemainingQuantityResponse remainingQuantityResponse = new RemainingQuantityResponse();

		if (runningSummary.isEmpty()) {
			remainingQuantityResponse.setRemainingQuantityCrawlHashtag(packageOfTransactionPackage.getCrawlQuantity());
			remainingQuantityResponse
					.setRemainingQuantitySearchHashtag(packageOfTransactionPackage.getSearchQuantity());

			detailsTransactionPackageResponse.setRemainingQuantityResponse(remainingQuantityResponse);
			detailsTransactionPackageResponse.setRunningSummaryResponse(runningSummaryResponse);
			detailsTransactionPackageResponse.setTransactionPackageId(transactionPackageId);

			return detailsTransactionPackageResponse;
		}

		runningSummaryResponse = mapper.map(runningSummary.get(), RunningSummaryResponse.class);
		if (PackageConstants.PACKAGE_TYPE.equals(typeOfPackage.getName())) {
			remainingQuantityResponse
					.setRemainingQuantityCrawlHashtag((byte) (packageOfTransactionPackage.getCrawlQuantity()
							- runningSummary.get().getCrawledPackageQuantity()));
			remainingQuantityResponse
					.setRemainingQuantitySearchHashtag((byte) (packageOfTransactionPackage.getSearchQuantity()
							- runningSummary.get().getSearchedPackageQuantity()));
		} else {
			remainingQuantityResponse
					.setRemainingQuantityCrawlHashtag((byte) (packageOfTransactionPackage.getCrawlQuantity()
							- runningSummary.get().getCrawledExtraPackageQuantity()));

			remainingQuantityResponse
					.setRemainingQuantitySearchHashtag((byte) (packageOfTransactionPackage.getSearchQuantity()
							- runningSummary.get().getSearchedExtraPackageQuantity()));
		}

		runningSummaryResponse.setId(runningSummary.get().getId().toString());
		detailsTransactionPackageResponse.setRemainingQuantityResponse(remainingQuantityResponse);
		detailsTransactionPackageResponse.setRunningSummaryResponse(runningSummaryResponse);
		detailsTransactionPackageResponse.setTransactionPackageId(transactionPackageId);

		return detailsTransactionPackageResponse;
	}

	private Request createRequest(Client client, RequestFormRequest requestFormRequest) {
		Request request = new Request();
		Date currentDate = dateTimeZoneUtils.getDateTimeZoneGMT();
		StringBuilder newRequestId = new StringBuilder(
				currentDate.toString().replace(" ", "") + "_" + client.getId().toString());

		request.setId(newRequestId.toString());
		request.setClientRequest(client);
		request.setReason(requestFormRequest.getReason());
		request.setTitle(requestFormRequest.getTitle());
		StatusOfRequest statusOfRequest = new StatusOfRequest(StatusRequestName.PENDING);
		request.setStatusOfRequest(statusOfRequest);
		TypeOfRequest typeOfRequest = new TypeOfRequest(RequestName.HANDLE_REQUEST);
		request.setTypeOfRequest(typeOfRequest);

		request = requestRepository.save(request);

		return request;
	}

	private void addHandlingRquestToRedis(Request request) {
		redisTemplate.opsForList().leftPush(JobConstants.PENDING_REQUESTS, request.getId());
	}

	@Override
	public ResponseEntity<MessageResponse> getValidPackage() {
		MessageResponse messageResponse = new MessageResponse();
		Client client = clientRepository.findByEmail(securityAuditorAware.getCurrentAuditor().get()).get();
		Date currentDate = dateTimeZoneUtils.getDateTimeZoneGMT();

		if (existsValidPackageOfClient(currentDate, client) == null) {
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());
			messageResponse.setMessage(AccountConstants.CLIENT_IS_NOT_A_MEMBER);

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}
		messageResponse = existsValidPackageOfClient(currentDate, client);

		return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
	}

	@Override
	public ResponseEntity<MessageResponse> getValidExtraPackages() {
		MessageResponse messageResponse = new MessageResponse();

		Client client = clientRepository.findByEmail(securityAuditorAware.getCurrentAuditor().get()).get();
		Date currentDate = dateTimeZoneUtils.getDateTimeZoneGMT();

		List<TransactionPackage> transactionPackages = transactionPackageRepository
				.findAllValidExtraTransactionPackages(PackageConstants.EXTRA_PACKAGE_TYPE, client.getId().toString(),
						currentDate);

		if (transactionPackages.size() == 0) {
			messageResponse.setStatus(HttpStatus.OK.value());
			messageResponse.setMessage(PackageConstants.CLIENT_HAVE_NOT_VALID_EXTRA_PACKAGE);

			return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
		}

		List<TransactionPackageResponse> transactionPackageResponses = new ArrayList<TransactionPackageResponse>();

		for (TransactionPackage transactionPackage : transactionPackages) {
			TransactionPackageResponse transactionPackageResponse = mapper.map(transactionPackage,
					TransactionPackageResponse.class);
			PackageResponse packageResponse = mapper.map(transactionPackage.getParentPackage(), PackageResponse.class);
			transactionPackageResponse.setValidPackage(packageResponse);
			transactionPackageResponses.add(transactionPackageResponse);
		}
		messageResponse.setStatus(HttpStatus.OK.value());
		messageResponse.setMessage(PackageConstants.GET_SUCCESS);
		messageResponse.setData(transactionPackageResponses);

		return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
	}

	@Override
	public ResponseEntity<MessageResponse> getDetailsValidTransactionPackage(String transactionPackageId) {
		MessageResponse messageResponse = new MessageResponse();

		Optional<TransactionPackage> transactionPackage = transactionPackageRepository
				.findById(Integer.valueOf(transactionPackageId));

		if (transactionPackage.isEmpty()) {
			messageResponse.setMessage(TransactionConstants.TRANSACTION_PACKAGE_NOT_EXISTS);
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}

		Date currentDate = dateTimeZoneUtils.getDateTimeZoneGMT();
		Client client = clientRepository.findByEmail(securityAuditorAware.getCurrentAuditor().get()).get();

		Optional<RunningSummary> runningSummary = runningSummaryRepository.findDetailsTransactionPackage(currentDate,
				client, transactionPackage.get());

		Package packageOfTransactionPackage = packageRepository
				.getById(transactionPackage.get().getParentPackage().getId());

		if (runningSummary.isEmpty()) {
			DetailsTransactionPackageResponse detailsTransactionPackageResponse = createDetailsTransactionPackageResponse(
					packageOfTransactionPackage, runningSummary, transactionPackageId);

			messageResponse.setMessage(TransactionConstants.RUNNING_SUMMARY_NOT_EXISTS);
			messageResponse.setStatus(HttpStatus.OK.value());
			messageResponse.setData(detailsTransactionPackageResponse);

			return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
		}

		DetailsTransactionPackageResponse detailsTransactionPackageResponse = createDetailsTransactionPackageResponse(
				packageOfTransactionPackage, runningSummary, transactionPackageId);

		messageResponse.setMessage(TransactionConstants.RUNNING_SUMMARY_EXISTS);
		messageResponse.setStatus(HttpStatus.OK.value());
		messageResponse.setData(detailsTransactionPackageResponse);

		return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
	}

	@SuppressWarnings("unused")
	private void sendNotificationForClient(Job job, HashtagClientManagementJob hashtagClientManagementJob,
			Date currentDate, String hashtagRunningHistoryId) {

		StringBuilder messageToClient = new StringBuilder(
				job.getTypeJob() + "ED " + job.getHashtag() + " " + job.getTypeJob());

		Message message = new Message();
		message.setMessage(messageToClient.toString());
		message.setTitle(job.getTypeJob());

//		ParameterCrawlDataPageResponse parameterCrawlDataPageResponse = new ParameterCrawlDataPageResponse();
//		parameterCrawlDataPageResponse.setDate(currentDate.toString());
//		parameterCrawlDataPageResponse.setPage(1);
//		parameterCrawlDataPageResponse.setSize(15);
//		parameterCrawlDataPageResponse.setHashtag(job.getHashtag());

//		StringBuilder urlForward = new StringBuilder(URL + "api/client/data-crawls");
//		parameterCrawlDataPageResponse.setUrl(urlForward.toString());

		HashtagRunningHistoryResponse hashtagRunningHistoryResponse = new HashtagRunningHistoryResponse();
		hashtagRunningHistoryResponse.setHashtag(job.getHashtag());
		hashtagRunningHistoryResponse.setId(hashtagRunningHistoryId);

		message.setObject(hashtagRunningHistoryResponse);

		UUID clientUUID = UUID.fromString(hashtagClientManagementJob.getClientId());
		Client client = clientRepository.findById(clientUUID).get();

		String emailClient = client.getEmail();
		System.out.println(emailClient);
		simpMessagingTemplate.convertAndSendToUser(emailClient, "/private", message);

	}

	private void sendNotificationToStaff() {
		Message message = new Message();
		message.setMessage("There is a new request pending");
		message.setTitle("New request pending");
		simpMessagingTemplate.convertAndSend("/newrequest/public", message);
		System.out.println("concacccccccccccccccccccc");
	}

	@Override
	public ResponseEntity<MessageResponse> createRequest(RequestFormRequest requestFormRequest) {
		MessageResponse messageResponse = new MessageResponse();
		Client client = clientRepository.findByEmail(securityAuditorAware.getCurrentAuditor().get()).get();
		Request request = createRequest(client, requestFormRequest);

		if (request == null) {
			messageResponse.setMessage(RequestConstants.CREATED_REQUEST_FAILED);
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}

		addHandlingRquestToRedis(request);
		sendNotificationToStaff();

		messageResponse.setMessage(RequestConstants.REQUEST_SUCCESS);
		messageResponse.setStatus(HttpStatus.OK.value());

		return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
	}

	@Override
	public PagedResponse<TransactionPackageResponse> findAllTransactionPackage(int page, int size) {

		Pageable pageable = PageRequest.of(page, size);

		Client client = clientRepository.findByEmail(securityAuditorAware.getCurrentAuditor().get()).get();

		Page<TransactionPackage> transactionPackages = transactionPackageRepository
				.findAllByClientOrderByIssuedeDateDesc(pageable, client);

		List<TransactionPackageResponse> transactionPackageResponses = new ArrayList<TransactionPackageResponse>(
				transactionPackages.getContent().size());

		for (TransactionPackage transactionPackage : transactionPackages.getContent()) {
			transactionPackageResponses.add(createTransactionPackageResponse(transactionPackage));
		}

		return new PagedResponse<>(transactionPackageResponses, transactionPackages.getNumber(),
				transactionPackages.getSize(), transactionPackages.getTotalElements(),
				transactionPackages.getTotalPages(), transactionPackages.isLast());
	}

	private TransactionPackageResponse createTransactionPackageResponse(TransactionPackage transactionPackage) {
		TransactionPackageResponse transactionPackageResponse = new TransactionPackageResponse();
		transactionPackageResponse.setId(transactionPackage.getId());
		transactionPackageResponse.setExpiredDate(transactionPackage.getExpiredDate());
		transactionPackageResponse.setIssuedeDate(transactionPackage.getIssuedeDate());

		Package package1 = packageRepository.findById(transactionPackage.getParentPackage().getId()).get();

		PackageResponse packageResponse = new PackageResponse();
		packageResponse.setId(package1.getId().toString());
		packageResponse.setPrice(package1.getPrice());
		packageResponse.setName(package1.getName());

		transactionPackageResponse.setValidPackage(packageResponse);

		return transactionPackageResponse;
	}

	private RequestResponse createRequestResponse(Request request) {
		RequestResponse requestResponse = mapper.map(request, RequestResponse.class);
		requestResponse.setRequestId(request.getId().toString());
		requestResponse.setStatusOfRequest(request.getStatusOfRequest().getName().toString());
		requestResponse.setTypeOfRequest(request.getTypeOfRequest().getName().toString());

		if (request.getStaff() == null) {
			requestResponse.setUpdatedBy(null);
		} else {
			Staff updatedByStaff = staffRepository.getById(request.getStaff().getId());
			requestResponse
					.setUpdatedBy(new StaffResponse(updatedByStaff.getId().toString(), updatedByStaff.getEmail()));
		}

		Client requestCreator = clientRepository.getById(request.getClientRequest().getId());
		requestResponse
				.setRequestCreator(new ClientResponse(requestCreator.getId().toString(), requestCreator.getEmail()));

		return requestResponse;
	}

	private Calendar calculateCalendarOfPackage(Optional<Package> existsPackage) {
		Calendar calendar = null;
		if (existsPackage.get().getNumberOfMonths() == 0) {
			return calendar;
		}
		calendar = Calendar.getInstance();
		Date currentDate = dateTimeZoneUtils.getDateTimeZoneGMT();
		calendar.setTime(currentDate);
		calendar.add(Calendar.DATE, existsPackage.get().getNumberOfMonths() * 30);

		return calendar;
	}

	private TransactionPackage createTransactionPackage(Optional<Package> existsPackage, Client client) {
		TransactionPackage newTransactionPackage = new TransactionPackage();
		
		Date currentDate = dateTimeZoneUtils.getDateTimeZoneGMT();

		newTransactionPackage.setClient(client);
		newTransactionPackage.setParentPackage(existsPackage.get());
		newTransactionPackage.setIssuedeDate(currentDate);

		Calendar calendar = calculateCalendarOfPackage(existsPackage);
		if (calculateCalendarOfPackage(existsPackage) == null) {
			newTransactionPackage.setExpiredDate(null);
		} else {
			newTransactionPackage.setExpiredDate(calendar.getTime());
		}
		newTransactionPackage = transactionPackageRepository.save(newTransactionPackage);

		return newTransactionPackage;
	}

	@Override
	public PagedResponse<RequestResponse> findAllNotPendingRequests(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Client client = clientRepository.findByEmail(securityAuditorAware.getCurrentAuditor().get()).get();
		StatusOfRequest statusOfRequest = new StatusOfRequest(StatusRequestName.PENDING);
		Page<Request> requests = requestRepository
				.findAllByClientRequestAndStatusOfRequestNotOrderByCreatedDateDesc(client, statusOfRequest, pageable);

		List<RequestResponse> requestResponses = new ArrayList<RequestResponse>(requests.getContent().size());

		for (Request request : requests.getContent()) {
			requestResponses.add(createRequestResponse(request));
		}

		return new PagedResponse<>(requestResponses, requests.getNumber(), requests.getSize(),
				requests.getTotalElements(), requests.getTotalPages(), requests.isLast());
	}

	@Override
	public PagedResponse<RequestResponse> findAllPendingRequests(int page, int size) {

		Client client = clientRepository.findByEmail(securityAuditorAware.getCurrentAuditor().get()).get();
		Pageable pageable = PageRequest.of(page, size);
		StatusOfRequest statusOfRequest = new StatusOfRequest(StatusRequestName.PENDING);
		Page<Request> requests = requestRepository
				.findAllByClientRequestAndStatusOfRequestOrderByCreatedDateDesc(client, statusOfRequest, pageable);

		List<RequestResponse> requestResponses = new ArrayList<RequestResponse>(requests.getContent().size());

		for (Request request : requests.getContent()) {
			requestResponses.add(createRequestResponse(request));
		}

		return new PagedResponse<>(requestResponses, requests.getNumber(), requests.getSize(),
				requests.getTotalElements(), requests.getTotalPages(), requests.isLast());
	}

	@Override
	public ResponseEntity<MessageResponse> verifiRequest(String token) {

		MessageResponse messageResponse = new MessageResponse();

		String clientId = token.split("_")[0];
		String packageId = token.split("_")[1];
		String requestId = token.split("_")[2] + "_" + token.split("_")[3];

		UUID clientUUID = UUID.fromString(clientId);
		Optional<Client> client = clientRepository.findById(clientUUID);

		TypeOfPackage ofPackage = typeOfPackageRepository.findByName(PackageConstants.EXTRA_PACKAGE_TYPE).get();
		Optional<Package> extraPackage = packageRepository.findPackageByIdAndTypeOfPackage(UUID.fromString(packageId),
				ofPackage);

		Optional<Request> request = requestRepository.findById(requestId);

		if (client.isEmpty()) {
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());
			messageResponse.setMessage(UserConstants.ACCOUNT_NOT_EXISTS);

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}

		if (extraPackage.isEmpty()) {
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());
			messageResponse.setMessage(PackageConstants.PACKAGE_NOT_EXISTS);

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}

		if (request.isEmpty()) {
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());
			messageResponse.setMessage(RequestConstants.REQUEST_NOT_EXISTS);

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}

		TransactionPackage newTransactionPackage = createTransactionPackage(extraPackage, client.get());

		if (newTransactionPackage == null) {
			messageResponse.setMessage(PackageConstants.PURCHASED_THE_EXTRA_PACKAGE_FAILED);
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}

		messageResponse.setMessage(PackageConstants.PURCHASED_THE_EXTRA_PACKAGE_SUCCESSFULLY);
		messageResponse.setStatus(HttpStatus.OK.value());

		return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
	}

}
