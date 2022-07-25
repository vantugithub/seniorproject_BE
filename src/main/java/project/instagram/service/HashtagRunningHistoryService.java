package project.instagram.service;

import org.springframework.http.ResponseEntity;

import project.instagram.response.HashtagRunningHistoryResponse;
import project.instagram.response.MessageResponse;
import project.instagram.response.PagedResponse;

public interface HashtagRunningHistoryService {
	PagedResponse<HashtagRunningHistoryResponse> findAllByClientByRunningTimeDesc(int page, int size);

	PagedResponse<HashtagRunningHistoryResponse> findAllByClientAndHashtag(int page, int size, String hashtag);

	ResponseEntity<MessageResponse> getAnalysisHashtags(String yearStr);
}
