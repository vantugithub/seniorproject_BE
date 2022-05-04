package project.instagram.response;

import java.util.Date;

public class TransactionPackageResponse {
	private int id;
	private Date issuedeDate;
	private Date expiredDate;
	private PackageResponse validPackage;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getIssuedeDate() {
		return issuedeDate;
	}
	public void setIssuedeDate(Date issuedeDate) {
		this.issuedeDate = issuedeDate;
	}
	public Date getExpiredDate() {
		return expiredDate;
	}
	public void setExpiredDate(Date expiredDate) {
		this.expiredDate = expiredDate;
	}
	public TransactionPackageResponse() {
	}
	
	public PackageResponse getValidPackage() {
		return validPackage;
	}
	
	public void setValidPackage(PackageResponse validPackage) {
		this.validPackage = validPackage;
	}
	
	
}
