package project.instagram.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "DataCrawls")
@EntityListeners(AuditingEntityListener.class)
public class DataCrawl implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@Column(name = "codePost", length = 15)
	private String codePost;
	
	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "issuedDate")
	private Date issuedDate;
	
	@Column(name = "caption")
	@Type(type = "text")
	private String caption;
	
	@CreatedDate
	@Column(name = "createdDatePost")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDatePost;
	
	@NotNull
	@Column(name = "username", length = 255)
	private String username;
	
	@Column(name = "relatedToHashtag")
	@Type(type = "text")
	private String relatedToHashtag;
	
	@ManyToOne
	@JoinColumn(name = "hashtag")
	private Hashtag hashtag;

	public DataCrawl() {
	}

	public DataCrawl(@NotNull String codePost, Date issuedDate, String caption, Date createdDatePost,
			@NotNull String username, String relatedToHashtag, Hashtag hashtag) {
		this.codePost = codePost;
		this.issuedDate = issuedDate;
		this.caption = caption;
		this.createdDatePost = createdDatePost;
		this.username = username;
		this.relatedToHashtag = relatedToHashtag;
		this.hashtag = hashtag;
	}

	public String getCodePost() {
		return codePost;
	}

	public void setCodePost(String codePost) {
		this.codePost = codePost;
	}

	public Date getIssuedDate() {
		return issuedDate;
	}

	public void setIssuedDate(Date issuedDate) {
		this.issuedDate = issuedDate;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public Date getCreatedDatePost() {
		return createdDatePost;
	}

	public void setCreatedDatePost(Date createdDatePost) {
		this.createdDatePost = createdDatePost;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRelatedToHashtag() {
		return relatedToHashtag;
	}

	public void setRelatedToHashtag(String relatedToHashtag) {
		this.relatedToHashtag = relatedToHashtag;
	}

	public Hashtag getHashtag() {
		return hashtag;
	}

	public void setHashtag(Hashtag hashtag) {
		this.hashtag = hashtag;
	}

}
