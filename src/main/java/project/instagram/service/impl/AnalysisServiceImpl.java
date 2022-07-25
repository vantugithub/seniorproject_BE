package project.instagram.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import project.instagram.common.enums.constants.HashtagConstants;
import project.instagram.common.enums.constants.RequestConstants;
import project.instagram.entity.Analysis;
import project.instagram.entity.Client;
import project.instagram.entity.Hashtag;
import project.instagram.entity.HashtagRunningHistory;
import project.instagram.repository.AnalysisRepository;
import project.instagram.repository.ClientRepository;
import project.instagram.repository.HashtagRepository;
import project.instagram.repository.HashtagRunningHistoryRepository;
import project.instagram.response.AnalysisResponse;
import project.instagram.response.CounterResponse;
import project.instagram.response.MessageResponse;
import project.instagram.security.SecurityAuditorAware;
import project.instagram.service.AnalysisService;
import project.instagram.utils.DateTimeZoneUtils;

@Service
public class AnalysisServiceImpl implements AnalysisService {

	@Autowired
	private SecurityAuditorAware securityAuditorAware;

	@Autowired
	private HashtagRunningHistoryRepository hashtagRunningHistoryRepository;

	@Autowired
	private AnalysisRepository analysisRepository;

	@Autowired
	private DateTimeZoneUtils dateTimeZoneUtils;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private HashtagRepository hashtagRepository;

	@Override
	public ResponseEntity<MessageResponse> getAnalysisByDateAndClient(String hashtagRunningHistoryId) {

		MessageResponse messageResponse = new MessageResponse();

		Optional<HashtagRunningHistory> hashtagRunningHistory = hashtagRunningHistoryRepository
				.findById(hashtagRunningHistoryId);

		if (hashtagRunningHistory.isEmpty()) {
			messageResponse.setMessage(RequestConstants.HASHTAG_RUNNING_HISTORY_ID_IS_NOT_EXISTS);
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}

		Optional<Client> client = clientRepository.findByEmail(securityAuditorAware.getCurrentAuditor().get());

		if (hashtagRunningHistory.get().getClient().getId().toString().equals(client.get().getId().toString())) {
			Pageable pageable = PageRequest.of(0, 30);

			Hashtag hashtag = hashtagRepository.getById(hashtagRunningHistory.get().getHashtag().getName());

			Page<Analysis> analysis = analysisRepository.findAllByHashtagAndDateOfAnalysisOrderByIdDesc(hashtag,
					hashtagRunningHistory.get().getRunningTime().toString().split(" ")[0], pageable);

			AnalysisResponse analysisResponse = new AnalysisResponse();

			List<CounterResponse> counterResponses = new ArrayList<CounterResponse>();

			for (Analysis ana : analysis.getContent()) {
				counterResponses.add(createCounterResponse(ana));
			}

			analysisResponse.setCounterResponses(counterResponses);
			analysisResponse.setDateOfAnalysis(hashtagRunningHistory.get().getRunningTime().toString());
			analysisResponse.setHashtag(hashtag.getName());

			messageResponse.setMessage(RequestConstants.REQUEST_SUCCESS);
			messageResponse.setStatus(HttpStatus.OK.value());
			messageResponse.setData(analysisResponse);

			return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
		}

		messageResponse.setMessage(RequestConstants.REQUEST_FAILED);
		messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
	}

	private CounterResponse createCounterResponse(Analysis ana) {
		CounterResponse counterResponse = new CounterResponse();
		counterResponse.setCount(ana.getCount());
		counterResponse.setHashtag(ana.getNameHashtag());

		return counterResponse;
	}

	@Override
	public ResponseEntity<MessageResponse> getAnalysisHashtagByPeriodOfTime(String hashtagStr, String startDateStr,
			String endDateStr) {

		MessageResponse messageResponse = new MessageResponse();
		Optional<Hashtag> hashtag = hashtagRepository.findById(hashtagStr);

		if (hashtag.isEmpty()) {
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());
			messageResponse.setMessage(HashtagConstants.HASHTAG_NOT_EXISTS);

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}

		Date startDate = dateTimeZoneUtils.formatDateTime(startDateStr);
		Date endDate = dateTimeZoneUtils.formatDateTime(endDateStr);
		
		Optional<Client> client = clientRepository.findByEmail(securityAuditorAware.getCurrentAuditor().get());

		List<HashtagRunningHistory> hashtagRunningHistories = hashtagRunningHistoryRepository
				.findAllByHashtagAndPriodOfTime(hashtagStr, startDate, endDate, client.get());

		if (hashtagRunningHistories.size() == 0) {
			messageResponse.setMessage(RequestConstants.YOUR_REQUEST_HAS_NO_RETURN_VALUE);
			messageResponse.setStatus(HttpStatus.OK.value());

			return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
		}

		Pageable pageable = PageRequest.of(0, 30);

		List<AnalysisResponse> analysisResponses = new ArrayList<AnalysisResponse>();

		for (HashtagRunningHistory hashtagRunningHistory : hashtagRunningHistories) {

			Page<Analysis> analysis = analysisRepository.findAllByHashtagAndDateOfAnalysisOrderByIdDesc(hashtag.get(),
					hashtagRunningHistory.getRunningTime().toString().split(" ")[0], pageable);

			List<CounterResponse> counterResponses = new ArrayList<CounterResponse>();

			for (Analysis ana : analysis.getContent()) {
				counterResponses.add(createCounterResponse(ana));
			}

			AnalysisResponse analysisResponse = new AnalysisResponse();

			analysisResponse.setCounterResponses(counterResponses);
			analysisResponse.setDateOfAnalysis(hashtagRunningHistory.getRunningTime().toString());
			analysisResponse.setHashtag(hashtag.get().getName());

			analysisResponses.add(analysisResponse);
		}

		messageResponse.setData(analysisResponses);
		messageResponse.setMessage(RequestConstants.REQUEST_SUCCESS);
		messageResponse.setStatus(HttpStatus.OK.value());

		return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
	}

}
