package project.instagram.response;

import java.util.Date;

public class HashtagRunningHistoryResponse {
	private String id;
	private Date runningTime;
	private String status;
	private String type;
	private String clientId;
	private String hashtag;
	private int transactionPackageId;

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getHashtag() {
		return hashtag;
	}

	public void setHashtag(String hashtag) {
		this.hashtag = hashtag;
	}

	public int getTransactionPackageId() {
		return transactionPackageId;
	}

	public void setTransactionPackageId(int transactionPackageId) {
		this.transactionPackageId = transactionPackageId;
	}

	public HashtagRunningHistoryResponse(String id, Date runningTime, String status, String type, String clientId,
			String hashtag, int transactionPackageId) {
		this.id = id;
		this.runningTime = runningTime;
		this.status = status;
		this.type = type;
		this.clientId = clientId;
		this.hashtag = hashtag;
		this.transactionPackageId = transactionPackageId;
	}

	public HashtagRunningHistoryResponse() {
	}

}
