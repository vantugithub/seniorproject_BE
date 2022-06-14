package project.instagram.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import project.instagram.common.enums.constants.JobConstants;
import project.instagram.common.enums.constants.Validation;
import project.instagram.entity.Client;
import project.instagram.entity.DataCrawl;
import project.instagram.entity.DateRange;
import project.instagram.entity.Hashtag;
import project.instagram.entity.HashtagClientManagement;
import project.instagram.entity.HashtagRunningHistory;
import project.instagram.entity.Package;
import project.instagram.entity.RunningSummary;
import project.instagram.entity.TransactionPackage;
import project.instagram.entity.TypeOfPackage;
import project.instagram.repository.ClientRepository;
import project.instagram.repository.DataCrawlRepository;
import project.instagram.repository.HashtagClientManagementRepository;
import project.instagram.repository.HashtagRepository;
import project.instagram.repository.HashtagRunningHistoryRepository;
import project.instagram.repository.PackageRepository;
import project.instagram.repository.RunningSummaryRepository;
import project.instagram.repository.TransactionPackageRepository;
import project.instagram.repository.TypeOfPackageRepository;
import project.instagram.response.DataCrawlResponse;
import project.instagram.response.MessageResponse;
import project.instagram.response.PagedResponse;
import project.instagram.schedule.job.HashtagClientManagementJob;
import project.instagram.schedule.job.Job;
import project.instagram.security.SecurityAuditorAware;
import project.instagram.service.DataCrawlService;
import project.instagram.utils.DateTimeZoneUtils;

@Service
@Transactional
public class DataCrawlServiceImpl implements DataCrawlService {

	@Value("${path.url}")
	private String URL;

	private static final String PACKAGE_TYPE = "Package";

	private static final String EXTRA_PACKAGE_TYPE = "Extra package";

	@Autowired
	private DataCrawlRepository dataCrawlRepository;

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private HashtagRepository hashtagRepository;

	@Autowired
	private DateTimeZoneUtils dateTimeZoneUtils;

	@Autowired
	private TransactionPackageRepository transactionPackageRepository;

	@Autowired
	private SecurityAuditorAware securityAuditorAware;

	@Autowired
	private RunningSummaryRepository runningSummaryRepository;

	@Autowired
	private HashtagRunningHistoryRepository hashtagRunningHistoryRepository;

	@Autowired
	private TypeOfPackageRepository typeOfPackageRepository;

	@Autowired
	private PackageRepository packageRepository;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private HashtagClientManagementRepository hashtagClientManagementRepository;

	@Autowired
	private ModelMapper mapper;

	private Date getRunningTime(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR, 16);

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

	private Set<TransactionPackage> sortTransactionPackagesByExpiredDate(Set<TransactionPackage> transactionPackages) {

		ArrayList<TransactionPackage> transactionPackagesExpiredDateNull = new ArrayList<>();
		ArrayList<TransactionPackage> transactionPackagesExpiredDateNotNull = new ArrayList<>();

		for (TransactionPackage transactionPackage : transactionPackages) {
			if (transactionPackage.getExpiredDate() == null) {
				transactionPackagesExpiredDateNull.add(transactionPackage);
			} else {
				transactionPackagesExpiredDateNotNull.add(transactionPackage);
			}
		}
		transactionPackagesExpiredDateNotNull.addAll(transactionPackagesExpiredDateNull);
		Set<TransactionPackage> resultSet = new LinkedHashSet<TransactionPackage>(
				transactionPackagesExpiredDateNotNull);

		return resultSet;
	}

	@SuppressWarnings("deprecation")
	private Date createPointOfTime(Date oldDate, Date currentDate) {
		Date newDate = oldDate;
		newDate.setMonth(currentDate.getMonth());

		return newDate;
	}

	private Optional<TypeOfPackage> findTypeOfPackage(Package packageFromTransactionPackage) {

		UUID packageTypeUUID = UUID.fromString(packageFromTransactionPackage.getTypeOfPackage().getId().toString());
		Optional<TypeOfPackage> typeOfPackage = typeOfPackageRepository.findById(packageTypeUUID);

		return typeOfPackage;
	}

	private boolean isDateInRange(Date date, DateRange dateRange) {
		System.out.println(dateRange.getStartDate());
		System.out.println(dateRange.getEndDate());
		if (date.after(dateRange.getStartDate()) && date.before(dateRange.getEndDate())) {
			return true;
		}

		return false;
	}

	private byte calculateRemainingQuantitySearchHashtag(Optional<TypeOfPackage> typeOfPackage,
			byte quantitySearchOfPackage, RunningSummary runningSummary) {

		if (PACKAGE_TYPE.equals(typeOfPackage.get().getName())) {
			return (byte) (quantitySearchOfPackage - runningSummary.getSearchedPackageQuantity());
		}

		return (byte) (quantitySearchOfPackage - runningSummary.getSearchedExtraPackageQuantity());
	}

	@SuppressWarnings({ "unused", "deprecation" })
	private void enableCrawlHashtag(Hashtag hashtag, Client client, Date dateCrawl, short crawlQuantity,
			TransactionPackage transactionPackage) {

		HashtagClientManagement hashtagClientManagement = new HashtagClientManagement();
		hashtagClientManagement.setActive(true);
		hashtagClientManagement.setClientManagement(client);
		hashtagClientManagement.setHashtagClientManagement(hashtag);
		hashtagClientManagement.setCrawlQuantity(crawlQuantity);
		dateCrawl.setHours(-24);
		hashtagClientManagement.setDateStartCrawl(dateCrawl);
		hashtagClientManagement.setTransactionPackage(transactionPackage);

		hashtagClientManagementRepository.save(hashtagClientManagement);

	}

	RunningSummary createNewRunningSummary(Client client, TransactionPackage transactionPackage, DateRange dateRange,
			Optional<TypeOfPackage> typeOfPackage) {

		RunningSummary runningSummary = new RunningSummary();
		runningSummary.setClient(client);
		runningSummary.setIssuedDate(dateRange.getStartDate());
		runningSummary.setExpiredDate(dateRange.getEndDate());
		runningSummary.setTransactionPackage(transactionPackage);

		if (PACKAGE_TYPE.equals(typeOfPackage.get().getName())) {
			runningSummary.setSearchedPackageQuantity((byte) 1);
		}

		if (EXTRA_PACKAGE_TYPE.equals(typeOfPackage.get().getName())) {
			runningSummary.setSearchedExtraPackageQuantity((byte) 1);
		}

		return runningSummary;
	}

	private int searchHashtagByClient() {

		Client client = clientRepository.findByEmail(securityAuditorAware.getCurrentAuditor().get()).get();
		Date currentDate = dateTimeZoneUtils.getLocalDateTime();

		Set<TransactionPackage> setValidTransactionPackage = transactionPackageRepository
				.findAllValidTransactionPackages(client, currentDate);

		if (setValidTransactionPackage.size() == 0) {
			return -1;
		}

		for (TransactionPackage transactionPackage : sortTransactionPackagesByExpiredDate(setValidTransactionPackage)) {

			Package packageFromTransactionPackage = transactionPackage.getParentPackage();
			Optional<TypeOfPackage> typeOfPackage = findTypeOfPackage(packageFromTransactionPackage);

			Optional<RunningSummary> validRunningSummary = runningSummaryRepository
					.findRunningSummaryByExpiredDateGreaterThanEqualAndTransactionPackage(currentDate,
							transactionPackage);

			if (validRunningSummary.isEmpty()) {

				Date issuedDateTransactionPackage = transactionPackage.getIssuedeDate();
				Date pointOfTime = createPointOfTime(issuedDateTransactionPackage, currentDate);

				DateRange dateRange = new DateRange();
				dateRange.correspondingDateRange(currentDate, pointOfTime);

				if (isDateInRange(currentDate, dateRange)) {

					RunningSummary runningSummary = createNewRunningSummary(client, transactionPackage, dateRange,
							typeOfPackage);

					runningSummaryRepository.save(runningSummary);

//					enableCrawlHashtag(hashtag, client, currentDate,
//							packageFromTransactionPackage.getNumberOfPostsPerHashtag(), transactionPackage);

					return transactionPackage.getId();
				}

				continue;
			}

			if (!isDateInRange(currentDate, new DateRange(validRunningSummary.get().getIssuedDate(),
					validRunningSummary.get().getExpiredDate()))) {
				continue;
			}

//			if (!isDateInRange(currentDate, new DateRange(validRunningSummary.get().getIssuedDate(),
//					validRunningSummary.get().getExpiredDate()))) {
//				continue;
//			}

			byte remainingQuantityCrawlHashtag = calculateRemainingQuantitySearchHashtag(typeOfPackage,
					packageFromTransactionPackage.getSearchQuantity(), validRunningSummary.get());

			if (remainingQuantityCrawlHashtag == 0) {
				continue;
			}

			validRunningSummary.get().increaseQuantitySearch(typeOfPackage.get(), 1);
			runningSummaryRepository.save(validRunningSummary.get());

//			enableCrawlHashtag(hashtag, client, currentDate, packageFromTransactionPackage.getNumberOfPostsPerHashtag(), transactionPackage);

			return transactionPackage.getId();
		}

		return -1;
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

	private void createCrawlJob(String hashtagName, int transactionPackageId, Client client) {

		HashMap<String, Job> hashMapCrawlJobInfo = new HashMap<String, Job>();

		TransactionPackage transactionPackage = transactionPackageRepository.getById(transactionPackageId);
		Package packageOfClient = packageRepository.getById(transactionPackage.getParentPackage().getId());

		Job job = new Job();
		job.setCrawlQuantity(packageOfClient.getNumberOfPostInEachSearch());
		job.setHashtag(hashtagName);

		HashtagClientManagementJob hashtagClientManagementJob = new HashtagClientManagementJob();
		hashtagClientManagementJob.setTransactionPackage(transactionPackageId);
		hashtagClientManagementJob.setClientId(client.getId().toString());
		hashtagClientManagementJob.setCrawlQuantity(packageOfClient.getNumberOfPostInEachSearch());
		hashtagClientManagementJob.setId(0);

		Set<HashtagClientManagementJob> hashtagClientManagementJobs = new HashSet<HashtagClientManagementJob>();
		hashtagClientManagementJobs.add(hashtagClientManagementJob);

		job.setHashtagClientManagementJobs(hashtagClientManagementJobs);

		job.setStatusJob(JobConstants.PENDING);
		job.setTypeJob(JobConstants.SEARCH);

		hashMapCrawlJobInfo.put(hashtagName, job);

		for (Map.Entry<String, Job> entry : hashMapCrawlJobInfo.entrySet()) {
			redisTemplate.opsForList().leftPush(JobConstants.CRAWL_JOB_QUEUE, JSON.toJSONString(entry));
		}
	}

	@Override
	public ResponseEntity<?> searchHashtag(String hashtagName) {

		Client client = clientRepository.findByEmail(securityAuditorAware.getCurrentAuditor().get()).get();

		MessageResponse messageResponse = new MessageResponse();

		int transactionPackageId = searchHashtagByClient();

		if (transactionPackageId == -1) {
			messageResponse.setMessage(Validation.CANNOT_SEARCH);
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}

		Optional<Hashtag> existsHashtag = hashtagRepository.findById(hashtagName);

		if (existsHashtag.isEmpty()) {

			hashtagRepository.save(new Hashtag(hashtagName));

			createCrawlJob(hashtagName, transactionPackageId, client);

			messageResponse.setMessage(Validation.WAITING_A_FEW_MINUTES);
			messageResponse.setStatus(HttpStatus.OK.value());

			return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
		}

		Date currentDate = dateTimeZoneUtils.getDateTimeZoneGMT();

		Hashtag hashtag = hashtagRepository.findById(hashtagName).get();

		Optional<DataCrawl> dataCrawl = dataCrawlRepository
				.findFirstByCreatedDatePostLessThanEqualAndHashtagOrderByCreatedDatePostDesc(currentDate, hashtag);

		if (dataCrawl.isEmpty()) {
			createCrawlJob(hashtagName, transactionPackageId, client);

			messageResponse.setMessage(Validation.WAITING_A_FEW_MINUTES);
			messageResponse.setStatus(HttpStatus.OK.value());

			return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
		}

		if ((currentDate.getTime() - dataCrawl.get().getCreatedDatePost().getTime()) / (60 * 60 * 1000) <= 48) {
			System.out.println(currentDate.toString());
			HashtagRunningHistory hashtagRunningHistory = createHashtagRunningHistory(currentDate, client,
					transactionPackageId, hashtag);

			messageResponse.setMessage(Validation.DATA_IS_EXISTS);
			messageResponse.setStatus(HttpStatus.OK.value());
			StringBuilder urlForward = new StringBuilder(
					URL + "api/client/data-crawls/?page=0&size=15&hashtagRunningHistoryId="+hashtagRunningHistory.getId());
//
//			ParameterCrawlDataPageResponse parameterCrawlDataPageResponse = new ParameterCrawlDataPageResponse();
//			parameterCrawlDataPageResponse.setPage(1);
//			parameterCrawlDataPageResponse.setSize(15);
//			parameterCrawlDataPageResponse.setDate(currentDate.toString());
//			parameterCrawlDataPageResponse.setUrl(urlForward.toString());
//			parameterCrawlDataPageResponse.setHashtag(hashtagName);

			messageResponse.setData(urlForward);

			return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
		}

		createCrawlJob(hashtagName, transactionPackageId, client);

		messageResponse.setMessage(Validation.WAITING_A_FEW_MINUTES);
		messageResponse.setStatus(HttpStatus.OK.value());

		return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
	}

	private HashtagRunningHistory createHashtagRunningHistory(Date currentDate, Client client, int transactionPackageId,
			Hashtag hashtag) {
		StringBuilder hashtagRunningHistoryId = new StringBuilder(
				currentDate.toString().replace(" ", "") + "_" + client.getId().toString());

		TransactionPackage transactionPackage = transactionPackageRepository.getById(transactionPackageId);
		
		Package packageOfClient = packageRepository.getById(transactionPackage.getParentPackage().getId());

		HashtagRunningHistory hashtagRunningHistory = new HashtagRunningHistory();

		hashtagRunningHistory.setId(hashtagRunningHistoryId.toString());
		hashtagRunningHistory.setClient(client);
		hashtagRunningHistory.setCrawlQuantity(packageOfClient.getNumberOfPostInEachSearch());
		hashtagRunningHistory.setType(JobConstants.SEARCH);
		hashtagRunningHistory.setTransactionPackage(transactionPackage);
		hashtagRunningHistory.setStatus(JobConstants.SUCCESS);
		hashtagRunningHistory.setRunningTime(currentDate);
		hashtagRunningHistory.setHashtag(hashtag);

		hashtagRunningHistoryRepository.save(hashtagRunningHistory);

		return hashtagRunningHistory;
	}

	@Override
	public PagedResponse<DataCrawlResponse> findAllDataCrawlsByHashtagRunningHistoryId(int page, int size,
			String hashtagRunningHistoryId) {

		if (page > 10) {
			page = 10;
		}

		Pageable pageable = PageRequest.of(page, size);

		Optional<HashtagRunningHistory> hashtagRunningHistory = hashtagRunningHistoryRepository
				.findById(hashtagRunningHistoryId);

		if (hashtagRunningHistory.isEmpty()) {
			return new PagedResponse<>();
		}

		Client client = clientRepository.findByEmail(securityAuditorAware.getCurrentAuditor().get()).get();

		if (!client.getId().toString().equals(hashtagRunningHistory.get().getClient().getId().toString())) {
			return new PagedResponse<>();
		}

		if (JobConstants.SUCCESS.equals(hashtagRunningHistory.get().getStatus())) {

			Page<DataCrawl> dataCrawls = dataCrawlRepository
					.findAllByCreatedDatePostLessThanEqualAndHashtagOrderByCreatedDatePostDesc(pageable,
							getRunningTime(hashtagRunningHistory.get().getRunningTime()),
							hashtagRunningHistory.get().getHashtag());

			List<DataCrawlResponse> crawlResponses = new ArrayList<DataCrawlResponse>(dataCrawls.getContent().size());

			for (DataCrawl dataCrawl : dataCrawls.getContent()) {
				crawlResponses.add(createDataCrawlsResponse(dataCrawl));
			}

			return new PagedResponse<>(crawlResponses, dataCrawls.getNumber(), dataCrawls.getSize(),
					dataCrawls.getTotalElements(), dataCrawls.getTotalPages(), dataCrawls.isLast());
		}

		return new PagedResponse<>();
	}

	@Override
	public PagedResponse<DataCrawlResponse> exportDataCrawls(String hashtagRunningHistoryId) {

		Optional<HashtagRunningHistory> hashtagRunningHistory = hashtagRunningHistoryRepository
				.findById(hashtagRunningHistoryId);

		if (hashtagRunningHistory.isEmpty()) {
			return new PagedResponse<>();
		}

		Pageable pageable = PageRequest.of(0, hashtagRunningHistory.get().getCrawlQuantity());

		Client client = clientRepository.findByEmail(securityAuditorAware.getCurrentAuditor().get()).get();

		if (!client.getId().toString().equals(hashtagRunningHistory.get().getClient().getId().toString())) {
			return new PagedResponse<>();
		}

		if (JobConstants.SUCCESS.equals(hashtagRunningHistory.get().getStatus())) {

			Page<DataCrawl> dataCrawls = dataCrawlRepository
					.findAllByCreatedDatePostLessThanEqualAndHashtagOrderByCreatedDatePostDesc(pageable,
							getRunningTime(hashtagRunningHistory.get().getRunningTime()),
							hashtagRunningHistory.get().getHashtag());

			List<DataCrawlResponse> crawlResponses = new ArrayList<DataCrawlResponse>(dataCrawls.getContent().size());

			for (DataCrawl dataCrawl : dataCrawls.getContent()) {
				crawlResponses.add(createDataCrawlsResponse(dataCrawl));
			}

			return new PagedResponse<>(crawlResponses, dataCrawls.getNumber(), dataCrawls.getSize(),
					dataCrawls.getTotalElements(), dataCrawls.getTotalPages(), dataCrawls.isLast());
		}

		return new PagedResponse<>();
	}

}
