package project.instagram.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.instagram.entity.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID>{

}
