package project.instagram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import project.instagram.entity.Hashtag;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, String> {
	public boolean existsByName(String name);
	
	@Query(value = "SELECT h.name, h.issued_date FROM hashtags h WHERE h.name = ?1", nativeQuery = true)
	public Hashtag getHashtagByName(String name);
}
