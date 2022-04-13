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

import project.instagram.common.enums.StatusRequestName;

@Entity
@Table(name = "StatusOfRequests")
public class StatusOfRequest implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private byte id;
	
	@Enumerated(EnumType.STRING)
    @NaturalId
	@Column(name = "name", length = 20)
	private StatusRequestName name;

	public byte getId() {
		return id;
	}

	public void setId(byte id) {
		this.id = id;
	}

	public StatusRequestName getName() {
		return name;
	}

	public void setName(StatusRequestName name) {
		this.name = name;
	}

	public StatusOfRequest(byte id, StatusRequestName name) {
		this.id = id;
		this.name = name;
	}

	public StatusOfRequest() {
	}
	
}
