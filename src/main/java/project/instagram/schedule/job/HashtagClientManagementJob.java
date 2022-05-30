package project.instagram.schedule.job;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HashtagClientManagementJob implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private long id;

	@JsonProperty("clientId")
	private String clientId;
	
	@JsonProperty("crawlQuantity")
	private int crawlQuantity;

	@JsonProperty("transactionPackage")
	private long transactionPackage;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public HashtagClientManagementJob() {
	}

	public long getTransactionPackage() {
		return transactionPackage;
	}

	public void setTransactionPackage(long transactionPackage) {
		this.transactionPackage = transactionPackage;
	}

	public HashtagClientManagementJob(long id, String clientId, long transactionPackage) {
		this.id = id;
		this.clientId = clientId;
		this.transactionPackage = transactionPackage;
	}

	@Override
	public String toString() {
		return "HashtagClientManagementJob [id=" + id + ", clientId=" + clientId + ", transactionPackage="
				+ transactionPackage + "]";
	}

	public HashtagClientManagementJob(long id, String clientId, int crawlQuantity, long transactionPackage) {
		this.id = id;
		this.clientId = clientId;
		this.crawlQuantity = crawlQuantity;
		this.transactionPackage = transactionPackage;
	}

	public int getCrawlQuantity() {
		return crawlQuantity;
	}

	public void setCrawlQuantity(int crawlQuantity) {
		this.crawlQuantity = crawlQuantity;
	}

}
