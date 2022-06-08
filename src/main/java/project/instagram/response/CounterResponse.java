package project.instagram.response;

import java.io.Serializable;

public class CounterResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8903562851364140597L;
	private String hashtag;
	private String count;

	public String getHashtag() {
		return hashtag;
	}

	public void setHashtag(String hashtag) {
		this.hashtag = hashtag;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public CounterResponse(String hashtag, String count) {
		this.hashtag = hashtag;
		this.count = count;
	}

	public CounterResponse() {
	}

}
