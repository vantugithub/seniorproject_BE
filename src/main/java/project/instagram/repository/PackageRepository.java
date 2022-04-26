package project.instagram.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.instagram.entity.Package;

@Repository
public interface PackageRepository extends JpaRepository<Package, UUID>{
	Optional<Package> findPackageByName(String packageName);
 }
