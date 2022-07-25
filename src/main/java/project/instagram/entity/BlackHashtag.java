package project.instagram.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "BlackHashtags")
public class BlackHashtag {
	
	@Id
	private String name;
	
	@OneToOne(cascade = CascadeType.ALL)
	@MapsId
	@JoinColumn(name = "name")
	private Hashtag hashtag;
	
	@Column(name = "active")
	private boolean active;
	
	@ManyToOne
	@JoinColumn(name = "createdBy")
	private Staff staff;

	public BlackHashtag() {
	}

	public BlackHashtag(String name, Hashtag hashtag, boolean active, Staff staff) {
		this.name = name;
		this.hashtag = hashtag;
		this.active = active;
		this.staff = staff;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Hashtag getHashtag() {
		return hashtag;
	}

	public void setHashtag(Hashtag hashtag) {
		this.hashtag = hashtag;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}
	
}
