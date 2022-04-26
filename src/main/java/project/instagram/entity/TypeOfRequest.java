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

import project.instagram.common.enums.RequestName;

@Entity
@Table(name = "TypeOfRequests")
public class TypeOfRequest implements Serializable {
	
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
	private RequestName name;

	public TypeOfRequest() {
	}
	
	public TypeOfRequest(RequestName requestName) {
		this.name = requestName;
	}

	public TypeOfRequest(UUID id, RequestName name) {
		super();
		this.id = id;
		this.name = name;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public RequestName getName() {
		return name;
	}

	public void setName(RequestName name) {
		this.name = name;
	}

}
