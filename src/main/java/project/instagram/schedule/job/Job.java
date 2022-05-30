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
	
	@JsonProperty("hashtagClientManagementJobs")
	private Set<HashtagClientManagementJob> hashtagClientManagementJobs;
	
	public String getHashtag() {
		return hashtag;
	}
	
	public void setHashtag(String hashtag) {
		this.hashtag = hashtag;
	}
	
	public Set<HashtagClientManagementJob> getHashtagClientManagementJobs() {
		return hashtagClientManagementJobs;
	}

	public void setHashtagClientManagementJobs(Set<HashtagClientManagementJob> hashtagClientManagementJobs) {
		this.hashtagClientManagementJobs = hashtagClientManagementJobs;
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

	public Job(String hashtag, int crawlQuantity, String statusJob,
			Set<HashtagClientManagementJob> hashtagClientManagementJobs) {
		super();
		this.hashtag = hashtag;
		this.crawlQuantity = crawlQuantity;
		this.statusJob = statusJob;
		this.hashtagClientManagementJobs = hashtagClientManagementJobs;
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
				+ ", hashtagClientManagementJobs=" + hashtagClientManagementJobs + "]";
	}

}
