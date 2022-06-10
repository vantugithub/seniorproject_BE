package project.instagram.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import project.instagram.entity.Client;
import project.instagram.entity.Hashtag;
import project.instagram.entity.HashtagRunningHistory;
import project.instagram.repository.ClientRepository;
import project.instagram.repository.HashtagRepository;
import project.instagram.repository.HashtagRunningHistoryRepository;
import project.instagram.response.HashtagRunningHistoryResponse;
import project.instagram.response.PagedResponse;
import project.instagram.security.SecurityAuditorAware;
import project.instagram.service.HashtagRunningHistoryService;

@Service
public class HashtagRunningHistoryServiceImpl implements HashtagRunningHistoryService {

	@Autowired
	private ModelMapper mapper;

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

}
