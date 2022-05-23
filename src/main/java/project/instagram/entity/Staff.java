package project.instagram.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "staffs")
@EntityListeners(AuditingEntityListener.class)
public class Staff extends User {
	
	private static final long serialVersionUID = 1L;
	
	@OneToOne
	@JoinColumn(name ="updatedBy")
	private Staff updatedById;
	
	@OneToOne
	@JoinColumn(name = "createdBy")
	private Staff createdById;
	
	@OneToMany(mappedBy = "staff", fetch = FetchType.LAZY)
	private Set<Request> requests;

	@OneToMany(mappedBy = "staff", fetch = FetchType.LAZY)
	private Set<BlackHashtag> blackHashtags;

	public Staff() {
		super();
	}

	public Staff(Staff updatedById, Staff createdById, Set<Request> requests,
			Set<BlackHashtag> blackHashtags) {
		super();
		this.updatedById = updatedById;
		this.createdById = createdById;
		this.requests = requests;
		this.blackHashtags = blackHashtags;
	}

	public Staff getUpdatedById() {
		return updatedById;
	}

	public void setUpdatedById(Staff updatedById) {
		this.updatedById = updatedById;
	}

	public Staff getCreatedById() {
		return createdById;
	}

	public void setCreatedById(Staff createdById) {
		this.createdById = createdById;
	}

	public Set<Request> getRequests() {
		return requests;
	}

	public void setRequests(Set<Request> requests) {
		this.requests = requests;
	}

	public Set<BlackHashtag> getBlackHashtags() {
		return blackHashtags;
	}

	public void setBlackHashtags(Set<BlackHashtag> blackHashtags) {
		this.blackHashtags = blackHashtags;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
