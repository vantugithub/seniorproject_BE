package project.instagram.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import project.instagram.common.enums.constants.AppConstants;
import project.instagram.common.enums.constants.JobConstants;
import project.instagram.common.enums.constants.RecommendConstants;
import project.instagram.common.enums.constants.RequestConstants;
import project.instagram.entity.Analysis;
import project.instagram.entity.Client;
import project.instagram.entity.DataCrawl;
import project.instagram.entity.Hashtag;
import project.instagram.entity.HashtagRunningHistory;
import project.instagram.repository.AnalysisRepository;
import project.instagram.repository.ClientRepository;
import project.instagram.repository.DataCrawlRepository;
import project.instagram.repository.HashtagRepository;
import project.instagram.repository.HashtagRunningHistoryRepository;
import project.instagram.response.MessageResponse;
import project.instagram.security.SecurityAuditorAware;
import project.instagram.service.RecommendService;

@Service
public class RecommendServiceImpl implements RecommendService {

	@Autowired
	private HashtagRunningHistoryRepository hashtagRunningHistoryRepository;

	@Autowired
	private SecurityAuditorAware securityAuditorAware;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private AnalysisRepository analysisRepository;

	@Autowired
	private HashtagRepository hashtagRepository;

	@Autowired
	private DataCrawlRepository dataCrawlRepository;

	private Date getRunningTime(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR, 16);

		return calendar.getTime();
	}

	@Override
	public ResponseEntity<MessageResponse> getLongestHashtags(String hashtagRunningHistoryId) {
		Optional<HashtagRunningHistory> hashtagRunningHistory = hashtagRunningHistoryRepository
				.findById(hashtagRunningHistoryId);

		MessageResponse messageResponse = new MessageResponse();

		if (hashtagRunningHistory.isEmpty()) {
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());
			messageResponse.setMessage(RequestConstants.REQUEST_FAILED);

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}

		Client client = clientRepository.findByEmail(securityAuditorAware.getCurrentAuditor().get()).get();

		if (!client.getId().toString().equals(hashtagRunningHistory.get().getClient().getId().toString())) {
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());
			messageResponse.setMessage(RequestConstants.REQUEST_FAILED);

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}

		if (JobConstants.SUCCESS.equals(hashtagRunningHistory.get().getStatus())) {

			List<DataCrawl> dataCrawls = dataCrawlRepository
					.findAllByCreatedDatePostLessThanEqualAndHashtagOrderByCreatedDatePostDesc(
							getRunningTime(hashtagRunningHistory.get().getRunningTime()),
							hashtagRunningHistory.get().getHashtag());
			String hashtags = "";
			for (DataCrawl dataCrawl : dataCrawls) {
				if (dataCrawl.getCaption().split(" ").length > hashtags.split(" ").length) {
					hashtags = dataCrawl.getCaption();
				}
			}

			messageResponse.setData(hashtags);
			messageResponse.setMessage(RequestConstants.REQUEST_SUCCESS);
			messageResponse.setStatus(HttpStatus.OK.value());

			return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
		}

		messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());
		messageResponse.setMessage(RequestConstants.REQUEST_FAILED);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<MessageResponse> getRelatedHashtags(String hashtags) {

		List<String> relatedHashtags = new ArrayList<String>();
		
		HashMap<String, String> isExistsRelatedHashtags = new HashMap<String, String>();

		String uri = AppConstants.URI_RECOMMEND_HASHTAGS + hashtags;

		RestTemplate restTemplate = new RestTemplate();
		List<String> results = restTemplate.getForObject(uri, List.class);

		for (String label : results) {
			Optional<Hashtag> hashtag = hashtagRepository.findById(label);
			if (hashtag.isEmpty()) {
				continue;
			}

			Optional<Analysis> analysis = analysisRepository.findTopByHashtagOrderByIdDesc(hashtag.get());
			if (analysis.isEmpty()) {
				continue;
			}

			Pageable pageable = PageRequest.of(0, 30);

			Page<Analysis> analysisPage = analysisRepository.findAllByHashtagAndDateOfAnalysisOrderByIdDesc(
					hashtag.get(), analysis.get().getDateOfAnalysis(), pageable);

			for (int i = 0; i < RecommendConstants.TOP_HASHTAGS; i++) {
				Analysis analysis2 = analysisPage.getContent().get(analysisPage.getSize() - i - 1);
				String hashtagTop = analysis2.getNameHashtag();

				if (!isExistsRelatedHashtags.containsKey(hashtagTop)) {
					isExistsRelatedHashtags.put(hashtagTop, hashtagTop);
					relatedHashtags.add(hashtagTop);
				}
			}
		}

		MessageResponse messageResponse = new MessageResponse();
		messageResponse.setData(relatedHashtags);
		messageResponse.setStatus(HttpStatus.OK.value());
		messageResponse.setMessage(RequestConstants.REQUEST_SUCCESS);

		return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
	}

}
