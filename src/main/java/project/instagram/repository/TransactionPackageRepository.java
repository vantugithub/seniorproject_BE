package project.instagram.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import project.instagram.entity.Client;
import project.instagram.entity.Package;
import project.instagram.entity.TransactionPackage;

@Repository
@Transactional
public interface TransactionPackageRepository extends JpaRepository<TransactionPackage, Integer> {

	@Query(value = "SELECT t.id, t.expired_date, t.issued_date, t.package, t.client "
			+ "FROM transaction_packages t WHERE t.package IN "
			+ "(SELECT p.id FROM packages p WHERE p.type_of_package IN "
			+ "(SELECT tp.id FROM type_of_package tp WHERE tp.name = ?1 AND t.client = ?2 )) "
			+ "AND t.expired_date > ?3 " + "ORDER BY t.id DESC LIMIT 1", nativeQuery = true)
	public Optional<TransactionPackage> findByExpiredDateGreaterThanEqualAndClientAndChildPackage(String packageType,
			String clientId, Date currentDate);

	@Query(value = "SELECT " + "t.id, " + "t.client, " + "t.expired_date, " + "t.issued_date, " + "t.package " + "FROM "
			+ "transaction_packages t " + "WHERE " + "t.client= ?1 " + "AND (t.expired_date>= ?2 "
			+ "OR t.expired_date is null) " + "ORDER BY " + "t.expired_date ASC", nativeQuery = true)
	public Set<TransactionPackage> findAllValidTransactionPackages(Client client, Date currentDate);

	@Query(value = "SELECT t.id, t.expired_date, t.issued_date, t.package, t.client "
			+ "			FROM transaction_packages t WHERE t.package IN "
			+ "			(SELECT p.id FROM packages p WHERE p.type_of_package IN "
			+ "			(SELECT tp.id FROM type_of_package tp WHERE tp.name = ?1 )) " + "            AND t.client = ?2 "
			+ "			AND (t.expired_date > ?3 OR t.expired_date IS NULL ) "
			+ "			ORDER BY t.id DESC", nativeQuery = true)
	public List<TransactionPackage> findAllValidExtraTransactionPackages(String packageType, String clientId,
			Date currentDate);

	Page<TransactionPackage> findAllByClientOrderByIssuedeDateDesc(Pageable pageable, Client client);

	@Query(value = "select t.id, t.issued_date, t.client, t.package, t.expired_date from transaction_packages t where "
			+ "t.package = ?1 and t.issued_date >= ?2 and t.issued_date < ?3", nativeQuery = true)
	List<TransactionPackage> findAllByPackageAndPeriodOfTime(Package pac, Date startDate, Date endDate);
	
	@Query(value = "select t.id, t.issued_date, t.client, t.package, t.expired_date from transaction_packages t where "
			+ "t.issued_date >= ?1 and t.issued_date < ?2", nativeQuery = true)
	List<TransactionPackage> findAllByPeriodOfTime(Date startDate, Date endDate);

}