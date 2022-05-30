package project.instagram;

import static org.mockito.ArgumentMatchers.anyList;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import project.instagram.common.enums.constants.PackageConstants;
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
	void test7( ) {
		Date currentDate = dateTimeZoneUtils.getDateZoneGMT();
		System.out.println(currentDate);
		Set<HashtagClientManagement> hashtagClientManagements = hashtagClientManagementRepository.findAllByDateStartCrawlAndActiveTrue(currentDate);
		
		if ( hashtagClientManagements.size() == 0 ) {
			System.out.println("none");
		} else {
			System.out.println(hashtagClientManagements.size());
		}
	}
	
	@Test
	void test8() {
		Client client = clientRepository.findByEmail("nguyenvantu11041999@gmail.com").get();
		Date currentDate = dateTimeZoneUtils.getDateTimeZoneGMT();
		
		TransactionPackage transactionPackage = transactionPackageRepository.findById(21).get();

		Optional<RunningSummary> runn = runningSummaryRepository.findDetailsTransactionPackage(currentDate, client, transactionPackage);
		
		Package packageOfTransactionPackage = packageRepository.findById(transactionPackage.getParentPackage().getId()).get();
		System.out.println(packageOfTransactionPackage.getName());
	}

}
