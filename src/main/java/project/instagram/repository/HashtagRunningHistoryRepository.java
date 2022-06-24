package project.instagram.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import project.instagram.entity.Client;
import project.instagram.entity.Hashtag;
import project.instagram.entity.HashtagRunningHistory;

@Repository
@Transactional
public interface HashtagRunningHistoryRepository extends JpaRepository<HashtagRunningHistory, String> {
	Page<HashtagRunningHistory> findAllByClientOrderByRunningTimeDesc(Client client, Pageable pageable);

	Page<HashtagRunningHistory> findAllByClientAndHashtagOrderByRunningTimeDesc(Client client, Hashtag hashtag,
			Pageable pageable);

	@Query(value = "select h.id, h.running_time, h.status, h.type, h.client, " + "h.hashtag, h.transaction_package, "
			+ "h.crawl_quantity FROM hashtag_running_histories h WHERE hashtag = ?1 "
			+ "AND running_time >= ?2 AND running_time <= ?3 AND client = ?4", nativeQuery = true)
	List<HashtagRunningHistory> findAllByHashtagAndPriodOfTime(String hashtagStr, Date startDate, Date endDate,
			Client client);

	@Query(value = "SELECT h.id, h.running_time, h.status, h.type, h.client, h.hashtag, h.transaction_package, "
			+ "h.crawl_quantity FROM hashtag_running_histories h WHERE "
			+ "h.running_time >= ?1 and h.running_time < ?2", nativeQuery = true)
	List<HashtagRunningHistory> getAllHashtagsByPeriodOfTime(Date startDate, Date endDate);
}
