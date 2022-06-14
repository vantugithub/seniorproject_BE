package project.instagram.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.instagram.entity.Package;
import project.instagram.entity.TypeOfPackage;

@Repository
public interface PackageRepository extends JpaRepository<Package, UUID> {
	Optional<Package> findPackageByName(String packageName);

	Optional<Package> findPackageByIdAndTypeOfPackage(UUID packageUUID, TypeOfPackage typeOfPackage);

	Page<Package> findAllByActiveTrueAndTypeOfPackage(TypeOfPackage typeOfPackage, Pageable pageable);

	Page<Package> findAllByTypeOfPackage(TypeOfPackage typeOfPackage, Pageable pageable);
	
	List<Package> findAllByTypeOfPackage(TypeOfPackage typeOfPackage);
}
