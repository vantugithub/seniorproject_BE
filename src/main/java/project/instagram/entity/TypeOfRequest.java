package project.instagram.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;

import project.instagram.common.enums.RequestName;

@Entity
@Table(name = "TypeOfRequests")
public class TypeOfRequest implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private byte id;
	
	@Enumerated(EnumType.STRING)
    @NaturalId
	@Column(name = "name", length = 20)
	private RequestName name;

	public TypeOfRequest() {
	}

	public TypeOfRequest(byte id, RequestName name) {
		super();
		this.id = id;
		this.name = name;
	}

	public byte getId() {
		return id;
	}

	public void setId(byte id) {
		this.id = id;
	}

	public RequestName getName() {
		return name;
	}

	public void setName(RequestName name) {
		this.name = name;
	}

}
