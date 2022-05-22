package project.instagram.schedule.job;

import java.io.Serializable;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Job implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("hashtag")
	private String hashtag;
	
	@JsonProperty("crawlQuantity")
	private int crawlQuantity;
	
	@JsonProperty("statusJob")
	private String statusJob;
	
	@JsonProperty("listIdClients")
	private Set<String> listIdClients;
	
	public String getHashtag() {
		return hashtag;
	}
	
	public void setHashtag(String hashtag) {
		this.hashtag = hashtag;
	}
	
	public Set<String> getListIdClients() {
		return listIdClients;
	}
	
	public void setListIdClients(Set<String> listIdClients) {
		this.listIdClients = listIdClients;
	}

	public Job() {
	}
	
	public int getCrawlQuantity() {
		return crawlQuantity;
	}
	
	public String getStatusJob() {
		return statusJob;
	}

	public void setStatusJob(String statusJob) {
		this.statusJob = statusJob;
	}

	public Job(String hashtag, int crawlQuantity, String statusJob, Set<String> listIdClients) {
		super();
		this.hashtag = hashtag;
		this.crawlQuantity = crawlQuantity;
		this.statusJob = statusJob;
		this.listIdClients = listIdClients;
	}

	public void setCrawlQuantity(int crawlQuantity) {
		if ( crawlQuantity > this.crawlQuantity ) {
			this.crawlQuantity = crawlQuantity;
		} else {
			this.crawlQuantity = getCrawlQuantity();
		}
	}

	@Override
	public String toString() {
		return "Job [hashtag=" + hashtag + ", crawlQuantity=" + crawlQuantity + ", statusJob=" + statusJob
				+ ", listIdClients=" + listIdClients + "]";
	}

}
