package project.instagram.repository;

import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import project.instagram.entity.RunningSummary;
import project.instagram.entity.TransactionPackage;

@Repository
@Transactional
public interface RunningSummaryRepository extends JpaRepository<RunningSummary, UUID> {

	Set<RunningSummary> findAllByTransactionPackage(TransactionPackage transactionPackage);

	@Query(value = "select " + "r.id, " + "r.client, " + "r.crawled_extra_package_quantity, "
			+ "r.crawled_package_quantity, " + "r.expired_date, " + "r.issued_date, "
			+ "r.searched_extra_package_quantity , " + "r.searched_package_quantity , " + "r.transaction_package "
			+ "from " + "running_summaries r " + "where " + "r.transaction_package=?", nativeQuery = true)
	Set<RunningSummary> fetchAllByTransactionPackage(TransactionPackage transactionPackage);

	Optional<RunningSummary> findRunningSummaryByExpiredDateGreaterThanEqualAndTransactionPackage(Date date,
			TransactionPackage transactionPackage);

}
