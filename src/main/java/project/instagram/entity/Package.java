package project.instagram.entity;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "packages")
public class Package implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "VARCHAR(40)")
	@Type(type = "uuid-char")
	@NotNull
	private UUID id;
	
	@Column(name = "name", length = 50)
	private String name;
	
	@Column(name = "crawlQuantity")
	private byte crawlQuantity;
	
	@Column(name = "searchQuantity")
	private byte searchQuantity;
	
	@Column(name = "numberOfPostsInEachSearch")
	private short numberOfPostInEachSearch;
	
	@Column(name = "numberOfPostsPerHashtag")
	private short numberOfPostsPerHashtag;
	
	@Column(name = "price")
	private double price;
	
	@Column(name = "numberOfMonths", nullable = true)
	private byte numberOfMonths;
	
	@Column(name = "active")
	private boolean active;
	
	@OneToMany(mappedBy = "parentPackage", fetch = FetchType.LAZY)
	private Set<TransactionPackage> transactionPackages;
	
	@JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer"})
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "typeOfPackage")
	private TypeOfPackage typeOfPackage;
	
	

	public Package(@NotNull UUID id, String name, byte crawlQuantity, byte searchQuantity,
			short numberOfPostInEachSearch, short numberOfPostsPerHashtag, double price, byte numberOfMonths,
			boolean active, Set<TransactionPackage> transactionPackages, TypeOfPackage typeOfPackage) {
		this.id = id;
		this.name = name;
		this.crawlQuantity = crawlQuantity;
		this.searchQuantity = searchQuantity;
		this.numberOfPostInEachSearch = numberOfPostInEachSearch;
		this.numberOfPostsPerHashtag = numberOfPostsPerHashtag;
		this.price = price;
		this.numberOfMonths = numberOfMonths;
		this.active = active;
		this.transactionPackages = transactionPackages;
		this.typeOfPackage = typeOfPackage;
	}

	public byte getNumberOfMonths() {
		return numberOfMonths;
	}

	public void setNumberOfMonth(byte numberOfMonths) {
		this.numberOfMonths = numberOfMonths;
	}

	public Package(String name) {
		this.name = name;
	}

	public Package() {
	}

	public Package(@NotNull UUID id, String name, byte crawlQuantity, byte searchQuantity,
			short numberOfPostInEachSearch, short numberOfPostsPerHashtag, double price, boolean active,
			Set<TransactionPackage> transactionPackages, TypeOfPackage typeOfPackage) {
		this.id = id;
		this.name = name;
		this.crawlQuantity = crawlQuantity;
		this.searchQuantity = searchQuantity;
		this.numberOfPostInEachSearch = numberOfPostInEachSearch;
		this.numberOfPostsPerHashtag = numberOfPostsPerHashtag;
		this.price = price;
		this.active = active;
		this.transactionPackages = transactionPackages;
		this.typeOfPackage = typeOfPackage;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Set<TransactionPackage> getTransactionPackages() {
		return transactionPackages;
	}

	public void setTransactionPackages(Set<TransactionPackage> transactionPackages) {
		this.transactionPackages = transactionPackages;
	}

	public TypeOfPackage getTypeOfPackage() {
		return typeOfPackage;
	}

	public void setTypeOfPackage(TypeOfPackage typeOfPackage) {
		this.typeOfPackage = typeOfPackage;
	}

	@Override
	public String toString() {
		return "Package [id=" + id + ", name=" + name + ", crawlQuantity=" + crawlQuantity + ", searchQuantity="
				+ searchQuantity + ", numberOfPostInEachSearch=" + numberOfPostInEachSearch
				+ ", numberOfPostsPerHashtag=" + numberOfPostsPerHashtag + ", price=" + price + ", numberOfMonth="
				+ numberOfMonths + ", active=" + active + ", transactionPackages=" + transactionPackages
				+ ", typeOfPackage=" + typeOfPackage + "]";
	}

}
