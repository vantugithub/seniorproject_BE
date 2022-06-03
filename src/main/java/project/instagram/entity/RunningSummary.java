package project.instagram.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import project.instagram.common.enums.constants.PackageConstants;

@Entity
@Table(name = "RunningSummaries")
public class RunningSummary implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "VARCHAR(40)")
	@Type(type = "uuid-char")
	private UUID id;
	
	@Column(name = "issuedDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date issuedDate;
	
	@Column(name = "expiredDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date expiredDate;
	
	@Column(name = "crawledPackageQuantity")
	private byte crawledPackageQuantity;
	
	@Column(name = "searchedPackageQuantity")
	private byte searchedPackageQuantity;
	
	@Column(name = "crawledExtraPackageQuantity")
	private byte crawledExtraPackageQuantity;
	
	@Column(name = "searchedExtraPackageQuantity")
	private byte searchedExtraPackageQuantity;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "client")
	private Client client;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "transactionPackage")
	private TransactionPackage transactionPackage;
	
	public void increaseQuantityCrawl(TypeOfPackage typeOfPackage, int crawledPackageQuantity) {
		if ( PackageConstants.PACKAGE_TYPE.equals(typeOfPackage.getName()) ) {
			this.crawledPackageQuantity += (byte) crawledPackageQuantity;
		} else {
			this.crawledExtraPackageQuantity += (byte) crawledPackageQuantity;
		}
	}
	
	public void increaseQuantitySearch(TypeOfPackage typeOfPackage, int searchedPackageQuantity) {
		if ( PackageConstants.PACKAGE_TYPE.equals(typeOfPackage.getName()) ) {
			this.searchedPackageQuantity += (byte) searchedPackageQuantity;
		} else {
			this.searchedExtraPackageQuantity += (byte) searchedPackageQuantity;
		}
	}

	public RunningSummary() {
	}

	@Override
	public String toString() {
		return "RunningSummary [id=" + id + ", issuedDate=" + issuedDate + ", expiredDate=" + expiredDate
				+ ", crawledPackageQuantity=" + crawledPackageQuantity + ", searchedPackageQuantity="
				+ searchedPackageQuantity + ", crawledExtraPackageQuantity=" + crawledExtraPackageQuantity
				+ ", searchedExtraPackageQuantity=" + searchedExtraPackageQuantity + "]";
	}

	public RunningSummary(UUID id, Date issuedDate, Date expiredDate, byte crawledPackageQuantity,
			byte searchedPackageQuantity, byte crawledExtraPackageQuantity, byte searchedExtraPackageQuantity,
			Client client, TransactionPackage transactionPackage) {
		super();
		this.id = id;
		this.issuedDate = issuedDate;
		this.expiredDate = expiredDate;
		this.crawledPackageQuantity = crawledPackageQuantity;
		this.searchedPackageQuantity = searchedPackageQuantity;
		this.crawledExtraPackageQuantity = crawledExtraPackageQuantity;
		this.searchedExtraPackageQuantity = searchedExtraPackageQuantity;
		this.client = client;
		this.transactionPackage = transactionPackage;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
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

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public TransactionPackage getTransactionPackage() {
		return transactionPackage;
	}

	public void setTransactionPackage(TransactionPackage transactionPackage) {
		this.transactionPackage = transactionPackage;
	}
	
}
