package project.instagram;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import project.instagram.entity.Client;
import project.instagram.entity.Hashtag;
import project.instagram.entity.HashtagClientManagement;
import project.instagram.entity.TransactionPackage;
import project.instagram.repository.BlackHashtagRepository;
import project.instagram.repository.ClientRepository;
import project.instagram.repository.HashtagClientManagementRepository;
import project.instagram.repository.HashtagRepository;
import project.instagram.repository.TransactionPackageRepository;
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

	@Test
	@Transactional
	void contextLoads() {
	}
	
	@Test
	void test() {
	}
	
	

}
