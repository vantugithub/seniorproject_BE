package project.instagram.service;

import org.springframework.http.ResponseEntity;

import project.instagram.response.MessageResponse;

public interface RecommendService {
	ResponseEntity<MessageResponse> getLongestHashtags(String hashtagRunningHistoryId);
	
	ResponseEntity<MessageResponse> getRelatedHashtags(String hashtags);
}
