package project.instagram.service;

import org.springframework.http.ResponseEntity;

import project.instagram.response.MessageResponse;

public interface HashtagServive {
	ResponseEntity<MessageResponse> createHashtag(String hashtagName);
	ResponseEntity<MessageResponse> deleteHashtag(String hashtagName);
}
