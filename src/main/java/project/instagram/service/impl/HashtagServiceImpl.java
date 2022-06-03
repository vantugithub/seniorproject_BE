package project.instagram.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import project.instagram.common.enums.constants.HashtagConstants;
import project.instagram.entity.Client;
import project.instagram.entity.DateRange;
import project.instagram.entity.Hashtag;
import project.instagram.entity.HashtagClientManagement;
import project.instagram.entity.Package;
import project.instagram.entity.RunningSummary;
import project.instagram.entity.TransactionPackage;
import project.instagram.entity.TypeOfPackage;
import project.instagram.repository.BlackHashtagRepository;
import project.instagram.repository.ClientRepository;
import project.instagram.repository.HashtagClientManagementRepository;
import project.instagram.repository.HashtagRepository;
import project.instagram.repository.RunningSummaryRepository;
import project.instagram.repository.TransactionPackageRepository;
import project.instagram.repository.TypeOfPackageRepository;
import project.instagram.response.MessageResponse;
import project.instagram.security.SecurityAuditorAware;
import project.instagram.service.HashtagServive;
import project.instagram.utils.DateTimeZoneUtils;

@Service
public class HashtagServiceImpl implements HashtagServive {

	private static final String PACKAGE_TYPE = "Package";

	private static final String EXTRA_PACKAGE_TYPE = "Extra package";

	@Autowired
	private HashtagRepository hashtagRepository;

	@Autowired
	private BlackHashtagRepository blackHashtagRepository;

	@Autowired
	private HashtagClientManagementRepository hashtagClientManagementRepository;

	@Autowired
	private SecurityAuditorAware securityAuditorAware;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private DateTimeZoneUtils dateTimeZoneUtils;

	@Autowired
	private TransactionPackageRepository transactionPackageRepository;

	@Autowired
	private TypeOfPackageRepository typeOfPackageRepository;

	@Autowired
	private RunningSummaryRepository runningSummaryRepository;

	private MessageResponse createHashtagByClient(String hashtagName, Client client) {
		MessageResponse messageResponse = new MessageResponse();
		Hashtag createHashtagByClient = hashtagRepository.save(new Hashtag(hashtagName));
		hashtagClientManagementRepository.save(new HashtagClientManagement(client, createHashtagByClient));
		messageResponse.setData(hashtagName);
		messageResponse.setMessage(HashtagConstants.CREATED_HASHTAG_SUCCESSFULLY);
		messageResponse.setStatus(HttpStatus.OK.value());

		return messageResponse;
	}

	private MessageResponse deleteHashtagByClient(Client client, Hashtag hashtag) {
		MessageResponse messageResponse = new MessageResponse();
		hashtagClientManagementRepository.deleteHashtagClientManagementByClientAndHashtag(client, hashtag);
		messageResponse.setMessage(HashtagConstants.DELETED_HASHTAG_SUCCESSFULLY);
		messageResponse.setStatus(HttpStatus.OK.value());

		return messageResponse;
	}

	private boolean existsBlackHashtagByName(String hashtagName) {
		return blackHashtagRepository.existsById(hashtagName);
	}

	private boolean existsHashtagClientManagementByClientAndHashtag(Client client, Hashtag hashtag) {
		if (hashtagClientManagementRepository.getHashtagClientManagementByClientAndHashtag(client, hashtag) == null)
			return true;

		return false;
	}

	Optional<TypeOfPackage> findTypeOfPackage(Package packageFromTransactionPackage) {

		UUID packageTypeUUID = UUID.fromString(packageFromTransactionPackage.getTypeOfPackage().getId().toString());
		Optional<TypeOfPackage> typeOfPackage = typeOfPackageRepository.findById(packageTypeUUID);

		return typeOfPackage;
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

	RunningSummary createNewRunningSummary(Client client, TransactionPackage transactionPackage, DateRange dateRange,
			Optional<TypeOfPackage> typeOfPackage) {

		RunningSummary runningSummary = new RunningSummary();
		runningSummary.setClient(client);
		runningSummary.setIssuedDate(dateRange.getStartDate());
		runningSummary.setExpiredDate(dateRange.getEndDate());
		runningSummary.setTransactionPackage(transactionPackage);

		if (PACKAGE_TYPE.equals(typeOfPackage.get().getName())) {
			runningSummary.setCrawledPackageQuantity((byte) 1);
		}

		if (EXTRA_PACKAGE_TYPE.equals(typeOfPackage.get().getName())) {
			runningSummary.setCrawledExtraPackageQuantity((byte) 1);
		}

		return runningSummary;
	}

	boolean isDateInRange(Date date, DateRange dateRange) {
		System.out.println(dateRange.getStartDate());
		System.out.println(dateRange.getEndDate());
		if (date.after(dateRange.getStartDate()) && date.before(dateRange.getEndDate())) {
			return true;
		}

		return false;
	}

	@SuppressWarnings("deprecation")
	Date createPointOfTime(Date oldDate, Date currentDate) {
		Date newDate = oldDate;
		newDate.setMonth(currentDate.getMonth());

		return newDate;
	}

	Date createDateOfRunningSummary(int month, Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, month);

		return calendar.getTime();
	}

	private Boolean enableCrawlHashtagByClient(Hashtag hashtag, Date crawlDate) {

		Client client = clientRepository.findByEmail(securityAuditorAware.getCurrentAuditor().get()).get();
		Date currentDate = dateTimeZoneUtils.getLocalDateTime();

		Set<TransactionPackage> setValidTransactionPackage = transactionPackageRepository
				.findAllValidTransactionPackages(client, crawlDate);

		if (setValidTransactionPackage.size() == 0) {
			return false;
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

				if (isDateInRange(crawlDate, dateRange)) {
					RunningSummary runningSummary = createNewRunningSummary(client, transactionPackage, dateRange,
							typeOfPackage);
					runningSummaryRepository.save(runningSummary);
					enableCrawlHashtag(hashtag, client, crawlDate,
							packageFromTransactionPackage.getNumberOfPostsPerHashtag(), transactionPackage);

					return true;
				}

				continue;
			}

			if (!isDateInRange(crawlDate, new DateRange(validRunningSummary.get().getIssuedDate(),
					validRunningSummary.get().getExpiredDate()))) {
				continue;
			}

			if (!isDateInRange(currentDate, new DateRange(validRunningSummary.get().getIssuedDate(),
					validRunningSummary.get().getExpiredDate()))) {
				continue;
			}

			byte remainingQuantityCrawlHashtag = calculateRemainingQuantityCrawlHashtag(typeOfPackage,
					packageFromTransactionPackage.getCrawlQuantity(), validRunningSummary.get());

			if (remainingQuantityCrawlHashtag == 0) {
				continue;
			}

			validRunningSummary.get().increaseQuantityCrawl(typeOfPackage.get(), 1);
			runningSummaryRepository.save(validRunningSummary.get());
			enableCrawlHashtag(hashtag, client, crawlDate, packageFromTransactionPackage.getNumberOfPostsPerHashtag(),
					transactionPackage);

			return true;
		}

		return false;
	}

	private byte calculateRemainingQuantityCrawlHashtag(Optional<TypeOfPackage> typeOfPackage,
			byte quantityCrawlOfPackage, RunningSummary runningSummary) {

		if (PACKAGE_TYPE.equals(typeOfPackage.get().getName())) {
			return (byte) (quantityCrawlOfPackage - runningSummary.getCrawledPackageQuantity());
		}

		return (byte) (quantityCrawlOfPackage - runningSummary.getCrawledExtraPackageQuantity());
	}

	@SuppressWarnings("deprecation")
	private void enableCrawlHashtag(Hashtag hashtag, Client client, Date dateCrawl, short crawlQuantity,
			TransactionPackage transactionPackage) {

		HashtagClientManagement hashtagClientManagement = hashtagClientManagementRepository
				.findByClientManagementAndHashtagClientManagement(client, hashtag);

//		HashtagClientManagement hashtagClientManagement = new HashtagClientManagement();
		hashtagClientManagement.setActive(true);
//		hashtagClientManagement.setClientManagement(client);
//		hashtagClientManagement.setHashtagClientManagement(hashtag);
		hashtagClientManagement.setCrawlQuantity(crawlQuantity);
		dateCrawl.setHours(-24);
		hashtagClientManagement.setDateStartCrawl(dateCrawl);
		hashtagClientManagement.setTransactionPackage(transactionPackage);

		hashtagClientManagementRepository.save(hashtagClientManagement);

	}

	@Override
	public ResponseEntity<MessageResponse> createHashtag(String hashtagName) {
		String cleanHashtagName = hashtagName.replace(" ", "").toLowerCase();
		MessageResponse messageResponse = new MessageResponse();
		messageResponse.setData(cleanHashtagName);

		Optional<Hashtag> hashTag = hashtagRepository.findById(cleanHashtagName);
		Client client = clientRepository.findByEmail(securityAuditorAware.getCurrentAuditor().get()).get();

		if (hashTag.isEmpty()) {
			messageResponse = createHashtagByClient(cleanHashtagName, client);
			return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
		}

		if (existsBlackHashtagByName(cleanHashtagName)) {
			messageResponse.setMessage(HashtagConstants.HASHTAG_IN_BLACK_LIST_HASHTAGS);
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}

		if (existsHashtagClientManagementByClientAndHashtag(client, hashTag.get())) {
			hashtagClientManagementRepository.save(new HashtagClientManagement(client, hashTag.get()));
			messageResponse.setMessage(HashtagConstants.CREATED_HASHTAG_SUCCESSFULLY);
			messageResponse.setStatus(HttpStatus.OK.value());

			return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
		}
		messageResponse.setMessage(HashtagConstants.HASHTAG_EXISTS);
		messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
	}

	@Override
	public ResponseEntity<MessageResponse> deleteHashtag(String hashtagName) {
		MessageResponse messageResponse = new MessageResponse();
		Optional<Hashtag> hashTag = hashtagRepository.findById(hashtagName);
		Client client = clientRepository.findByEmail(securityAuditorAware.getCurrentAuditor().get()).get();

		if (hashTag.isEmpty()) {
			messageResponse.setMessage(HashtagConstants.HASHTAG_NOT_EXISTS);
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}
		if (existsHashtagClientManagementByClientAndHashtag(client, hashTag.get())) {
			messageResponse.setMessage(HashtagConstants.DELETED_HASHTAG_FAILED);
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}
		messageResponse = deleteHashtagByClient(client, hashTag.get());

		return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
	}

	@Override
	public ResponseEntity<MessageResponse> enableCrawlHashtagByClient(String hashtagName, String dateCrawlStr) {

		MessageResponse messageResponse = new MessageResponse();
		Optional<Hashtag> existsHashtag = hashtagRepository.findById(hashtagName);

		Date dateCrawl = dateTimeZoneUtils.formatDateTime(dateCrawlStr);

		if (existsHashtag.isEmpty()) {
			messageResponse.setMessage(HashtagConstants.HASHTAG_NOT_EXISTS);
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}

		if (!enableCrawlHashtagByClient(existsHashtag.get(), dateCrawl)) {
			messageResponse.setMessage(HashtagConstants.ENABLED_CRAWL_HASHTAG_FAILED);
			messageResponse.setStatus(HttpStatus.BAD_REQUEST.value());

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
		}
		messageResponse.setMessage(HashtagConstants.ENABLED_CRAWL_HASHTAG_SUCCESSFULLY);
		messageResponse.setStatus(HttpStatus.OK.value());

		return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
	}

}
