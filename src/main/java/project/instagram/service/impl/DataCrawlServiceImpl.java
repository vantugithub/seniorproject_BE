package project.instagram.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import project.instagram.entity.DataCrawl;
import project.instagram.entity.Hashtag;
import project.instagram.repository.DataCrawlRepository;
import project.instagram.repository.HashtagRepository;
import project.instagram.response.DataCrawlResponse;
import project.instagram.response.PagedResponse;
import project.instagram.service.DataCrawlService;

@Service
public class DataCrawlServiceImpl implements DataCrawlService {

	@Autowired
	private DataCrawlRepository dataCrawlRepository;

	@Autowired
	private HashtagRepository hashtagRepository;
	
	@Autowired
	private ModelMapper mapper;

	private Date getRunningTime(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR, 18);

		return calendar.getTime();
	}
	
	private Date convertStringToDate(String dateString) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {

            date = formatter.parse(dateString);

        } catch (ParseException e) {
            e.printStackTrace();
        }
		
		return date;
	}

	@Override
	public PagedResponse<DataCrawlResponse> findAllDataCrawls(int page, int size, String date, String hashtagName) {
		Pageable pageable = PageRequest.of(page, size);

		Hashtag hashtag = hashtagRepository.getById(hashtagName);

		Page<DataCrawl> dataCrawls = dataCrawlRepository
				.findAllByCreatedDatePostLessThanEqualAndHashtagOrderByCreatedDatePostDesc(pageable,
						getRunningTime(convertStringToDate(date)), hashtag);

		List<DataCrawlResponse> crawlResponses = new ArrayList<DataCrawlResponse>(dataCrawls.getContent().size());

		for (DataCrawl dataCrawl : dataCrawls.getContent()) {
			crawlResponses.add(createDataCrawlsResponse(dataCrawl));
		}

		return new PagedResponse<>(crawlResponses, dataCrawls.getNumber(), dataCrawls.getSize(),
				dataCrawls.getTotalElements(), dataCrawls.getTotalPages(), dataCrawls.isLast());
	}

	private DataCrawlResponse createDataCrawlsResponse(DataCrawl dataCrawl) {
		DataCrawlResponse dataCrawlResponse = mapper.map(dataCrawl, DataCrawlResponse.class);
		dataCrawlResponse.setHashtag(dataCrawl.getHashtag().getName());
		
		return dataCrawlResponse;
	}

}
