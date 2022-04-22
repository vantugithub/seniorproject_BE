package project.instagram.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TypeOfPackage")
public class TypeOfPackage implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	private byte id;
	
	@Column(name = "name", length = 50)
	private String name;

	public TypeOfPackage(byte id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public TypeOfPackage(String name) {
		this.name = name;
	}

	public TypeOfPackage() {
	}

	public byte getId() {
		return id;
	}

	public void setId(byte id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
