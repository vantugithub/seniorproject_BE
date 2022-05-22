//package project.instagram.schedule.job;
//
//import java.time.Duration;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.TimeUnit;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.ListOperations;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import com.alibaba.fastjson.JSON;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import project.instagram.entity.HashtagClientManagement;
//import project.instagram.repository.HashtagClientManagementRepository;
//import project.instagram.utils.DateTimeZoneUtils;
//
//@Component
//public class CrawlJob {
//
//	private static final String CRAWL_JOB_KEY = "crawlJobKey";
//	private static final String CRAWL_JOB_QUEUE = "crawlJobQueue";
//	private static final String RESULT_CRAWL_JOB_QUEUE = "resultCrawlJobQueue";
//	private static final long ONE_HOUR = 3600000;
//	private static final long ONE_S = 3000;
//	private static final long ONE_MINUTE = 6000;
//	private static final String PENDING = "PENDING";
//
//	@Autowired
//	private DateTimeZoneUtils dateTimeZoneUtils;
//
//	@Autowired
//	private StringRedisTemplate redisTemplate;
//
//	@Autowired
//	private HashtagClientManagementRepository hashtagClientManagementRepository;
//
//	private Set<HashtagClientManagement> getHashtagClientManagements() {
//
//		Date currentDate = dateTimeZoneUtils.getDateZoneGMT();
//		System.out.println(currentDate.toString());
//
//		Set<HashtagClientManagement> hashtagClientManagements = hashtagClientManagementRepository
//				.findAllByDateStartCrawl(currentDate);
//
//		return hashtagClientManagements;
//	}
//
//	private Job createJob(String hashtagName, HashtagClientManagement hashtagClientManagement) {
//		Job job = new Job();
//		Set<String> clientsName = new HashSet<String>();
//		clientsName.add(hashtagClientManagement.getClientManagement().getId().toString());
//		job.setListIdClients(clientsName);
//		job.setCrawlQuantity(hashtagClientManagement.getCrawlQuantity());
//		job.setStatusJob(PENDING);
//		job.setHashtag(hashtagName);
//
//		return job;
//	}
//
//	private void setMapCrawlJobInfo(String hashtagName, HashtagClientManagement hashtagClientManagement,
//			HashMap<String, Job> mapCrawlJobInfo) {
//		mapCrawlJobInfo.get(hashtagName).setCrawlQuantity(hashtagClientManagement.getCrawlQuantity());
//		mapCrawlJobInfo.get(hashtagName).getListIdClients().add(hashtagClientManagement.getClientManagement().getId().toString());
//		mapCrawlJobInfo.get(hashtagName).setHashtag(hashtagName);
//	}
//
//	private HashMap<String, Job> createCrawlJobs(Set<HashtagClientManagement> hashtagClientManagements) {
//
//		HashMap<String, Job> mapCrawlJobInfo = new HashMap<String, Job>();
//
//		for (HashtagClientManagement hashtagClientManagement : hashtagClientManagements) {
//
//			String hashtagName = hashtagClientManagement.getHashtagClientManagement().getName();
//
//			if (mapCrawlJobInfo.containsKey(hashtagName)) {
//				setMapCrawlJobInfo(hashtagName, hashtagClientManagement, mapCrawlJobInfo);
//			} else {
//				mapCrawlJobInfo.put(hashtagName, createJob(hashtagName, hashtagClientManagement));
//			}
//		}
//
//		return mapCrawlJobInfo;
//	}
//
//	@Scheduled(fixedRate = ONE_S)
//	public void scheduleFixedRateTask() throws InterruptedException {
//		System.out.println("Task - " + new Date());
//		Set<HashtagClientManagement> hashtagClientManagements = getHashtagClientManagements();
//		if (hashtagClientManagements.isEmpty()) {
//			return;
//		}
//		HashMap<String, Job> hashMapCrawlJobInfo = createCrawlJobs(hashtagClientManagements);
//		for (Map.Entry<String, Job> entry : hashMapCrawlJobInfo.entrySet()) {
//			redisTemplate.opsForList().leftPush(CRAWL_JOB_QUEUE, JSON.toJSONString(entry));
//		}
//	}
//
//	@Scheduled(fixedRate = ONE_S)
//	public void scheduleFixedRateTask2() throws InterruptedException {
//		Object consultResult = redisTemplate.opsForList().leftPop("resultCrawlJobQueue", Duration.ofMillis(1000));
//		ObjectMapper mapper = new ObjectMapper();
//		char quotes = '"';
//		String result = JSON.toJSON(consultResult).toString().replaceAll("'", Character.toString(quotes));
//		try {
//			
//			Job map = mapper.readValue(result, Job.class);
//			System.out.println(map.toString());
//			
//		} catch (JsonMappingException e) {
//			e.printStackTrace();
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//		}
//	}
//
//}
