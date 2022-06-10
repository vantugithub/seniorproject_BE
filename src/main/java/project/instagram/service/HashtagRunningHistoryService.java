package project.instagram.service;

import project.instagram.response.HashtagRunningHistoryResponse;
import project.instagram.response.PagedResponse;

public interface HashtagRunningHistoryService {
	PagedResponse<HashtagRunningHistoryResponse> findAllByClientByRunningTimeDesc(int page, int size);

	PagedResponse<HashtagRunningHistoryResponse> findAllByClientAndHashtag(int page, int size, String hashtag);
}
