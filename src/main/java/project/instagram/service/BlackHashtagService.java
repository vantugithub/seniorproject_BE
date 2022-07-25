package project.instagram.service;

import org.springframework.http.ResponseEntity;

import project.instagram.response.BlackHashtagResponse;
import project.instagram.response.MessageResponse;
import project.instagram.response.PagedResponse;

public interface BlackHashtagService {
	PagedResponse<BlackHashtagResponse> findAllBlackHashtags(int page, int size);
	
	ResponseEntity<MessageResponse> createBlackHashtagByManager(String name);
	
	ResponseEntity<MessageResponse> deleteBlackHashtag(String name);
}
