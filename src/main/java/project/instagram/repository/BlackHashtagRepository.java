package project.instagram.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import project.instagram.entity.BlackHashtag;

public interface BlackHashtagRepository extends JpaRepository<BlackHashtag, String>{
	public boolean existsByName(String name);
}
