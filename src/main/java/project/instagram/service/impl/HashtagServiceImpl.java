package project.instagram.service.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import project.instagram.common.enums.constants.HashtagConstants;
import project.instagram.entity.Client;
import project.instagram.entity.Hashtag;
import project.instagram.entity.HashtagClientManagement;
import project.instagram.repository.BlackHashtagRepository;
import project.instagram.repository.ClientRepository;
import project.instagram.repository.HashtagClientManagementRepository;
import project.instagram.repository.HashtagRepository;
import project.instagram.response.MessageResponse;
import project.instagram.security.SecurityAuditorAware;
import project.instagram.service.HashtagServive;

@Service
@Transactional
public class HashtagServiceImpl implements HashtagServive {
	
	@Autowired
	private HashtagRepository hashtagRepository;
	
	@Autowired 
	private BlackHashtagRepository blackHashtagRepository;
	
	@Autowired
	private HashtagClientManagementRepository hashtagClientManagementRepository;
	
	@Autowired
	private SecurityAuditorAware securityAuditorAware;
	
	@Autowired
	private ClientRepository clientRepository;
	
	MessageResponse createHashtagByClient(String hashtagName, Client client) {
		MessageResponse messageResponse = new MessageResponse();
		Hashtag createHashtagByClient = hashtagRepository.save(new Hashtag(hashtagName));
		hashtagClientManagementRepository.save(new HashtagClientManagement(client, createHashtagByClient));
		messageResponse.setData(hashtagName);
		messageResponse.setMessage(HashtagConstants.CREATED_HASHTAG_SUCCESSFULLY);
		messageResponse.setStatus(HttpStatus.OK.value());
		
		return messageResponse;
	}
	
	MessageResponse deleteHashtagByClient(Client client, Hashtag hashtag) {
		MessageResponse messageResponse = new MessageResponse();
		hashtagClientManagementRepository.deleteHashtagClientManagementByClientAndHashtag(client, hashtag);
		messageResponse.setMessage(HashtagConstants.DELETED_HASHTAG_SUCCESSFULLY);
		messageResponse.setStatus(HttpStatus.OK.value());
		
		return messageResponse;
	}
	
	boolean existsBlackHashtagByName(String hashtagName) {
		return blackHashtagRepository.existsById(hashtagName);
	}
	
	boolean existsHashtagClientManagementByClientAndHashtag(Client client, Hashtag hashtag) {
		if (hashtagClientManagementRepository.getHashtagClientManagementByClientAndHashtag(client, hashtag) == null)
			return true;
		
		return false;
	}

	@Override
	public ResponseEntity<MessageResponse> createHashtag(String hashtagName) {
		String cleanHashtagName = hashtagName.replace(" ", "").toLowerCase();
		MessageResponse messageResponse = new MessageResponse();
		messageResponse.setData(cleanHashtagName);
		
		Optional<Hashtag> hashTag = hashtagRepository.findById(cleanHashtagName);
		Client client = clientRepository.findByEmail(securityAuditorAware.getCurrentAuditor().get()).get();
		
		if (hashTag == null) {
			messageResponse = createHashtagByClient(cleanHashtagName, client);
			return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
		}
		
		if (existsBlackHashtagByName(cleanHashtagName)) {
			messageResponse.setMessage(HashtagConstants.HASHTAG_IN_BLACK_LIST_HASHTAGS);
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}

		if (existsHashtagClientManagementByClientAndHashtag(client, hashTag.get())) 
		{
			hashtagClientManagementRepository.save(new HashtagClientManagement(client, hashTag.get()));
			messageResponse.setMessage(HashtagConstants.CREATED_HASHTAG_SUCCESSFULLY);
			messageResponse.setStatus(HttpStatus.OK.value());
			
			return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
		}
		messageResponse.setMessage(HashtagConstants.HASHTAG_EXISTS);
		messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
	}

	@Override
	public ResponseEntity<MessageResponse> deleteHashtag(String hashtagName) {
		MessageResponse messageResponse = new MessageResponse();
		Optional<Hashtag> hashTag = hashtagRepository.findById(hashtagName);
		Client client = clientRepository.findByEmail(securityAuditorAware.getCurrentAuditor().get()).get();
		
		if (hashTag == null) {
			messageResponse.setMessage(HashtagConstants.HASHTAG_NOT_EXISTS);
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}
		if (existsHashtagClientManagementByClientAndHashtag(client, hashTag.get())) {
			messageResponse.setMessage(HashtagConstants.DELETED_HASHTAG_FAILED);
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}
		messageResponse = deleteHashtagByClient(client, hashTag.get());
		
		return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
	}
	
}
