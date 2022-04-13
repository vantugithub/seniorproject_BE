package project.instagram.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "clients")
@EntityListeners(AuditingEntityListener.class)
public class Client extends User implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "biography", length = 300)
	private String biography;
	
	@OneToMany(mappedBy = "clientRequest", fetch = FetchType.LAZY)
	private Set<Request> requests;
	
	@OneToMany(mappedBy = "clientManagement", fetch = FetchType.LAZY)
	private Set<HashtagClientManagement> hashtagClientManagements;

	@OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
	private Set<HashtagRunningHistory> hashtagRunningHistories;
	
	@OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
	private Set<TransactionPackage> transactionPackages;
	
	@OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
	private Set<RunningSummary> runningSummaries;

	public String getBiography() {
		return biography;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}

	public Set<Request> getRequests() {
		return requests;
	}

	public void setRequests(Set<Request> requests) {
		this.requests = requests;
	}

	public Set<HashtagClientManagement> getHashtagClientManagements() {
		return hashtagClientManagements;
	}

	public void setHashtagClientManagements(Set<HashtagClientManagement> hashtagClientManagements) {
		this.hashtagClientManagements = hashtagClientManagements;
	}

	public Set<HashtagRunningHistory> getHashtagRunningHistories() {
		return hashtagRunningHistories;
	}

	public void setHashtagRunningHistories(Set<HashtagRunningHistory> hashtagRunningHistories) {
		this.hashtagRunningHistories = hashtagRunningHistories;
	}

	public Set<TransactionPackage> getTransactionPackages() {
		return transactionPackages;
	}

	public void setTransactionPackages(Set<TransactionPackage> transactionPackages) {
		this.transactionPackages = transactionPackages;
	}

	public Set<RunningSummary> getRunningSummaries() {
		return runningSummaries;
	}

	public void setRunningSummaries(Set<RunningSummary> runningSummaries) {
		this.runningSummaries = runningSummaries;
	}

	public Client() {
		super();
	}

	public Client(UUID id, @NotNull String email, @NotNull String password, String firstName, String lastName,
			String phone, String address, Date createdDate, boolean active, Role role) {
		super(id, email, password, firstName, lastName, phone, address, createdDate, active, role);
	}

	public Client(String biography, Set<Request> requests, Set<HashtagClientManagement> hashtagClientManagements,
			Set<HashtagRunningHistory> hashtagRunningHistories, Set<TransactionPackage> transactionPackages,
			Set<RunningSummary> runningSummaries) {
		super();
		this.biography = biography;
		this.requests = requests;
		this.hashtagClientManagements = hashtagClientManagements;
		this.hashtagRunningHistories = hashtagRunningHistories;
		this.transactionPackages = transactionPackages;
		this.runningSummaries = runningSummaries;
	}
	
}
