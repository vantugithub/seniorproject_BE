package project.instagram.service.impl;

import java.util.ArrayList;
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
import project.instagram.entity.BlackHashtag;
import project.instagram.entity.Hashtag;
import project.instagram.entity.Staff;
import project.instagram.repository.BlackHashtagRepository;
import project.instagram.repository.HashtagRepository;
import project.instagram.repository.StaffRepository;
import project.instagram.response.BlackHashtagResponse;
import project.instagram.response.MessageResponse;
import project.instagram.response.PagedResponse;
import project.instagram.response.StaffResponse;
import project.instagram.security.SecurityAuditorAware;
import project.instagram.service.BlackHashtagService;

@Service
public class BlackHashtagServiceImpl implements BlackHashtagService {
	
	@Autowired
	private SecurityAuditorAware securityAuditorAware;

	@Autowired
	private BlackHashtagRepository blackHashtagRepository;

	@Autowired
	private HashtagRepository hashtagRepository;

	@Autowired
	private StaffRepository staffRepository;

	@Override
	public PagedResponse<BlackHashtagResponse> findAllBlackHashtags(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);

		Page<BlackHashtag> blackHashtags = blackHashtagRepository.findAll(pageable);

		List<BlackHashtagResponse> blackHashtagResponses = new ArrayList<BlackHashtagResponse>(
				blackHashtags.getContent().size());

		for (BlackHashtag blackHashtag : blackHashtags.getContent()) {
			blackHashtagResponses.add(createBlackHashtagResponse(blackHashtag));
		}

		return new PagedResponse<>(blackHashtagResponses, blackHashtags.getNumber(), blackHashtags.getSize(),
				blackHashtags.getTotalElements(), blackHashtags.getTotalPages(), blackHashtags.isLast());
	}

	private BlackHashtagResponse createBlackHashtagResponse(BlackHashtag blackHashtag) {
		BlackHashtagResponse blackHashtagResponse = new BlackHashtagResponse();

		Staff staff = staffRepository.findById(blackHashtag.getStaff().getId()).get();
		StaffResponse staffResponse = new StaffResponse();
		staffResponse.setEmail(staff.getEmail());
		staffResponse.setStaffId(staff.getId().toString());

		blackHashtagResponse.setStaffResponse(staffResponse);
		blackHashtagResponse.setActive(blackHashtag.isActive());
		blackHashtagResponse.setName(blackHashtag.getName());

		return blackHashtagResponse;
	}
	
	private void createBlackHashtag(Hashtag hashtag) {
		Staff staff = staffRepository.findByEmail(securityAuditorAware.getCurrentAuditor().get()).get();
		BlackHashtag blackHashtag = new BlackHashtag();
		blackHashtag.setActive(true);
		blackHashtag.setHashtag(hashtag);
		blackHashtag.setName(hashtag.getName());
		blackHashtag.setStaff(staff);
		
		blackHashtagRepository.save(blackHashtag);
	}
	

	@Override
	public ResponseEntity<MessageResponse> createBlackHashtagByManager(String name) {

		MessageResponse messageResponse = new MessageResponse();
		Optional<Hashtag> existsHashtag = hashtagRepository.findById(name);

		if (!existsHashtag.isEmpty()) {
			createBlackHashtag(existsHashtag.get());

			messageResponse.setMessage(HashtagConstants.CREATED_BLACK_HASHTAG_SUCCESSFULLY);
			messageResponse.setStatus(HttpStatus.OK.value());

			return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
		}

		Hashtag newHashtag = new Hashtag();
		newHashtag.setName(name);
		newHashtag = hashtagRepository.save(newHashtag);

		createBlackHashtag(newHashtag);

		messageResponse.setMessage(HashtagConstants.CREATED_BLACK_HASHTAG_SUCCESSFULLY);
		messageResponse.setStatus(HttpStatus.OK.value());

		return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
	}

	@Override
	public ResponseEntity<MessageResponse> deleteBlackHashtag(String name) {
		Optional<BlackHashtag> blackHashtag = blackHashtagRepository.findById(name);
		MessageResponse messageResponse = new MessageResponse();
		
		
		if (blackHashtag.isEmpty()) {
			messageResponse.setMessage(HashtagConstants.BLACK_HASHTAG_NOT_EXISTS);
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}
		blackHashtag.get().setActive(!blackHashtag.get().isActive());
		blackHashtagRepository.save(blackHashtag.get());
		messageResponse.setMessage(HashtagConstants.UPDATE_ACTIVE_BLACK_HASHTAG_SUCCESSFULLY);
		messageResponse.setStatus(HttpStatus.OK.value());
		
		return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
	}

}
