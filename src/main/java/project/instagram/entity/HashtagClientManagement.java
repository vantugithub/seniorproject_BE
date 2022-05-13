package project.instagram.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "HashtagClientManagements")
public class HashtagClientManagement implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name = "dateStartCrawl")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateStartCrawl;
	
	@Column(name = "crawlQuantity")
	private int crawlQuantity;
	
	@Column(name = "active")
	private boolean active;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "client")
	private Client clientManagement;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hashtag")
	private Hashtag hashtagClientManagement;

	public HashtagClientManagement() {
	}
	
	public HashtagClientManagement(Client clientManagement, Hashtag hashtagClientManagement) {
		this.clientManagement = clientManagement;
		this.hashtagClientManagement = hashtagClientManagement;
	}
	
	public HashtagClientManagement(Date dateStartCrawl, int crawlQuantity, boolean active, Client clientManagement,
			Hashtag hashtagClientManagement) {
		this.dateStartCrawl = dateStartCrawl;
		this.crawlQuantity = crawlQuantity;
		this.active = active;
		this.clientManagement = clientManagement;
		this.hashtagClientManagement = hashtagClientManagement;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDateStartCrawl() {
		return dateStartCrawl;
	}

	public void setDateStartCrawl(Date dateStartCrawl) {
		this.dateStartCrawl = dateStartCrawl;
	}

	public int getCrawlQuantity() {
		return crawlQuantity;
	}

	public void setCrawlQuantity(int crawlQuantity) {
		this.crawlQuantity = crawlQuantity;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Client getClientManagement() {
		return clientManagement;
	}

	public void setClientManagement(Client clientManagement) {
		this.clientManagement = clientManagement;
	}

	public Hashtag getHashtagClientManagement() {
		return hashtagClientManagement;
	}

	public void setHashtagClientManagement(Hashtag hashtagClientManagement) {
		this.hashtagClientManagement = hashtagClientManagement;
	}

	@Override
	public String toString() {
		return "HashtagClientManagement [id=" + id + ", dateStartCrawl=" + dateStartCrawl + ", crawlQuantity="
				+ crawlQuantity + ", active=" + active + "]";
	}
	
	
}
