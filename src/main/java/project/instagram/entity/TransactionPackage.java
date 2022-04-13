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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "TransactionPackages")
public class TransactionPackage implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "issuedDate")
	@Temporal(TemporalType.TIMESTAMP)
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
	
	@ManyToOne
	@JoinColumn(name = "package")
	private Package package1;

	public TransactionPackage() {
	}

	public TransactionPackage(int id, Date issuedeDate, Date expiredDate,
			Set<HashtagRunningHistory> hashtagRunningSummaries, Client client, Set<RunningSummary> runningSummaries,
			Package package1) {
		this.id = id;
		this.issuedeDate = issuedeDate;
		this.expiredDate = expiredDate;
		this.hashtagRunningSummaries = hashtagRunningSummaries;
		this.client = client;
		this.runningSummaries = runningSummaries;
		this.package1 = package1;
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

	public Package getPackage1() {
		return package1;
	}

	public void setPackage1(Package package1) {
		this.package1 = package1;
	}
	
}
