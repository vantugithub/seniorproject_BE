package project.instagram.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "requests")
@EntityListeners(AuditingEntityListener.class)
public class Request implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", length = 50)
	private String id;
	
	@Column(name = "reason")
	@Type(type = "text")
	private String reason;
	
	@Column(name = "title")
	private String title;
	
	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;
	
	@ManyToOne
	@JoinColumn(name = "client_id", nullable = false)
	private Client clientRequest;
	
	@ManyToOne
	@JoinColumn(name = "updatedBy", nullable = true)
	private Staff staff;
	
	@OneToOne
	@JoinColumn(name = "typeOfRequest")
	private TypeOfRequest typeOfRequest;
	
	@OneToOne
	@JoinColumn(name = "statusOfRequest")
	private StatusOfRequest statusOfRequest;

	public Request() {
	}

	public Request(String id, String reason, String title, Date createdDate, Date modifiedDate, Client clientRequest,
			Staff staff, TypeOfRequest typeOfRequest, StatusOfRequest statusOfRequest) {
		this.id = id;
		this.reason = reason;
		this.title = title;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
		this.clientRequest = clientRequest;
		this.staff = staff;
		this.typeOfRequest = typeOfRequest;
		this.statusOfRequest = statusOfRequest;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Client getClientRequest() {
		return clientRequest;
	}

	public void setClientRequest(Client clientRequest) {
		this.clientRequest = clientRequest;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public TypeOfRequest getTypeOfRequest() {
		return typeOfRequest;
	}

	public void setTypeOfRequest(TypeOfRequest typeOfRequest) {
		this.typeOfRequest = typeOfRequest;
	}

	public StatusOfRequest getStatusOfRequest() {
		return statusOfRequest;
	}

	public void setStatusOfRequest(StatusOfRequest statusOfRequest) {
		this.statusOfRequest = statusOfRequest;
	}

}
