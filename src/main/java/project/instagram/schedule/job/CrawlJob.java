//package project.instagram.schedule.job;
//
//import java.time.Duration;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//import java.util.UUID;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import com.alibaba.fastjson.JSON;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import project.instagram.common.enums.constants.JobConstants;
//import project.instagram.entity.Client;
//import project.instagram.entity.Hashtag;
//import project.instagram.entity.HashtagClientManagement;
//import project.instagram.entity.HashtagRunningHistory;
//import project.instagram.entity.TransactionPackage;
//import project.instagram.repository.ClientRepository;
//import project.instagram.repository.HashtagClientManagementRepository;
//import project.instagram.repository.HashtagRepository;
//import project.instagram.repository.HashtagRunningHistoryRepository;
//import project.instagram.repository.TransactionPackageRepository;
//import project.instagram.utils.DateTimeZoneUtils;
//
//@Component
//public class CrawlJob {
//
//	@Autowired
//	private DateTimeZoneUtils dateTimeZoneUtils;
//
//	@Autowired
//	private StringRedisTemplate redisTemplate;
//
//	@Autowired
//	private ClientRepository clientRepository;
//
//	@Autowired
//	private HashtagRepository hashtagRepository;
//
//	@Autowired
//	private TransactionPackageRepository transactionPackageRepository;
//
//	@Autowired
//	private HashtagRunningHistoryRepository hashtagRunningHistoryRepository;
//
//	@Autowired
//	private HashtagClientManagementRepository hashtagClientManagementRepository;
//
//	private void updateActiveHashtagClientManagement(HashtagClientManagement hashtagClientManagement) {
//		hashtagClientManagement.setActive(false);
//
//		hashtagClientManagementRepository.save(hashtagClientManagement);
//	}
//
//	private Set<HashtagClientManagement> getHashtagClientManagements() {
//
//		Date currentDate = dateTimeZoneUtils.getDateZoneGMT();
//		System.out.println(currentDate.toString());
//
//		Set<HashtagClientManagement> hashtagClientManagements = hashtagClientManagementRepository
//				.findAllByDateStartCrawlAndActiveTrue(currentDate);
//
//		return hashtagClientManagements;
//	}
//
//	private Job createJob(String hashtagName, HashtagClientManagement hashtagClientManagement) {
//		Job job = new Job();
//		Set<HashtagClientManagementJob> hashtagClientManagementJobs = new HashSet<HashtagClientManagementJob>();
//
//		HashtagClientManagementJob hashtagClientManagementJob = new HashtagClientManagementJob();
//		hashtagClientManagementJob.setTransactionPackage(hashtagClientManagement.getTransactionPackage().getId());
//		hashtagClientManagementJob.setClientId(hashtagClientManagement.getClientManagement().getId().toString());
//		hashtagClientManagementJob.setCrawlQuantity(hashtagClientManagement.getCrawlQuantity());
//		hashtagClientManagementJob.setId(hashtagClientManagement.getId());
//
//		hashtagClientManagementJobs.add(hashtagClientManagementJob);
//
//		job.setHashtagClientManagementJobs(hashtagClientManagementJobs);
//		job.setCrawlQuantity(hashtagClientManagement.getCrawlQuantity());
//		job.setStatusJob(JobConstants.PENDING);
//		job.setHashtag(hashtagName);
//
//		return job;
//	}
//
//	private void setMapCrawlJobInfo(String hashtagName, HashtagClientManagement hashtagClientManagement,
//			HashMap<String, Job> mapCrawlJobInfo) {
//
//		HashtagClientManagementJob hashtagClientManagementJob = new HashtagClientManagementJob();
//		hashtagClientManagementJob.setTransactionPackage(hashtagClientManagement.getTransactionPackage().getId());
//		hashtagClientManagementJob.setClientId(hashtagClientManagement.getClientManagement().getId().toString());
//		hashtagClientManagementJob.setCrawlQuantity(hashtagClientManagement.getCrawlQuantity());
//		hashtagClientManagementJob.setId(hashtagClientManagement.getId());
//
//		mapCrawlJobInfo.get(hashtagName).setCrawlQuantity(hashtagClientManagement.getCrawlQuantity());
//		mapCrawlJobInfo.get(hashtagName).getHashtagClientManagementJobs().add(hashtagClientManagementJob);
//	}
//
//	private HashMap<String, Job> createCrawlJobs(Set<HashtagClientManagement> hashtagClientManagements) {
//
//		HashMap<String, Job> mapCrawlJobInfo = new HashMap<String, Job>();
//
//		for (HashtagClientManagement hashtagClientManagement : hashtagClientManagements) {
//			updateActiveHashtagClientManagement(hashtagClientManagement);
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
//	private void createHashtagRunningHistory(Job job, HashtagClientManagementJob hashtagClientManagementJob) {
//		HashtagRunningHistory hashtagRunningHistory = new HashtagRunningHistory();
//		Date currentDate = dateTimeZoneUtils.getDateTimeZoneGMT();
//		StringBuilder newRequestId = new StringBuilder(
//				currentDate.toString().replace(" ", "") + "_" + hashtagClientManagementJob.getClientId());
//		hashtagRunningHistory.setId(newRequestId.toString());
//
//		UUID clientUUID = UUID.fromString(hashtagClientManagementJob.getClientId());
//		Client client = clientRepository.getById(clientUUID);
//		hashtagRunningHistory.setClient(client);
//
//		Hashtag hashtag = hashtagRepository.getById(job.getHashtag());
//		hashtagRunningHistory.setHashtag(hashtag);
//
//		TransactionPackage transactionPackage = transactionPackageRepository
//				.getById((int) hashtagClientManagementJob.getTransactionPackage());
//		hashtagRunningHistory.setTransactionPackage(transactionPackage);
//
//		hashtagRunningHistory.setType(JobConstants.CRAWL);
//		hashtagRunningHistory.setRunningTime(currentDate);
//		hashtagRunningHistory.setStatus(job.getStatusJob());
//		hashtagRunningHistory.setCrawlQuantity(hashtagClientManagementJob.getCrawlQuantity());
//
//		hashtagRunningHistoryRepository.save(hashtagRunningHistory);
//	}
//
//	@Scheduled(fixedRate = JobConstants.THREE_SECONDS)
//	public void scheduleFixedRateTask() throws InterruptedException {
//		System.out.println("Task - " + new Date());
//		Set<HashtagClientManagement> hashtagClientManagements = getHashtagClientManagements();
//		if (hashtagClientManagements.isEmpty()) {
//			return;
//		}
//
//		HashMap<String, Job> hashMapCrawlJobInfo = createCrawlJobs(hashtagClientManagements);
//
//		for (Map.Entry<String, Job> entry : hashMapCrawlJobInfo.entrySet()) {
//			redisTemplate.opsForList().leftPush(JobConstants.CRAWL_JOB_QUEUE, JSON.toJSONString(entry));
//		}
//	}
//
//	@Scheduled(fixedRate = JobConstants.THREE_SECONDS)
//	public void scheduleGetResultCrawlJobQueue() throws InterruptedException {
//		Object consultResult = redisTemplate.opsForList().leftPop("resultCrawlJobQueue", Duration.ofMillis(1000));
//		if (consultResult != null) {
//			ObjectMapper mapper = new ObjectMapper();
//			char quotes = '"';
//			String result = JSON.toJSON(consultResult).toString().replaceAll("'", Character.toString(quotes));
//			try {
//
//				Job job = mapper.readValue(result, Job.class);
//				System.out.println(job.toString());
//				for (HashtagClientManagementJob hashtagClientManagementJob : job.getHashtagClientManagementJobs()) {
//					createHashtagRunningHistory(job, hashtagClientManagementJob);
//				}
//
//			} catch (JsonMappingException e) {
//				e.printStackTrace();
//			} catch (JsonProcessingException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//}
