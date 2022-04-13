package project.instagram.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "Analysis")
@EntityListeners(AuditingEntityListener.class)
public class Analysis implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "count", length = 10)
	private String count;
	
	@Column(name = "nameHashtag", length = 255)
	private String nameHashtag;
	
	@Column(name = "dateOfAnalysis")
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateOfAnalysis;
	
	@ManyToOne
	@JoinColumn(name = "topic", nullable = false)
	private Hashtag hashtag;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getNameHashtag() {
		return nameHashtag;
	}

	public void setNameHashtag(String nameHashtag) {
		this.nameHashtag = nameHashtag;
	}

	public Date getDateOfAnalysis() {
		return dateOfAnalysis;
	}

	public void setDateOfAnalysis(Date dateOfAnalysis) {
		this.dateOfAnalysis = dateOfAnalysis;
	}

	public Hashtag getHashtag() {
		return hashtag;
	}

	public void setHashtag(Hashtag hashtag) {
		this.hashtag = hashtag;
	}

	public Analysis() {
	}

	public Analysis(int id, String count, String nameHashtag, Date dateOfAnalysis, Hashtag hashtag) {
		super();
		this.id = id;
		this.count = count;
		this.nameHashtag = nameHashtag;
		this.dateOfAnalysis = dateOfAnalysis;
		this.hashtag = hashtag;
	}
	
}
