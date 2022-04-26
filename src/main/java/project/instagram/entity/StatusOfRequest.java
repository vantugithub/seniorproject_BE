package project.instagram.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;

import project.instagram.common.enums.StatusRequestName;

@Entity
@Table(name = "StatusOfRequests")
public class StatusOfRequest implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "VARCHAR(40)")
	@Type(type = "uuid-char")
	private UUID id;
	
	@Enumerated(EnumType.STRING)
    @NaturalId
	@Column(name = "name", length = 20)
	private StatusRequestName name;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public StatusRequestName getName() {
		return name;
	}

	public void setName(StatusRequestName name) {
		this.name = name;
	}

	public StatusOfRequest(UUID id, StatusRequestName name) {
		this.id = id;
		this.name = name;
	}
	
	public StatusOfRequest(StatusRequestName statusRequestName) {
		this.name = statusRequestName;
	}

	public StatusOfRequest() {
	}
	
}
