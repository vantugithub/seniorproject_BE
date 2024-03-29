package project.instagram.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.instagram.entity.Role;
import project.instagram.entity.Staff;

@Repository
public interface StaffRepository extends JpaRepository<Staff,UUID>{
	
	Optional<Staff> findByEmailAndActiveTrueAndRoleNot(String email, Role role);
	Optional<Staff> findByEmailAndActiveTrue(String email);
	Page<Staff> findAllByRoleOrderByCreatedDateDesc(Role role, Pageable pageable);
	Optional<Staff> findByEmail(String email);
}
