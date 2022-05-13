package project.instagram;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
import project.instagram.repository.PackageRepository;
import project.instagram.repository.RequestRepository;
import project.instagram.repository.RunningSummaryRepository;
import project.instagram.repository.TransactionPackageRepository;
import project.instagram.repository.TypeOfPackageRepository;
import project.instagram.utils.DateTimeZoneUtils;

@SpringBootTest
class InstagramApplicationTests {

	private static final String PACKAGE_TYPE = "Package";

	private static final String EXTRA_PACKAGE_TYPE = "Extra package";

	@Autowired
	private HashtagRepository hashtagRepository;

	@Autowired
	private RunningSummaryRepository runningSummaryRepository;

	@Autowired
	private DateTimeZoneUtils dateTimeZoneUtils;

	@Autowired
	private TransactionPackageRepository transactionPackageRepository;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	BlackHashtagRepository blackHashtagRepository;

	@Autowired
	private HashtagClientManagementRepository hashtagClientManagementRepository;

	@Autowired
	private RequestRepository requestRepository;

	@Autowired
	private PackageRepository packageRepository;

	@Autowired
	private TypeOfPackageRepository typeOfPackageRepository;

	@Test
	void contextLoads() {
		UUID uuid = UUID.fromString("bf61d5ba-79c0-495a-bc73-4ac4b51c4b21");
		Client client = clientRepository.findById(uuid).get();
		Date currentDate = dateTimeZoneUtils.getDateTimeZoneGMT();
//		Optional<TransactionPackage> existsValidPackageOfClient =  transactionPackageRepository
//				.findByExpiredDateGreaterThanEqualAndClientAndActiveTrue(currentDate, client);
//		System.out.println(existsValidPackageOfClient.get().toString());
//		Request newRequest = new Request();
//		StringBuilder newRequestId = new StringBuilder(currentDate.toString().replace(" ", "")+"_"+client.getId().toString());
//		newRequest.setId(newRequestId.toString());
//		newRequest.setTitle(PackageConstants.UPGRADE_PACKAGE);
//		newRequest.setClientRequest(client);
//		StatusOfRequest statusOfRequest = new StatusOfRequest(StatusRequestName.PENDING);
//		newRequest.setStatusOfRequest(statusOfRequest);
//		TypeOfRequest typeOfRequest = new TypeOfRequest(RequestName.SYSTEM_REQUEST);
//		newRequest.setTypeOfRequest(typeOfRequest);
//		
//		newRequest = requestRepository.save(newRequest);
	}

	@Test
	void test() {
		UUID uuid = UUID.fromString("bf61d5ba-79c0-495a-bc73-4ac4b51c4b21");
		Client client = clientRepository.findById(uuid).get();
		Date currentDate = dateTimeZoneUtils.getDateTimeZoneGMT();
		Set<TransactionPackage> list = transactionPackageRepository
				.findAllByClientAndExpiredDateGreaterThanEqualOrExpiredDateNullOrderByExpiredDateAsc(client,
						currentDate);

		for (TransactionPackage transactionPackage : list) {
			project.instagram.entity.Package packageOfClient = transactionPackage.getParentPackage();
			TypeOfPackage ofPackage = typeOfPackageRepository.findById(packageOfClient.getTypeOfPackage().getId())
					.get();
			if (ofPackage.getName().equals("Package")) {
				System.out.println("ok");
			} else {
				System.out.println("dell ok");
			}
		}
	}

	@Test
	void test1() {
		UUID packageUUID = UUID.fromString("01fd653a-630d-4294-98c4-aba211807676");
		Package package2 = packageRepository.findById(packageUUID).get();
		UUID uuid = UUID.fromString("bf61d5ba-79c0-495a-bc73-4ac4b51c4b21");
		Client client = clientRepository.findById(uuid).get();
		Date currentDate = dateTimeZoneUtils.getDateTimeZoneGMT();
//		TypeOfPackage typeOfPackage = typeOfPackageRepository.findByName("Extra Package").get();
//		Package package1 = packageRepository.findPackageByIdAndTypeOfPackage(packageUUID, typeOfPackage).get();
		Optional<TransactionPackage> optional = transactionPackageRepository
				.findByExpiredDateGreaterThanEqualAndClientAndChildPackage("Package", uuid.toString(), currentDate);

		System.out.println(optional.get().toString());

	}

	@Test
	void test2() {
		UUID uuid = UUID.fromString("bf61d5ba-79c0-495a-bc73-4ac4b51c4b21");
		Client client = clientRepository.findById(uuid).get();

		String startDateString = "2022/07/02";
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		Date startDate = null;
		try {
			startDate = df.parse(startDateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Set<TransactionPackage> list = transactionPackageRepository
				.findAllByClientAndExpiredDateGreaterThanEqualOrExpiredDateNullOrderByExpiredDateAsc(client, startDate);

		for (TransactionPackage transactionPackage : list) {
			if (transactionPackage.getExpiredDate() == null) {
				System.out.println("is Null");
			} else {
				System.out.println(transactionPackage.getExpiredDate().toString());
			}

		}
	}

	@Test
	void test3() {

		UUID uuid = UUID.fromString("09ee87aa-9542-4e92-aa41-0e73205a34e8");
		Client client = clientRepository.findById(uuid).get();
		
		Optional<Hashtag> hashtag = hashtagRepository.findById("netflix");
		
		String startDateString = "2022/05/12";
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		Date startDate = null;
		try {
			startDate = df.parse(startDateString);

		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (enableHashtagByClient(client, startDate, hashtag.get())) {
			System.out.println(startDateString.toString());
			System.out.println("success");
		} else {
			System.out.println("failed");
		}
	}

	void enableCrawlHashtag(Hashtag hashtag, Date dateCrawl, short crawlQuantity, Client client) {

		HashtagClientManagement hashtagClientManagement = new HashtagClientManagement();
		hashtagClientManagement.setActive(true);
		hashtagClientManagement.setClientManagement(client);
		hashtagClientManagement.setHashtagClientManagement(hashtag);
		hashtagClientManagement.setCrawlQuantity(crawlQuantity);
		hashtagClientManagement.setDateStartCrawl(dateCrawl);

		hashtagClientManagementRepository.save(hashtagClientManagement);

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

	Boolean enableHashtagByClient(Client client, Date crawlDate, Hashtag hashtag) {

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
					enableCrawlHashtag(hashtag, crawlDate, packageFromTransactionPackage.getNumberOfPostsPerHashtag(), client);
					
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
			enableCrawlHashtag(hashtag, crawlDate, packageFromTransactionPackage.getNumberOfPostsPerHashtag(), client);
			
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

	@Test
	void test4() {
		UUID uuid = UUID.fromString("09ee87aa-9542-4e92-aa41-0e73205a34e8");
		Client client = clientRepository.findById(uuid).get();

		String startDateString = "2022/05/10";
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		Date startDate = null;
		try {
			startDate = df.parse(startDateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Set<TransactionPackage> setValidTransactionPackage = transactionPackageRepository
				.findAllByClientAndExpiredDateGreaterThanEqualOrExpiredDateNullOrderByExpiredDateAsc(client, startDate);
	}

	@Test
	void test5() {
		UUID uuid = UUID.fromString("66dd1b6a-dd1c-4d6e-ae7a-51886d337a7a");
		RunningSummary runningSummary = runningSummaryRepository.findById(uuid).get();

		System.out.println(runningSummary.toString());

	}

	@Test
	void test6() {
		UUID uuid = UUID.fromString("09ee87aa-9542-4e92-aa41-0e73205a34e8");
		Client client = clientRepository.findById(uuid).get();

		System.out.println(client.toString());
	}
	
	@Test
	void test7( ) {
		Date currentDate = dateTimeZoneUtils.getDateTimeZoneGMT();
		UUID uuid = UUID.fromString("09ee87aa-9542-4e92-aa41-0e73205a34e8");
		TransactionPackage transactionPackage = transactionPackageRepository.
				findByExpiredDateGreaterThanEqualAndClientAndChildPackage(PACKAGE_TYPE, uuid.toString(), currentDate).get();
		
		System.out.println(transactionPackage.getParentPackage().getId().toString());
	}

}
