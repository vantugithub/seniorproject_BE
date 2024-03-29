package project.instagram.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "TransactionPackages")
public class TransactionPackage implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "issuedDate")
	private Date issuedeDate;
	
	@Column(name = "expiredDate", nullable = true)
	private Date expiredDate;
	
	@OneToMany(mappedBy = "transactionPackage", fetch = FetchType.LAZY)
	private Set<HashtagRunningHistory> hashtagRunningSummaries;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "client")
	private Client client;
	
	@OneToMany(mappedBy = "transactionPackage", fetch = FetchType.LAZY)
	private Set<RunningSummary> runningSummaries;
	
	@JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer"})
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "package")
	private Package parentPackage;
	
	@OneToMany(mappedBy = "transactionPackage", fetch = FetchType.LAZY)
	private Set<HashtagClientManagement> hashtagClientManagements;

	@Override
	public String toString() {
		return "TransactionPackage [id=" + id + ", issuedeDate=" + issuedeDate + ", expiredDate=" + expiredDate + "]";
	}

	public TransactionPackage() {
	}

	public TransactionPackage(int id, Date issuedeDate, Date expiredDate, Package parentPackage) {
		this.id = id;
		this.issuedeDate = issuedeDate;
		this.expiredDate = expiredDate;
		this.parentPackage = parentPackage;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getIssuedeDate() {
		return issuedeDate;
	}

	public void setIssuedeDate(Date issuedeDate) {
		this.issuedeDate = issuedeDate;
	}

	public Date getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(Date expiredDate) {
		this.expiredDate = expiredDate;
	}

	public Set<HashtagRunningHistory> getHashtagRunningSummaries() {
		return hashtagRunningSummaries;
	}

	public void setHashtagRunningSummaries(Set<HashtagRunningHistory> hashtagRunningSummaries) {
		this.hashtagRunningSummaries = hashtagRunningSummaries;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Set<RunningSummary> getRunningSummaries() {
		return runningSummaries;
	}

	public void setRunningSummaries(Set<RunningSummary> runningSummaries) {
		this.runningSummaries = runningSummaries;
	}

	public Package getParentPackage() {
		return parentPackage;
	}

	public void setParentPackage(Package package1) {
		this.parentPackage = package1;
	}

	public Set<HashtagClientManagement> getHashtagClientManagements() {
		return hashtagClientManagements;
	}

	public void setHashtagClientManagements(Set<HashtagClientManagement> hashtagClientManagements) {
		this.hashtagClientManagements = hashtagClientManagements;
	}

	
}
