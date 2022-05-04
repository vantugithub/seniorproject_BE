package project.instagram;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.transaction.Transactional;

import org.apache.naming.TransactionRef;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import project.instagram.common.enums.RequestName;
import project.instagram.common.enums.StatusRequestName;
import project.instagram.common.enums.constants.PackageConstants;
import project.instagram.entity.Client;
import project.instagram.entity.Hashtag;
import project.instagram.entity.HashtagClientManagement;
import project.instagram.entity.Package;
import project.instagram.entity.Request;
import project.instagram.entity.StatusOfRequest;
import project.instagram.entity.TransactionPackage;
import project.instagram.entity.TypeOfPackage;
import project.instagram.entity.TypeOfRequest;
import project.instagram.repository.BlackHashtagRepository;
import project.instagram.repository.ClientRepository;
import project.instagram.repository.HashtagClientManagementRepository;
import project.instagram.repository.HashtagRepository;
import project.instagram.repository.PackageRepository;
import project.instagram.repository.RequestRepository;
import project.instagram.repository.TransactionPackageRepository;
import project.instagram.repository.TypeOfPackageRepository;
import project.instagram.response.PackageResponse;
import project.instagram.utils.DateTimeZoneUtils;

@SpringBootTest
class InstagramApplicationTests {
	
	@Autowired
	private HashtagRepository hashtagRepository;
	
	@Autowired
	private DateTimeZoneUtils dateTimeZoneUtils;
	
	@Autowired
	private TransactionPackageRepository transactionPackageRepository;
	
	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired BlackHashtagRepository blackHashtagRepository;
	
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
		Set<TransactionPackage> list = transactionPackageRepository.
				findAllByExpiredDateGreaterThanEqualOrExpiredDateNullAndClient(currentDate, client);
		
		for (TransactionPackage transactionPackage : list) {
			project.instagram.entity.Package packageOfClient = transactionPackage.getParentPackage();
			TypeOfPackage ofPackage = typeOfPackageRepository.findById(packageOfClient.getTypeOfPackage().getId()).get();
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
		Optional<TransactionPackage> optional = 
				transactionPackageRepository.findByExpiredDateGreaterThanEqualAndClientAndChildPackage(
						"Package", uuid.toString(), currentDate);
		
		System.out.println(optional.get().toString());
		
		
	}
	
	

}
