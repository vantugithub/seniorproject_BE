package project.instagram.response;

import java.io.Serializable;
import java.util.Date;

public class RunningSummaryResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2132966532443660369L;
	private String id;
	private Date issuedDate;
	private Date expiredDate;
	private byte crawledPackageQuantity;
	private byte searchedPackageQuantity;
	private byte crawledExtraPackageQuantity;
	private byte searchedExtraPackageQuantity;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getIssuedDate() {
		return issuedDate;
	}

	public void setIssuedDate(Date issuedDate) {
		this.issuedDate = issuedDate;
	}

	public Date getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(Date expiredDate) {
		this.expiredDate = expiredDate;
	}

	public byte getCrawledPackageQuantity() {
		return crawledPackageQuantity;
	}

	public void setCrawledPackageQuantity(byte crawledPackageQuantity) {
		this.crawledPackageQuantity = crawledPackageQuantity;
	}

	public byte getSearchedPackageQuantity() {
		return searchedPackageQuantity;
	}

	public void setSearchedPackageQuantity(byte searchedPackageQuantity) {
		this.searchedPackageQuantity = searchedPackageQuantity;
	}

	public byte getCrawledExtraPackageQuantity() {
		return crawledExtraPackageQuantity;
	}

	public void setCrawledExtraPackageQuantity(byte crawledExtraPackageQuantity) {
		this.crawledExtraPackageQuantity = crawledExtraPackageQuantity;
	}

	public byte getSearchedExtraPackageQuantity() {
		return searchedExtraPackageQuantity;
	}

	public void setSearchedExtraPackageQuantity(byte searchedExtraPackageQuantity) {
		this.searchedExtraPackageQuantity = searchedExtraPackageQuantity;
	}

	public RunningSummaryResponse(String id, Date issuedDate, Date expiredDate, byte crawledPackageQuantity,
			byte searchedPackageQuantity, byte crawledExtraPackageQuantity, byte searchedExtraPackageQuantity) {
		this.id = id;
		this.issuedDate = issuedDate;
		this.expiredDate = expiredDate;
		this.crawledPackageQuantity = crawledPackageQuantity;
		this.searchedPackageQuantity = searchedPackageQuantity;
		this.crawledExtraPackageQuantity = crawledExtraPackageQuantity;
		this.searchedExtraPackageQuantity = searchedExtraPackageQuantity;
	}

	public RunningSummaryResponse() {
	}

}
