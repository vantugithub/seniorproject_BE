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
import project.instagram.entity.HashtagClientManagement;
import project.instagram.repository.ClientRepository;
import project.instagram.repository.HashtagClientManagementRepository;
import project.instagram.response.HashtagClientManagementResponse;
import project.instagram.response.PagedResponse;
import project.instagram.security.SecurityAuditorAware;
import project.instagram.service.HashtagClientManagementService;

@Service
public class HashtagClientManagementServiceImpl implements HashtagClientManagementService {

	@Autowired
	private SecurityAuditorAware securityAuditorAware;

	@Autowired
	private HashtagClientManagementRepository hashtagClientManagementRepository;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private ClientRepository clientRepository;

	private HashtagClientManagementResponse createHashtagClientManagementResponse(
			HashtagClientManagement hashtagClientManagement) {
		HashtagClientManagementResponse hashtagClientManagementResponse = mapper.map(hashtagClientManagement,
				HashtagClientManagementResponse.class);
		hashtagClientManagementResponse.setHashtag(hashtagClientManagement.getHashtagClientManagement().getName());

		return hashtagClientManagementResponse;
	}

	@Override
	public PagedResponse<HashtagClientManagementResponse> findAllHashtagNoCrawl(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Client client = clientRepository.findByEmail(securityAuditorAware.getCurrentAuditor().get()).get();

		Page<HashtagClientManagement> hashtagClientManagements = hashtagClientManagementRepository
				.findAllByClientManagementAndActiveFalse(client, pageable);

		List<HashtagClientManagementResponse> hashtagClientManagementResponses = new ArrayList<HashtagClientManagementResponse>(
				hashtagClientManagements.getContent().size());

		for (HashtagClientManagement hashtagClientManagement : hashtagClientManagements.getContent()) {
			hashtagClientManagementResponses.add(createHashtagClientManagementResponse(hashtagClientManagement));
		}

		return new PagedResponse<>(hashtagClientManagementResponses, hashtagClientManagements.getNumber(),
				hashtagClientManagements.getSize(), hashtagClientManagements.getTotalElements(),
				hashtagClientManagements.getTotalPages(), hashtagClientManagements.isLast());
	}

	@Override
	public PagedResponse<HashtagClientManagementResponse> findAllHashtagCrawl(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Client client = clientRepository.findByEmail(securityAuditorAware.getCurrentAuditor().get()).get();

		Page<HashtagClientManagement> hashtagClientManagements = hashtagClientManagementRepository
				.findAllByClientManagementAndActiveTrue(client, pageable);

		List<HashtagClientManagementResponse> hashtagClientManagementResponses = new ArrayList<HashtagClientManagementResponse>(
				hashtagClientManagements.getContent().size());

		for (HashtagClientManagement hashtagClientManagement : hashtagClientManagements.getContent()) {
			hashtagClientManagementResponses.add(createHashtagClientManagementResponse(hashtagClientManagement));
		}

		return new PagedResponse<>(hashtagClientManagementResponses, hashtagClientManagements.getNumber(),
				hashtagClientManagements.getSize(), hashtagClientManagements.getTotalElements(),
				hashtagClientManagements.getTotalPages(), hashtagClientManagements.isLast());
	}

}
