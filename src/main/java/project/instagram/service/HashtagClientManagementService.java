package project.instagram.service;

import project.instagram.response.HashtagClientManagementResponse;
import project.instagram.response.PagedResponse;

public interface HashtagClientManagementService {
	PagedResponse<HashtagClientManagementResponse> findAllHashtagNoCrawl(int page, int size);
	
	PagedResponse<HashtagClientManagementResponse> findAllHashtagCrawl(int page, int size);

}
