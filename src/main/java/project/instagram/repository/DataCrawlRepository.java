package project.instagram.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.instagram.entity.DataCrawl;
import project.instagram.entity.Hashtag;

@Repository
public interface DataCrawlRepository extends JpaRepository<DataCrawl, String> {
	List<DataCrawl> findAllByCreatedDatePostLessThanEqualAndHashtagOrderByCreatedDatePostDesc(Date date,
			Hashtag hashtag);

	List<DataCrawl> findAllByCreatedDatePostGreaterThanEqualAndHashtagOrderByCreatedDatePostDesc(Date date,
			Hashtag hashtag);

	Page<DataCrawl> findAllByCreatedDatePostLessThanEqualAndHashtagOrderByCreatedDatePostDesc(Pageable pageable,
			Date date, Hashtag hashtag);
	
	Optional<DataCrawl> findFirstByCreatedDatePostLessThanEqualAndHashtagOrderByCreatedDatePostDesc(Date date, Hashtag hashtag);
}
