package project.instagram.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import project.instagram.common.enums.RequestName;

@Entity
@Table(name = "TypeOfRequests")
public class TypeOfRequest implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Enumerated(EnumType.STRING)
    @Id
	@Column(name = "name", length = 20)
	private RequestName name;

	public TypeOfRequest() {
	}
	
	public TypeOfRequest(RequestName requestName) {
		this.name = requestName;
	}

	public RequestName getName() {
		return name;
	}

	public void setName(RequestName name) {
		this.name = name;
	}

}
