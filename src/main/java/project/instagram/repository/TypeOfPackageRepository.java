package project.instagram.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.instagram.entity.TypeOfPackage;

@Repository
public interface TypeOfPackageRepository extends JpaRepository<TypeOfPackage, UUID> {
	
	public boolean existsByName(String name);
	public Optional<TypeOfPackage> findByName(String name);
}
