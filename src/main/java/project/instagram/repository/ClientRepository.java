package project.instagram.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.instagram.entity.Client;
import project.instagram.entity.Role;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID>{

	Optional<Client> findByEmailAndActiveTrueAndRole(String email, Role role);
	Optional<Client> findByEmail(String email);
	Page<Client> findAllByOrderByIdDesc(Pageable pageable);
	
}
