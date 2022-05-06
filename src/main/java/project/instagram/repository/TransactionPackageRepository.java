package project.instagram.repository;

import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import project.instagram.entity.TransactionPackage;

@Repository
@Transactional
public interface TransactionPackageRepository extends JpaRepository<TransactionPackage, Integer> {
	
	@Query(value = "SELECT t.id, t.expired_date, t.issued_date, t.package, t.client "
			+ "FROM transaction_packages t WHERE t.package IN "
			+ "(SELECT p.id FROM packages p WHERE p.type_of_package IN "
			+ "(SELECT tp.id FROM type_of_package tp WHERE tp.name = ?1 AND t.client = ?2 )) "
			+ "AND t.expired_date > ?3 "
			+ "ORDER BY t.id DESC LIMIT 1", nativeQuery = true)
	public Optional<TransactionPackage> findByExpiredDateGreaterThanEqualAndClientAndChildPackage(
			String packageType, String clientId, Date currentDate);
	
}