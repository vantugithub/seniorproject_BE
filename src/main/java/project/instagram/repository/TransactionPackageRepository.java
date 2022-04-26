package project.instagram.repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.instagram.entity.Client;
import project.instagram.entity.TransactionPackage;

@Repository
public interface TransactionPackageRepository extends JpaRepository<TransactionPackage, Integer> {
	
	public Optional<TransactionPackage> findByExpiredDateGreaterThanEqualAndClient(Date currentDate, Client client);
	
	
}
