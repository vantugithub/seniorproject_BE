package project.instagram.response;

import java.util.Date;

public class HashtagResponse {
	private String hashtag;
	private Date issuedDate;
	
	public String getHashtag() {
		return hashtag;
	}
	public void setHashtag(String hashtag) {
		this.hashtag = hashtag;
	}
	public Date getIssuedDate() {
		return issuedDate;
	}
	public void setIssuedDate(Date issuedDate) {
		this.issuedDate = issuedDate;
	}
	public HashtagResponse() {
	}
	
}
