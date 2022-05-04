package project.instagram.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.instagram.entity.Package;
import project.instagram.entity.TypeOfPackage;

@Repository
public interface PackageRepository extends JpaRepository<Package, UUID>{
	Optional<Package> findPackageByName(String packageName);
	Optional<Package> findPackageByIdAndTypeOfPackage(UUID packageUUID, TypeOfPackage typeOfPackage);
 }
