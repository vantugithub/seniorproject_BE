package project.instagram.request;

public class PackageFormRequest {
	
	private String name;
	
	private byte crawlQuantity;
	
	private byte searchQuantity;
	
	private short numberOfPostInEachSearch;
	
	private short numberOfPostsPerHashtag;
	
	private double price;
	
	private byte numberOfMonths;
	
	private String typeOfPackageId;

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

	public String getTypeOfPackageId() {
		return typeOfPackageId;
	}

	public void setTypeOfPackageId(String typeOfPackageId) {
		this.typeOfPackageId = typeOfPackageId;
	}

	public PackageFormRequest() {
	}

	public byte getNumberOfMonths() {
		return numberOfMonths;
	}

	public void setNumberOfMonths(byte numberOfMonths) {
		this.numberOfMonths = numberOfMonths;
	}
	
	
}
