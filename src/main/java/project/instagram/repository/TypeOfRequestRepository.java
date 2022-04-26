package project.instagram.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.instagram.entity.TypeOfRequest;

@Repository
public interface TypeOfRequestRepository extends JpaRepository<TypeOfRequest, UUID>{

}
