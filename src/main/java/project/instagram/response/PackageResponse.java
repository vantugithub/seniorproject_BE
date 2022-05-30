package project.instagram.response;

import java.io.Serializable;

public class PackageResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private byte numberOfMonths;
	private String name;
	private byte crawlQuantity;
	private byte searchQuantity;
	private short numberOfPostInEachSearch;
	private short numberOfPostsPerHashtag;
	private double price;
	private boolean active;

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public byte getNumberOfMonths() {
		return numberOfMonths;
	}

	public void setNumberOfMonths(byte numberOfMonths) {
		this.numberOfMonths = numberOfMonths;
	}

	public PackageResponse(String id, byte numberOfMonths, String name, byte crawlQuantity, byte searchQuantity,
			short numberOfPostInEachSearch, short numberOfPostsPerHashtag, double price) {
		this.id = id;
		this.numberOfMonths = numberOfMonths;
		this.name = name;
		this.crawlQuantity = crawlQuantity;
		this.searchQuantity = searchQuantity;
		this.numberOfPostInEachSearch = numberOfPostInEachSearch;
		this.numberOfPostsPerHashtag = numberOfPostsPerHashtag;
		this.price = price;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public PackageResponse(String id, byte numberOfMonths, String name, byte crawlQuantity, byte searchQuantity,
			short numberOfPostInEachSearch, short numberOfPostsPerHashtag, double price, boolean active) {
		this.id = id;
		this.numberOfMonths = numberOfMonths;
		this.name = name;
		this.crawlQuantity = crawlQuantity;
		this.searchQuantity = searchQuantity;
		this.numberOfPostInEachSearch = numberOfPostInEachSearch;
		this.numberOfPostsPerHashtag = numberOfPostsPerHashtag;
		this.price = price;
		this.active = active;
	}

}
