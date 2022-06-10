package project.instagram.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.instagram.entity.Analysis;
import project.instagram.entity.Hashtag;

@Repository
public interface AnalysisRepository extends JpaRepository<Analysis, Integer> {
	Page<Analysis> findAllByHashtagAndDateOfAnalysisOrderByIdDesc(Hashtag hashtag, String date, Pageable pageable);

}
