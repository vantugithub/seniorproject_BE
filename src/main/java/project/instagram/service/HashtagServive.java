package project.instagram.service;

import org.springframework.http.ResponseEntity;

import project.instagram.response.HashtagResponse;
import project.instagram.response.MessageResponse;
import project.instagram.response.PagedResponse;

public interface HashtagServive {
	ResponseEntity<MessageResponse> createHashtag(String hashtagName);

	ResponseEntity<MessageResponse> deleteHashtag(String hashtagName);

	ResponseEntity<MessageResponse> enableCrawlHashtagByClient(String hashtagName, String dateCrawlStr);
	
	PagedResponse<HashtagResponse> findAllHashtags(int page, int size);
	
	ResponseEntity<MessageResponse> createHashtagByManager(String hashtagName);
}
