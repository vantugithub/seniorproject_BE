package project.instagram.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import project.instagram.common.enums.StatusRequestName;

@Entity
@Table(name = "StatusOfRequests")
public class StatusOfRequest implements Serializable{
	
	private static final long serialVersionUID = 1L;

//	@Id
//    @GeneratedValue(generator = "uuid2")
//    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
//    @Column(name = "id", columnDefinition = "VARCHAR(40)")
//	@Type(type = "uuid-char")
//	private UUID id;
	
	@Enumerated(EnumType.STRING)
    @Id
	@Column(name = "name", length = 20)
	private StatusRequestName name;

	public StatusRequestName getName() {
		return name;
	}

	public void setName(StatusRequestName name) {
		this.name = name;
	}
	
	public StatusOfRequest(StatusRequestName statusRequestName) {
		this.name = statusRequestName;
	}

	public StatusOfRequest() {
	}
	
}
