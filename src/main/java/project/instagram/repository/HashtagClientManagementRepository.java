package project.instagram.repository;

import java.util.Date;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import project.instagram.entity.Client;
import project.instagram.entity.Hashtag;
import project.instagram.entity.HashtagClientManagement;

@Repository
@Transactional
public interface HashtagClientManagementRepository extends JpaRepository<HashtagClientManagement, Long> {
	
	@Query(value = "SELECT h.id, h.active, h.crawl_quantity, h.date_start_crawl, h.client, h.hashtag "
			+ "FROM hashtag_client_managements h WHERE client = ?1 AND hashtag = ?2", nativeQuery = true)
	public HashtagClientManagement getHashtagClientManagementByClientAndHashtag(Client client, Hashtag hashtag);
	
	@Modifying
	@Query(value = "DELETE FROM hashtag_client_managements WHERE client = ?1 AND hashtag = ?2", nativeQuery = true)
	public void deleteHashtagClientManagementByClientAndHashtag(Client client, Hashtag hashtag);

	public Set<HashtagClientManagement> findAllByDateStartCrawlAndActiveTrue(Date dateStartCrawl);
}
