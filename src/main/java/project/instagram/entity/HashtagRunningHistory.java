package project.instagram.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "HashtagRunningHistories")
public class HashtagRunningHistory implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", nullable = false)
	private String id;
	
	@Column(name = "runningTime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date runningTime;
	
	@Column(name = "crawlQuantity")
	private int crawlQuantity;
	
	@Column(name = "type")
	private String type;
	
	@Column(name = "status")
	private String status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "client", nullable = false)
	private Client client;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hashtag", nullable = false)
	private Hashtag hashtag;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "transactionPackage", nullable = false)
	private TransactionPackage transactionPackage;

	public HashtagRunningHistory() {
	}

	public HashtagRunningHistory(String id, Date runningTime, String type, String status, Client client,
			Hashtag hashtag, TransactionPackage transactionPackage) {
		this.id = id;
		this.runningTime = runningTime;
		this.type = type;
		this.status = status;
		this.client = client;
		this.hashtag = hashtag;
		this.transactionPackage = transactionPackage;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getRunningTime() {
		return runningTime;
	}

	public void setRunningTime(Date runningTime) {
		this.runningTime = runningTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Hashtag getHashtag() {
		return hashtag;
	}

	public void setHashtag(Hashtag hashtag) {
		this.hashtag = hashtag;
	}

	public TransactionPackage getTransactionPackage() {
		return transactionPackage;
	}

	public void setTransactionPackage(TransactionPackage transactionPackage) {
		this.transactionPackage = transactionPackage;
	}

	public int getCrawlQuantity() {
		return crawlQuantity;
	}

	public void setCrawlQuantity(int crawlQuantity) {
		this.crawlQuantity = crawlQuantity;
	}

	public HashtagRunningHistory(String id, Date runningTime, int crawlQuantity, String type, String status,
			Client client, Hashtag hashtag, TransactionPackage transactionPackage) {
		this.id = id;
		this.runningTime = runningTime;
		this.crawlQuantity = crawlQuantity;
		this.type = type;
		this.status = status;
		this.client = client;
		this.hashtag = hashtag;
		this.transactionPackage = transactionPackage;
	}

	@Override
	public String toString() {
		return "HashtagRunningHistory [id=" + id + ", runningTime=" + runningTime + ", crawlQuantity=" + crawlQuantity
				+ ", type=" + type + ", status=" + status + "]";
	}
	
	
}
