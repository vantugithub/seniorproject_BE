package project.instagram.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "hashtags")
@EntityListeners(AuditingEntityListener.class)
public class Hashtag implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "name", length = 50, nullable = false)
	private String name;
	
	@CreatedDate
	@Column(name = "issuedDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date issuedDate;
	
	@OneToMany(mappedBy = "hashtag", fetch = FetchType.LAZY)
	private Set<DataCrawl> dataCrawls;
	
	@OneToMany(mappedBy = "hashtag", fetch = FetchType.LAZY)
	private Set<Analysis> analysis;
	
	@OneToMany(mappedBy = "hashtagClientManagement", fetch = FetchType.LAZY)
	private Set<HashtagClientManagement> hashtagClientManagements;
	
	@OneToMany(mappedBy = "hashtag", fetch = FetchType.LAZY)
	private Set<HashtagRunningHistory> hashtagRunningHistories;

	public Hashtag(String name, Date issuedDate, Set<DataCrawl> dataCrawls, Set<Analysis> analysis,
			Set<HashtagClientManagement> hashtagClientManagements, Set<HashtagRunningHistory> hashtagRunningHistories) {
		super();
		this.name = name;
		this.issuedDate = issuedDate;
		this.dataCrawls = dataCrawls;
		this.analysis = analysis;
		this.hashtagClientManagements = hashtagClientManagements;
		this.hashtagRunningHistories = hashtagRunningHistories;
	}

	public Hashtag() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getIssuedDate() {
		return issuedDate;
	}

	public void setIssuedDate(Date issuedDate) {
		this.issuedDate = issuedDate;
	}

	public Set<DataCrawl> getDataCrawls() {
		return dataCrawls;
	}

	public void setDataCrawls(Set<DataCrawl> dataCrawls) {
		this.dataCrawls = dataCrawls;
	}

	public Set<Analysis> getAnalysis() {
		return analysis;
	}

	public void setAnalysis(Set<Analysis> analysis) {
		this.analysis = analysis;
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
	
}
