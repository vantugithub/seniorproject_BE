package project.instagram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.instagram.entity.Request;

@Repository
public interface RequestRepository extends JpaRepository<Request, String> {

}
