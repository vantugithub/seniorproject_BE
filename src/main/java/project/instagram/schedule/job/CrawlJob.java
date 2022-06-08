package project.instagram.schedule.job;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import project.instagram.common.enums.constants.JobConstants;
import project.instagram.entity.Client;
import project.instagram.entity.Hashtag;
import project.instagram.entity.HashtagClientManagement;
import project.instagram.entity.HashtagRunningHistory;
import project.instagram.entity.TransactionPackage;
import project.instagram.repository.ClientRepository;
import project.instagram.repository.HashtagClientManagementRepository;
import project.instagram.repository.HashtagRepository;
import project.instagram.repository.HashtagRunningHistoryRepository;
import project.instagram.repository.TransactionPackageRepository;
import project.instagram.response.HashtagRunningHistoryResponse;
import project.instagram.response.ParameterCrawlDataPageResponse;
import project.instagram.socket.config.Message;
import project.instagram.utils.DateTimeZoneUtils;

@Component
public class CrawlJob {

	@Value("${path.url}")
	private String URL;

	@Autowired
	private DateTimeZoneUtils dateTimeZoneUtils;

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private HashtagRepository hashtagRepository;

	@Autowired
	private TransactionPackageRepository transactionPackageRepository;

	@Autowired
	private HashtagRunningHistoryRepository hashtagRunningHistoryRepository;

	@Autowired
	private HashtagClientManagementRepository hashtagClientManagementRepository;

	private void updateActiveHashtagClientManagement(HashtagClientManagement hashtagClientManagement) {
		hashtagClientManagement.setActive(false);

		hashtagClientManagementRepository.save(hashtagClientManagement);
	}

	private Set<HashtagClientManagement> getHashtagClientManagements() {

		Date currentDate = dateTimeZoneUtils.getDateZoneGMT();

		Set<HashtagClientManagement> hashtagClientManagements = hashtagClientManagementRepository
				.findAllByDateStartCrawlAndActiveTrue(currentDate);

		return hashtagClientManagements;
	}

	private Job createJob(String hashtagName, HashtagClientManagement hashtagClientManagement) {
		Job job = new Job();
		Set<HashtagClientManagementJob> hashtagClientManagementJobs = new HashSet<HashtagClientManagementJob>();

		HashtagClientManagementJob hashtagClientManagementJob = new HashtagClientManagementJob();
		hashtagClientManagementJob.setTransactionPackage(hashtagClientManagement.getTransactionPackage().getId());
		hashtagClientManagementJob.setClientId(hashtagClientManagement.getClientManagement().getId().toString());
		hashtagClientManagementJob.setCrawlQuantity(hashtagClientManagement.getCrawlQuantity());
		hashtagClientManagementJob.setId(hashtagClientManagement.getId());

		hashtagClientManagementJobs.add(hashtagClientManagementJob);

		job.setHashtagClientManagementJobs(hashtagClientManagementJobs);
		job.setCrawlQuantity(hashtagClientManagement.getCrawlQuantity());
		job.setStatusJob(JobConstants.PENDING);
		job.setTypeJob(JobConstants.CRAWL);
		job.setHashtag(hashtagName);

		return job;
	}

	private void setMapCrawlJobInfo(String hashtagName, HashtagClientManagement hashtagClientManagement,
			HashMap<String, Job> mapCrawlJobInfo) {

		HashtagClientManagementJob hashtagClientManagementJob = new HashtagClientManagementJob();
		hashtagClientManagementJob.setTransactionPackage(hashtagClientManagement.getTransactionPackage().getId());
		hashtagClientManagementJob.setClientId(hashtagClientManagement.getClientManagement().getId().toString());
		hashtagClientManagementJob.setCrawlQuantity(hashtagClientManagement.getCrawlQuantity());
		hashtagClientManagementJob.setId(hashtagClientManagement.getId());

		mapCrawlJobInfo.get(hashtagName).setCrawlQuantity(hashtagClientManagement.getCrawlQuantity());
		mapCrawlJobInfo.get(hashtagName).getHashtagClientManagementJobs().add(hashtagClientManagementJob);
	}

	private HashMap<String, Job> createCrawlJobs(Set<HashtagClientManagement> hashtagClientManagements) {

		HashMap<String, Job> mapCrawlJobInfo = new HashMap<String, Job>();

		for (HashtagClientManagement hashtagClientManagement : hashtagClientManagements) {

			updateActiveHashtagClientManagement(hashtagClientManagement);

			String hashtagName = hashtagClientManagement.getHashtagClientManagement().getName();

			if (mapCrawlJobInfo.containsKey(hashtagName)) {
				setMapCrawlJobInfo(hashtagName, hashtagClientManagement, mapCrawlJobInfo);
			} else {
				mapCrawlJobInfo.put(hashtagName, createJob(hashtagName, hashtagClientManagement));
			}
		}

		return mapCrawlJobInfo;
	}

	private String createHashtagRunningHistory(Job job, HashtagClientManagementJob hashtagClientManagementJob,
			Date currentDate) {
		HashtagRunningHistory hashtagRunningHistory = new HashtagRunningHistory();

		StringBuilder newRequestId = new StringBuilder(
				currentDate.toString().replace(" ", "") + "_" + hashtagClientManagementJob.getClientId());
		hashtagRunningHistory.setId(newRequestId.toString());

		UUID clientUUID = UUID.fromString(hashtagClientManagementJob.getClientId());
		Client client = clientRepository.getById(clientUUID);
		hashtagRunningHistory.setClient(client);

		Hashtag hashtag = hashtagRepository.getById(job.getHashtag());
		hashtagRunningHistory.setHashtag(hashtag);

		TransactionPackage transactionPackage = transactionPackageRepository
				.getById((int) hashtagClientManagementJob.getTransactionPackage());
		hashtagRunningHistory.setTransactionPackage(transactionPackage);

		hashtagRunningHistory.setType(job.getTypeJob());
		hashtagRunningHistory.setRunningTime(currentDate);
		hashtagRunningHistory.setStatus(job.getStatusJob());
		hashtagRunningHistory.setCrawlQuantity(hashtagClientManagementJob.getCrawlQuantity());

		hashtagRunningHistory = hashtagRunningHistoryRepository.save(hashtagRunningHistory);

		return hashtagRunningHistory.getId();
	}

	@Scheduled(fixedRate = JobConstants.ONE_HOUR)
	public void scheduleFixedRateTask() throws InterruptedException {
		System.out.println("Task - " + new Date());

		Set<HashtagClientManagement> hashtagClientManagements = getHashtagClientManagements();
		if (hashtagClientManagements.isEmpty()) {
			return;
		}
		HashMap<String, Job> hashMapCrawlJobInfo = createCrawlJobs(hashtagClientManagements);

		for (Map.Entry<String, Job> entry : hashMapCrawlJobInfo.entrySet()) {
			redisTemplate.opsForList().leftPush(JobConstants.CRAWL_JOB_QUEUE, JSON.toJSONString(entry));
		}
	}

	@Scheduled(fixedRate = JobConstants.ONE_MINUTE)
	public void scheduleGetResultCrawlJobQueue() throws InterruptedException {
		if (redisTemplate.hasKey("resultCrawlJobQueue")) {
			Object consultResult = redisTemplate.opsForList().leftPop("resultCrawlJobQueue", Duration.ofMillis(1000));
			Date currentDate = dateTimeZoneUtils.getDateTimeZoneGMT();
			if (consultResult != null) {
				ObjectMapper mapper = new ObjectMapper();
				char quotes = '"';
				String result = JSON.toJSON(consultResult).toString().replaceAll("'", Character.toString(quotes));
				try {
					Job job = mapper.readValue(result, Job.class);
					for (HashtagClientManagementJob hashtagClientManagementJob : job.getHashtagClientManagementJobs()) {
						String hashtagRunningHistoryId = createHashtagRunningHistory(job, hashtagClientManagementJob,
								currentDate);
						sendNotificationForClient(job, hashtagClientManagementJob, currentDate,
								hashtagRunningHistoryId);
					}
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			}
		}

	}

//	@Scheduled(fixedRate = JobConstants.THREE_SECONDS)
//	public void scheduleFixedRateMessageTask() throws InterruptedException {
//		StringBuilder messageToClient = new StringBuilder("SEARCH" + "ED " + "iloveyou" + " done!");
//		Message message = new Message();
//		message.setMessage(messageToClient.toString());
//		message.setTitle("SEARCHED");
//
////		ParameterCrawlDataPageResponse parameterCrawlDataPageResponse = new ParameterCrawlDataPageResponse();
////		parameterCrawlDataPageResponse.setDate(dateTimeZoneUtils.getDateTimeZoneGMT().toString());
////		parameterCrawlDataPageResponse.setPage(1);
////		parameterCrawlDataPageResponse.setSize(15);
////		parameterCrawlDataPageResponse.setHashtag("iloveyou");
//
////		StringBuilder urlForward = new StringBuilder(URL + "api/client/data-crawls");
////		parameterCrawlDataPageResponse.setUrl(urlForward.toString());
//		HashtagRunningHistoryResponse hashtagRunningHistoryResponse = new HashtagRunningHistoryResponse();
//		hashtagRunningHistoryResponse.setHashtag("iloveyou");
//		hashtagRunningHistoryResponse.setId("2022-05-3005:14:51.785767_09ee87aa-9542-4e92-aa41-0e73205a34e8");
//		
//		message.setObject(hashtagRunningHistoryResponse);
//
//		simpMessagingTemplate.convertAndSendToUser("nguyenvantu11041999@gmail.com", "/private", message);
//	}

	private void sendNotificationForClient(Job job, HashtagClientManagementJob hashtagClientManagementJob,
			Date currentDate, String hashtagRunningHistoryId) {

		StringBuilder messageToClient = new StringBuilder(
				job.getTypeJob() + "ED " + job.getHashtag() + " " + job.getTypeJob());

		Message message = new Message();
		message.setMessage(messageToClient.toString());
		message.setTitle(job.getTypeJob());

//		ParameterCrawlDataPageResponse parameterCrawlDataPageResponse = new ParameterCrawlDataPageResponse();
//		parameterCrawlDataPageResponse.setDate(currentDate.toString());
//		parameterCrawlDataPageResponse.setPage(1);
//		parameterCrawlDataPageResponse.setSize(15);
//		parameterCrawlDataPageResponse.setHashtag(job.getHashtag());

//		StringBuilder urlForward = new StringBuilder(URL + "api/client/data-crawls");
//		parameterCrawlDataPageResponse.setUrl(urlForward.toString());

		HashtagRunningHistoryResponse hashtagRunningHistoryResponse = new HashtagRunningHistoryResponse();
		hashtagRunningHistoryResponse.setHashtag(job.getHashtag());
		hashtagRunningHistoryResponse.setId(hashtagRunningHistoryId);

		message.setObject(hashtagRunningHistoryResponse);

		UUID clientUUID = UUID.fromString(hashtagClientManagementJob.getClientId());
		Client client = clientRepository.findById(clientUUID).get();

		String emailClient = client.getEmail();

		simpMessagingTemplate.convertAndSendToUser(emailClient, "/private", message);

	}

//	@Scheduled(fixedRate = JobConstants.ONE_MINUTE)
//	public void scheduleFixedRateMessageTask() throws InterruptedException {
//		String temp = "Task - " + new Date().toString();
//		Message message = new Message();
//		message.setMessage(temp);
//		message.setTitle("Searched");
//		simpMessagingTemplate.convertAndSendToUser("nguyenvantu11041999@gmail.com", "/private", message);
//	}
//
//	@Scheduled(fixedRate = JobConstants.ONE_MINUTE)
//	public void scheduleFixedRateFindAllPendingRequestsTask() throws InterruptedException {
//		
//		redisTemplate.opsForList().leftPush(JobConstants.PENDING_REQUESTS, UUID.randomUUID().toString());
//
//		if (redisTemplate.hasKey(JobConstants.PENDING_REQUESTS)) {
//			Object consultResult = redisTemplate.opsForList().leftPop(JobConstants.PENDING_REQUESTS);	
//			
//			redisTemplate.opsForList().leftPush(consultResult.toString(), consultResult.toString());
//			
//			redisTemplate.expire(consultResult.toString(), 60, TimeUnit.SECONDS);
//			
//			redisTemplate.delete(consultResult.toString());
//		}
//	}
//
//	@Scheduled(fixedRate = JobConstants.ONE_HOUR)
//	public void scheduleFixedRateMessageTask() throws InterruptedException {
//		if (redisTemplate.hasKey(JobConstants.CRAWL_JOB_QUEUE)) {
//			String hashtagName = "netflix";
//			HashMap<String, Job> hashMapCrawlJobInfo = new HashMap<String, Job>();
//
//			Job job = new Job();
//			job.setCrawlQuantity(100);
//			job.setHashtag(hashtagName);
//
//			HashtagClientManagementJob hashtagClientManagementJob = new HashtagClientManagementJob();
//			hashtagClientManagementJob.setTransactionPackage(1);
//			hashtagClientManagementJob.setClientId("oasdasdsadasdasdsadsa");
//			hashtagClientManagementJob.setCrawlQuantity(222);
//			hashtagClientManagementJob.setId(1);
//
//			Set<HashtagClientManagementJob> hashtagClientManagementJobs = new HashSet<HashtagClientManagementJob>();
//			hashtagClientManagementJobs.add(hashtagClientManagementJob);
//
//			job.setHashtagClientManagementJobs(hashtagClientManagementJobs);
//
//			job.setStatusJob("PENDING");
//			job.setStatusJob(JobConstants.SEARCH);
//
//			hashMapCrawlJobInfo.put(hashtagName, job);
//
//			for (Map.Entry<String, Job> entry : hashMapCrawlJobInfo.entrySet()) {
//				redisTemplate.opsForList().leftPush(JobConstants.CRAWL_JOB_QUEUE, JSON.toJSONString(entry));
//			}
//
//		}
//	}

}
