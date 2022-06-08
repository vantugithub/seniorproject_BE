package project.instagram.service;

import org.springframework.http.ResponseEntity;

import project.instagram.response.DataCrawlResponse;
import project.instagram.response.PagedResponse;

public interface DataCrawlService {
	PagedResponse<DataCrawlResponse> findAllDataCrawls(int page, int size, String date, String hashtag);

	PagedResponse<DataCrawlResponse> findAllDataCrawlsByHashtagRunningHistoryId(int page, int size,
			String hashtagRunningHistoryId);

	ResponseEntity<?> searchHashtag(String hashtag);
	
	PagedResponse<DataCrawlResponse> exportDataCrawls(String hashtagRunningHistoryId);
}
