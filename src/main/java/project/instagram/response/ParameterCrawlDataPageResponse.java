package project.instagram.response;

public class ParameterCrawlDataPageResponse {
	private int page;
	private int size;
	private String date;
	private String hashtag;
	private String url;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getHashtag() {
		return hashtag;
	}

	public void setHashtag(String hashtag) {
		this.hashtag = hashtag;
	}

	public ParameterCrawlDataPageResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ParameterCrawlDataPageResponse(int page, int size, String date, String hashtag) {
		super();
		this.page = page;
		this.size = size;
		this.date = date;
		this.hashtag = hashtag;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public ParameterCrawlDataPageResponse(int page, int size, String date, String hashtag, String url) {
		super();
		this.page = page;
		this.size = size;
		this.date = date;
		this.hashtag = hashtag;
		this.url = url;
	}

}
