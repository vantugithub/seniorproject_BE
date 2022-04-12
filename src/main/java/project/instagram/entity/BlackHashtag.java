package project.instagram.entity;

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
	
	@OneToOne
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

	
	
}
