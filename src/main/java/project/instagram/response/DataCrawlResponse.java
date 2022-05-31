package project.instagram.response;

import java.util.Date;

public class DataCrawlResponse {
	private String codePost;
	private String caption;
	private Date createdDatePost;
	private String username;
	private String hashtag;

	public String getCodePost() {
		return codePost;
	}

	public void setCodePost(String codePost) {
		this.codePost = codePost;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public Date getCreatedDatePost() {
		return createdDatePost;
	}

	public void setCreatedDatePost(Date createdDatePost) {
		this.createdDatePost = createdDatePost;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getHashtag() {
		return hashtag;
	}

	public void setHashtag(String hashtag) {
		this.hashtag = hashtag;
	}

	public DataCrawlResponse(String codePost, String caption, Date createdDatePost, String username, String hashtag) {
		this.codePost = codePost;
		this.caption = caption;
		this.createdDatePost = createdDatePost;
		this.username = username;
		this.hashtag = hashtag;
	}

	public DataCrawlResponse() {
	}

}
