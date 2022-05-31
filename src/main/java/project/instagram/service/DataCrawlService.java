package project.instagram.service;

import project.instagram.response.DataCrawlResponse;
import project.instagram.response.PagedResponse;

public interface DataCrawlService {
	PagedResponse<DataCrawlResponse> findAllDataCrawls(int page, int size, String date, String hashtag);
}
