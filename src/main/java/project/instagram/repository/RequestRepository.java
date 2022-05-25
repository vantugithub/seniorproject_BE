package project.instagram.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.instagram.entity.Request;
import project.instagram.entity.StatusOfRequest;

@Repository
public interface RequestRepository extends JpaRepository<Request, String> {
	Page<Request> findAllByStatusOfRequestNotOrderByCreatedDateDesc(StatusOfRequest statusOfRequest, Pageable pageable);
	Page<Request> findAllByStatusOfRequestOrderByCreatedDateDesc(StatusOfRequest statusOfRequest, Pageable pageable);
}
