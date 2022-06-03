package project.instagram.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import project.instagram.common.enums.StatusRequestName;
import project.instagram.common.enums.constants.JobConstants;
import project.instagram.common.enums.constants.RequestConstants;
import project.instagram.entity.Client;
import project.instagram.entity.Request;
import project.instagram.entity.Staff;
import project.instagram.entity.StatusOfRequest;
import project.instagram.repository.ClientRepository;
import project.instagram.repository.RequestRepository;
import project.instagram.repository.StaffRepository;
import project.instagram.response.ClientResponse;
import project.instagram.response.MessageResponse;
import project.instagram.response.PagedResponse;
import project.instagram.response.RequestResponse;
import project.instagram.response.StaffResponse;
import project.instagram.service.StaffService;

@Service
public class StaffServiceImpl implements StaffService {

	@Autowired
	private RequestRepository requestRepository;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private StaffRepository staffRepository;

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
	public ResponseEntity<MessageResponse> updateRequest(String requestId) {
		redisTemplate.delete(requestId);
		return null;
	}

	@Override
	public ResponseEntity<RequestResponse> getPendingRequest() {

		if (redisTemplate.hasKey(JobConstants.PENDING_REQUESTS)) {
			Object consultResult = redisTemplate.opsForList().leftPop(JobConstants.PENDING_REQUESTS);	
			
			redisTemplate.opsForList().leftPush(consultResult.toString(), consultResult.toString());
			
			redisTemplate.expire(consultResult.toString(), 60, TimeUnit.SECONDS);
		}
		
		return null;
	}

}
