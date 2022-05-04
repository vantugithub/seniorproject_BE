package project.instagram.response;

public class PackageResponse {
	private String name;
	private byte crawlQuantity;
	private byte searchQuantity;
	private short numberOfPostInEachSearch;
	private short numberOfPostsPerHashtag;
	private double price;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public byte getCrawlQuantity() {
		return crawlQuantity;
	}
	public void setCrawlQuantity(byte crawlQuantity) {
		this.crawlQuantity = crawlQuantity;
	}
	public byte getSearchQuantity() {
		return searchQuantity;
	}
	public void setSearchQuantity(byte searchQuantity) {
		this.searchQuantity = searchQuantity;
	}
	public short getNumberOfPostInEachSearch() {
		return numberOfPostInEachSearch;
	}
	public void setNumberOfPostInEachSearch(short numberOfPostInEachSearch) {
		this.numberOfPostInEachSearch = numberOfPostInEachSearch;
	}
	public short getNumberOfPostsPerHashtag() {
		return numberOfPostsPerHashtag;
	}
	public void setNumberOfPostsPerHashtag(short numberOfPostsPerHashtag) {
		this.numberOfPostsPerHashtag = numberOfPostsPerHashtag;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public PackageResponse(String name, byte crawlQuantity, byte searchQuantity, short numberOfPostInEachSearch,
			short numberOfPostsPerHashtag, double price) {
		this.name = name;
		this.crawlQuantity = crawlQuantity;
		this.searchQuantity = searchQuantity;
		this.numberOfPostInEachSearch = numberOfPostInEachSearch;
		this.numberOfPostsPerHashtag = numberOfPostsPerHashtag;
		this.price = price;
	}
	public PackageResponse() {
	}
	
}
