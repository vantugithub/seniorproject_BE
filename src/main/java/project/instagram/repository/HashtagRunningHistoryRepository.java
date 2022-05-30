package project.instagram.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.instagram.entity.Client;
import project.instagram.entity.HashtagRunningHistory;

@Repository
public interface HashtagRunningHistoryRepository extends JpaRepository<HashtagRunningHistory, String> {
	Page<HashtagRunningHistory> findAllByClientOrderByRunningTimeDesc(Client client, Pageable pageable);
}
