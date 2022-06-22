package project.instagram.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import project.instagram.common.enums.StatusRequestName;
import project.instagram.common.enums.constants.JobConstants;
import project.instagram.common.enums.constants.PackageConstants;
import project.instagram.common.enums.constants.RequestConstants;
import project.instagram.entity.Client;
import project.instagram.entity.Request;
import project.instagram.entity.Staff;
import project.instagram.entity.StatusOfRequest;
import project.instagram.entity.TypeOfPackage;
import project.instagram.repository.ClientRepository;
import project.instagram.repository.PackageRepository;
import project.instagram.repository.RequestRepository;
import project.instagram.repository.StaffRepository;
import project.instagram.repository.TypeOfPackageRepository;
import project.instagram.response.ClientResponse;
import project.instagram.response.MessageResponse;
import project.instagram.response.PagedResponse;
import project.instagram.response.RequestResponse;
import project.instagram.response.StaffResponse;
import project.instagram.response.UpdateRequestProcessing;
import project.instagram.service.StaffService;

@Service
public class StaffServiceImpl implements StaffService {

	@Value("${path.url}")
	private String URL;

	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	private RequestRepository requestRepository;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private StaffRepository staffRepository;

	@Autowired
	private PackageRepository packageRepository;

	@Autowired
	private TypeOfPackageRepository typeOfPackageRepository;

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private ModelMapper mapper;

	@Override
	public PagedResponse<RequestResponse> findAllNotPendingRequests(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		StatusOfRequest statusOfRequest = new StatusOfRequest(StatusRequestName.PENDING);
		Page<Request> requests = requestRepository.findAllByStatusOfRequestNotOrderByCreatedDateDesc(statusOfRequest,
				pageable);

		List<RequestResponse> requestResponses = new ArrayList<RequestResponse>(requests.getContent().size());

		for (Request request : requests.getContent()) {
			requestResponses.add(createRequestResponse(request));
		}

		return new PagedResponse<>(requestResponses, requests.getNumber(), requests.getSize(),
				requests.getTotalElements(), requests.getTotalPages(), requests.isLast());
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

	@Override
	public PagedResponse<RequestResponse> findAllPendingRequests(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		StatusOfRequest statusOfRequest = new StatusOfRequest(StatusRequestName.PENDING);
		Page<Request> requests = requestRepository.findAllByStatusOfRequestOrderByCreatedDateDesc(statusOfRequest,
				pageable);

		List<RequestResponse> requestResponses = new ArrayList<RequestResponse>(requests.getContent().size());

		for (Request request : requests.getContent()) {
			requestResponses.add(createRequestResponse(request));
		}

		return new PagedResponse<>(requestResponses, requests.getNumber(), requests.getSize(),
				requests.getTotalElements(), requests.getTotalPages(), requests.isLast());
	}

	@Override
	public ResponseEntity<MessageResponse> getDetailsRequest(String requestId) {
		MessageResponse messageResponse = new MessageResponse();
		Optional<Request> request = requestRepository.findById(requestId);

		if (request.isEmpty()) {
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());
			messageResponse.setMessage(RequestConstants.REQUEST_NOT_EXISTS);

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}
		RequestResponse requestResponse = createRequestResponse(request.get());

		messageResponse.setStatus(HttpStatus.OK.value());
		messageResponse.setMessage(RequestConstants.GET_REQUEST_SUCCESSFULLY);
		messageResponse.setData(requestResponse);

		return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
	}

	@Override
	public ResponseEntity<MessageResponse> updateRequest(UpdateRequestProcessing updateRequestProcessing) {
		MessageResponse messageResponse = new MessageResponse();

		Optional<Client> client = clientRepository.findById(UUID.fromString(updateRequestProcessing.getClientId()));

		TypeOfPackage typeOfPackage = typeOfPackageRepository.findByName(PackageConstants.EXTRA_PACKAGE_TYPE).get();

		Optional<project.instagram.entity.Package> extraPackage = packageRepository
				.findPackageByNameAndTypeOfPackage(updateRequestProcessing.getExtraPackageName(), typeOfPackage);

		
		try {
			if (!RequestConstants.ACCEPT_REQUEST.equals(updateRequestProcessing.getStatusRequest())) {
				StringBuilder message = new StringBuilder("Wellcome ");
				StringBuilder linkVerification = new StringBuilder(RequestConstants.CONTENT_SORRY);
				message.append(client.get().getEmail());
				message.append("\n" + linkVerification);

				SimpleMailMessage messageEmail = new SimpleMailMessage();
				messageEmail.setTo(client.get().getEmail());
				messageEmail.setSubject("Rejected request");
				messageEmail.setText(message.toString());
				emailSender.send(messageEmail);

				messageResponse.setMessage(RequestConstants.UPDATE_REQUEST_SUCCESSFULLY);
				messageResponse.setStatus(HttpStatus.OK.value());
				updateRequestProcessing(updateRequestProcessing.getRequestId(),
						new StatusOfRequest(StatusRequestName.REJECTED));

				redisTemplate.delete(updateRequestProcessing.getRequestId());

				return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
			}
			
			if (extraPackage.isEmpty()) {
				messageResponse.setMessage(PackageConstants.PACKAGE_NOT_EXISTS);
				messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());

				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
			}

			StringBuilder linkVerification = new StringBuilder(URL + "api/auth/client/confirm-request?token=");
			StringBuilder token = new StringBuilder(client.get().getId().toString());

			linkVerification.append(token);
			linkVerification.append("_" + extraPackage.get().getId().toString());
			linkVerification.append("_" + updateRequestProcessing.getRequestId());

			StringBuilder message = new StringBuilder("Wellcome ");
			message.append(client.get().getEmail());
			message.append("\nYour link Verification is: ");
			message.append(linkVerification);
			message.append("\nPlease enter link on website to complete buy the extra package");

			SimpleMailMessage messageEmail = new SimpleMailMessage();
			messageEmail.setTo(client.get().getEmail());
			messageEmail.setSubject("Verification Link");
			messageEmail.setText(message.toString());
			emailSender.send(messageEmail);

			messageResponse.setMessage(RequestConstants.UPDATE_REQUEST_SUCCESSFULLY);
			messageResponse.setStatus(HttpStatus.OK.value());
			updateRequestProcessing(updateRequestProcessing.getRequestId(),
					new StatusOfRequest(StatusRequestName.ACCEPTED));
			
			redisTemplate.delete(updateRequestProcessing.getRequestId());
			return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
		} catch (Exception e) {
			messageResponse.setMessage(RequestConstants.REQUEST_FAILED);
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}

	}

	private void updateRequestProcessing(String requestId, StatusOfRequest statusOfRequest) {
		Request request = requestRepository.findById(requestId).get();
		request.setStatusOfRequest(statusOfRequest);

		requestRepository.save(request);
	}

	@Override
	public ResponseEntity<RequestResponse> getPendingRequest() {
		Object consultResult = null;
		RequestResponse requestResponse = new RequestResponse();
		if (redisTemplate.hasKey(JobConstants.PENDING_REQUESTS)) {
			consultResult = redisTemplate.opsForList().leftPop(JobConstants.PENDING_REQUESTS);

			redisTemplate.opsForList().leftPush(consultResult.toString(), consultResult.toString());

			redisTemplate.expire(consultResult.toString(), 120, TimeUnit.SECONDS);

			Request request = requestRepository.findById(consultResult.toString()).get();
			requestResponse = createRequestResponse(request);
			return ResponseEntity.status(HttpStatus.OK).body(requestResponse);
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(requestResponse);
	}

}
