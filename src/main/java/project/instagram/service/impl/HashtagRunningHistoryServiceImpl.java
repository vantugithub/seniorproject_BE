package project.instagram.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import project.instagram.common.enums.constants.RequestConstants;
import project.instagram.entity.Client;
import project.instagram.entity.Hashtag;
import project.instagram.entity.HashtagRunningHistory;
import project.instagram.repository.ClientRepository;
import project.instagram.repository.HashtagRepository;
import project.instagram.repository.HashtagRunningHistoryRepository;
import project.instagram.response.HashtagRunningHistoryResponse;
import project.instagram.response.MessageResponse;
import project.instagram.response.PagedResponse;
import project.instagram.security.SecurityAuditorAware;
import project.instagram.service.HashtagRunningHistoryService;
import project.instagram.utils.DateTimeZoneUtils;

@Service
public class HashtagRunningHistoryServiceImpl implements HashtagRunningHistoryService {

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private DateTimeZoneUtils dateTimeZoneUtils;

	@Autowired
	private SecurityAuditorAware securityAuditorAware;

	@Autowired
	private HashtagRepository hashtagRepository;

	@Autowired
	private HashtagRunningHistoryRepository hashtagRunningHistoryRepository;

	@Autowired
	private ClientRepository clientRepository;

	@Override
	public PagedResponse<HashtagRunningHistoryResponse> findAllByClientByRunningTimeDesc(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Client client = clientRepository.findByEmail(securityAuditorAware.getCurrentAuditor().get()).get();

		Page<HashtagRunningHistory> hashtagRunningHistories = hashtagRunningHistoryRepository
				.findAllByClientOrderByRunningTimeDesc(client, pageable);

		List<HashtagRunningHistoryResponse> hashtagRunningHistoryResponses = new ArrayList<HashtagRunningHistoryResponse>(
				hashtagRunningHistories.getContent().size());

		for (HashtagRunningHistory hashtagRunningHistory : hashtagRunningHistories.getContent()) {
			hashtagRunningHistoryResponses.add(createHashtagRunningHistoryResponse(hashtagRunningHistory));
		}

		return new PagedResponse<>(hashtagRunningHistoryResponses, hashtagRunningHistories.getNumber(),
				hashtagRunningHistories.getSize(), hashtagRunningHistories.getTotalElements(),
				hashtagRunningHistories.getTotalPages(), hashtagRunningHistories.isLast());
	}

	private HashtagRunningHistoryResponse createHashtagRunningHistoryResponse(
			HashtagRunningHistory hashtagRunningHistory) {
		HashtagRunningHistoryResponse hashtagRunningHistoryResponse = mapper.map(hashtagRunningHistory,
				HashtagRunningHistoryResponse.class);

		hashtagRunningHistoryResponse.setClientId(hashtagRunningHistory.getClient().getId().toString());
		hashtagRunningHistoryResponse.setHashtag(hashtagRunningHistory.getHashtag().getName());
		hashtagRunningHistoryResponse.setTransactionPackageId(hashtagRunningHistory.getTransactionPackage().getId());

		return hashtagRunningHistoryResponse;
	}

	@Override
	public PagedResponse<HashtagRunningHistoryResponse> findAllByClientAndHashtag(int page, int size,
			String hashtagStr) {
		Pageable pageable = PageRequest.of(page, size);
		Client client = clientRepository.findByEmail(securityAuditorAware.getCurrentAuditor().get()).get();

		Hashtag hashtag = hashtagRepository.findById(hashtagStr).get();

		Page<HashtagRunningHistory> hashtagRunningHistories = hashtagRunningHistoryRepository
				.findAllByClientAndHashtagOrderByRunningTimeDesc(client, hashtag, pageable);

		List<HashtagRunningHistoryResponse> hashtagRunningHistoryResponses = new ArrayList<HashtagRunningHistoryResponse>(
				hashtagRunningHistories.getContent().size());

		for (HashtagRunningHistory hashtagRunningHistory : hashtagRunningHistories.getContent()) {
			hashtagRunningHistoryResponses.add(createHashtagRunningHistoryResponse(hashtagRunningHistory));
		}

		return new PagedResponse<>(hashtagRunningHistoryResponses, hashtagRunningHistories.getNumber(),
				hashtagRunningHistories.getSize(), hashtagRunningHistories.getTotalElements(),
				hashtagRunningHistories.getTotalPages(), hashtagRunningHistories.isLast());
	}

	private HashMap<String, HashMap<String, Integer>> createHashMap() {

		HashMap<String, HashMap<String, Integer>> counters = new HashMap<String, HashMap<String, Integer>>();

		for (int i = 1; i <= 12; i++) {
			HashMap<String, Integer> counter = new HashMap<String, Integer>();
			if (i >= 10) {
				counters.put(String.valueOf(i), counter);
			} else {
				counters.put("0" + String.valueOf(i), counter);
			}
		}

		return counters;
	}

	private HashMap<String, HashMap<String, Integer>> createHashMapForHashtags(
			List<HashtagRunningHistory> runningHistories) {

		HashMap<String, HashMap<String, Integer>> counters = createHashMap();

		for (HashtagRunningHistory hashtagRunningHistory : runningHistories) {

			String monthStr = hashtagRunningHistory.getRunningTime().toString().split("-")[1];

			HashMap<String, Integer> hashMapHashtag = counters.get(monthStr);
			if (hashMapHashtag.containsKey(hashtagRunningHistory.getHashtag().getName())) {
				hashMapHashtag.put(hashtagRunningHistory.getHashtag().getName(),
						hashMapHashtag.get(hashtagRunningHistory.getHashtag().getName()) + 1);
			} else {
				hashMapHashtag.put(hashtagRunningHistory.getHashtag().getName(), 1);
			}
		}

		return counters;
	}

	@Override
	public ResponseEntity<MessageResponse> getAnalysisHashtags(String yearStr) {

		String startYearStr = yearStr;
		String startDateTimeStr = startYearStr + "-01-01";
		String endDateTimeStr = String.valueOf(Integer.parseInt(startYearStr) + (Integer) 1) + "-01-01";

		Date startDate = dateTimeZoneUtils.formatDateTime(startDateTimeStr);
		Date endDate = dateTimeZoneUtils.formatDateTime(endDateTimeStr);

//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(startDate);
//		calendar.add(Calendar.MONTH, 1);

//		Date endDate = calendar.getTime();

		List<HashtagRunningHistory> runningHistories = hashtagRunningHistoryRepository
				.getAllHashtagsByPeriodOfTime(startDate, endDate);
		
		MessageResponse messageResponse = new MessageResponse();
		messageResponse.setStatus(HttpStatus.OK.value());
		messageResponse.setData(createHashMapForHashtags(runningHistories));
		messageResponse.setMessage(RequestConstants.REQUEST_SUCCESS);
		
		return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
	}

}
