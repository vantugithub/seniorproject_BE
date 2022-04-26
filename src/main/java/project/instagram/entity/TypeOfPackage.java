package project.instagram.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "TypeOfPackage")
public class TypeOfPackage implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "VARCHAR(40)")
	@Type(type = "uuid-char")
	private UUID id;
	
	@Column(name = "name", length = 50)
	private String name;

	public TypeOfPackage(UUID id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public TypeOfPackage(String name) {
		this.name = name;
	}

	public TypeOfPackage() {
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
	
}
