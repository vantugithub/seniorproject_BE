package project.instagram;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import project.instagram.common.enums.constants.PackageConstants;
import project.instagram.entity.Analysis;
import project.instagram.entity.Client;
import project.instagram.entity.DataCrawl;
import project.instagram.entity.Hashtag;
import project.instagram.entity.HashtagClientManagement;
import project.instagram.entity.HashtagRunningHistory;
import project.instagram.entity.Package;
import project.instagram.entity.RunningSummary;
import project.instagram.entity.TransactionPackage;
import project.instagram.entity.TypeOfPackage;
import project.instagram.repository.AnalysisRepository;
import project.instagram.repository.BlackHashtagRepository;
import project.instagram.repository.ClientRepository;
import project.instagram.repository.DataCrawlRepository;
import project.instagram.repository.HashtagClientManagementRepository;
import project.instagram.repository.HashtagRepository;
import project.instagram.repository.HashtagRunningHistoryRepository;
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

	private static final int HashMap = 0;

	@Autowired
	private HashtagRepository hashtagRepository;

	@Autowired
	private AnalysisRepository analysisRepository;

	@Autowired
	private HashtagRunningHistoryRepository hashtagRunningHistoryRepository;

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

	@Autowired
	private DataCrawlRepository dataCrawlRepository;

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
	void test7() {
		Date currentDate = dateTimeZoneUtils.getDateZoneGMT();
		System.out.println(currentDate);
		Set<HashtagClientManagement> hashtagClientManagements = hashtagClientManagementRepository
				.findAllByDateStartCrawlAndActiveTrue(currentDate);

		if (hashtagClientManagements.size() == 0) {
			System.out.println("none");
		} else {
			System.out.println(hashtagClientManagements.size());
		}
	}

	@Test
	void test8() {
		Optional<HashtagRunningHistory> hashtagRunningHistory = hashtagRunningHistoryRepository
				.findById("2022-05-3005:14:51.785767_09ee87aa-9542-4e92-aa41-0e73205a34e8");

		System.out.println(hashtagRunningHistory.get().getRunningTime().toString());

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(hashtagRunningHistory.get().getRunningTime());
		calendar.add(Calendar.HOUR, 18);

		System.out.println(calendar.getTime().toString());

		Hashtag hashtag = hashtagRepository.getById("netflix");

		List<DataCrawl> crawls = dataCrawlRepository
				.findAllByCreatedDatePostLessThanEqualAndHashtagOrderByCreatedDatePostDesc(calendar.getTime(), hashtag);

		System.out.println(crawls.size());

	}

	@Test
	void test9() {
		Date currentDate = dateTimeZoneUtils.getDateZoneGMT();

		Hashtag hashtag = hashtagRepository.findById("netflix").get();

		Optional<DataCrawl> dataCrawl = dataCrawlRepository
				.findFirstByCreatedDatePostLessThanEqualAndHashtagOrderByCreatedDatePostDesc(currentDate, hashtag);

		if ((currentDate.getTime() - dataCrawl.get().getCreatedDatePost().getTime()) / (60 * 60 * 1000) > 48) {
			System.out.println("asojdaishduiasduasbdu");
		}

		System.out.println(dataCrawl.get().toString());
	}

	@Test
	void test10() {
		UUID clientUUID = UUID.fromString("5f19663e-c529-419d-a65b-1aab85d74da9");
		Client client = clientRepository.findById(clientUUID).get();

		Date currentDate = dateTimeZoneUtils.getDateTimeZoneGMT();

		List<TransactionPackage> transactionPackages = transactionPackageRepository
				.findAllValidExtraTransactionPackages(PackageConstants.EXTRA_PACKAGE_TYPE, client.getId().toString(),
						currentDate);

		System.out.println(transactionPackages.size());
	}

	@Test
	void test11() {
		HashtagRunningHistory hashtagRunningHistory = hashtagRunningHistoryRepository
				.findById("2022-06-0610:14:05.5919186_09ee87aa-9542-4e92-aa41-0e73205a34e8").get();

		System.out.println(hashtagRunningHistory.getRunningTime().toGMTString());
		System.out.println(hashtagRunningHistory.getRunningTime().toLocaleString());
		System.out.println(hashtagRunningHistory.getRunningTime().toString());
	}

	@Test
	void test12() {
		
		String startYear = "2022";
		String startDateTimeStr = startYear + "-01-01";

		String endDateTimeStr = String.valueOf(Integer.parseInt(startYear) + (Integer) 1) + "-01-01";

		Date startDate = dateTimeZoneUtils.formatDateTime(startDateTimeStr);
		Date endDate = dateTimeZoneUtils.formatDateTime(endDateTimeStr);

		TypeOfPackage typeOfPackage = typeOfPackageRepository.findByName(PACKAGE_TYPE).get();

		List<Package> packages = packageRepository.findAllByTypeOfPackage(typeOfPackage);

		List<HashMap<String, Integer>> counters = new ArrayList<HashMap<String, Integer>>();

		for (Package pac : packages) {
			List<TransactionPackage> transactionPackages = transactionPackageRepository
					.findAllByPackageAndPeriodOfTime(pac, startDate, endDate);
			HashMap<String, Integer> counter = new HashMap<String, Integer>();
			for (TransactionPackage transactionPackage : transactionPackages) {

				String monthStr = transactionPackage.getIssuedeDate().toString().split("-")[1];

				if (counter.containsKey(monthStr)) {
					counter.put(monthStr, counter.get(monthStr) + 1);
				} else {
					counter.put(monthStr, 1);
				}
				
			}
			counters.add(counter);
		}
		
		for (int i = 0 ; i < counters.size(); i++) {
			HashMap<String, Integer> counter = counters.get(i);
			Set<Map.Entry<String, Integer>> mappingGeeks = counter.entrySet();
	        System.out.println("Set of Keys and Values : "+mappingGeeks);
		}

	}
	
	@Test
	void test13() {
		
		
		Date startDate = dateTimeZoneUtils.formatDateTime("2022" + "-06-01");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		calendar.add(Calendar.MONTH, 1);
		System.out.println(startDate.toString());
		System.out.println(calendar.getTime().toString());
		
	}
	
	@Test
	void test14() {
		String hashtags = "awesomehongkong insidehongkong discoverhongkong hongkong hk hkig cityscape wanderlust followforfollowback travel instagram photooftheday passionpassport fitnesshk photography hkigers fujifilm naturelovers fujifilmglobal moodygrams landscape travelgram lifeofadventure igers fitnessmotivation justgoshoot agameoftones hongkong hikingadventures ";
		String uri = "http://127.0.0.1:5000/classify/"+hashtags;

	    RestTemplate restTemplate = new RestTemplate();
	    @SuppressWarnings("unchecked")
		List<String> result = restTemplate.getForObject(uri, List.class);

	    System.out.println(result.toString());

	}

}
