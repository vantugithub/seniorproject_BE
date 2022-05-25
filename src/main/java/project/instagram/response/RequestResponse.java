package project.instagram.response;

import java.io.Serializable;
import java.util.Date;

public class RequestResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -917926580598566074L;
	private String requestId;
	private Date createdDate;
	private Date modifiedDate;
	private String reason;
	private String title;
	private String statusOfRequest;
	private String typeOfRequest;
	private ClientResponse requestCreator;
	private StaffResponse updatedBy;

	public RequestResponse(String requestId, Date createdDate, Date modifiedDate, String reason, String title,
			String statusOfRequest, String typeOfRequest, ClientResponse requestCreator, StaffResponse updatedBy) {
		this.requestId = requestId;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
		this.reason = reason;
		this.setTitle(title);
		this.statusOfRequest = statusOfRequest;
		this.typeOfRequest = typeOfRequest;
		this.requestCreator = requestCreator;
		this.updatedBy = updatedBy;
	}

	public RequestResponse(String requestId, Date createdDate, Date modifiedDate, String reason, String title,
			ClientResponse requestCreator, StaffResponse updatedBy) {
		this.requestId = requestId;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
		this.reason = reason;
		this.setTitle(title);
		this.requestCreator = requestCreator;
		this.updatedBy = updatedBy;
	}

	public RequestResponse() {
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
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

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public ClientResponse getRequestCreator() {
		return requestCreator;
	}

	public void setRequestCreator(ClientResponse requestCreator) {
		this.requestCreator = requestCreator;
	}

	public StaffResponse getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(StaffResponse updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getStatusOfRequest() {
		return statusOfRequest;
	}

	public void setStatusOfRequest(String statusOfRequest) {
		this.statusOfRequest = statusOfRequest;
	}

	public String getTypeOfRequest() {
		return typeOfRequest;
	}

	public void setTypeOfRequest(String typeOfRequest) {
		this.typeOfRequest = typeOfRequest;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
