package project.instagram.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import project.instagram.entity.BlackHashtag;

public interface BlackHashtagRepository extends JpaRepository<BlackHashtag, String>{
	boolean existsByName(String name);
	
	Page<BlackHashtag> findAll(Pageable pageable);
}
