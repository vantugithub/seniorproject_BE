package project.instagram.response;

import java.io.Serializable;
import java.util.Date;

public class HashtagClientManagementResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private boolean active;
	private int crawlQuantity;
	private Date dateStartCrawl;
	private String hashtag;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getCrawlQuantity() {
		return crawlQuantity;
	}

	public void setCrawlQuantity(int crawlQuantity) {
		this.crawlQuantity = crawlQuantity;
	}

	public Date getDateStartCrawl() {
		return dateStartCrawl;
	}

	public void setDateStartCrawl(Date dateStartCrawl) {
		this.dateStartCrawl = dateStartCrawl;
	}

	public String getHashtag() {
		return hashtag;
	}

	public void setHashtag(String hashtag) {
		this.hashtag = hashtag;
	}

	public HashtagClientManagementResponse(long id, boolean active, int crawlQuantity, Date dateStartCrawl,
			String hashtag) {
		this.id = id;
		this.active = active;
		this.crawlQuantity = crawlQuantity;
		this.dateStartCrawl = dateStartCrawl;
		this.hashtag = hashtag;
	}

	public HashtagClientManagementResponse() {
	}

}
