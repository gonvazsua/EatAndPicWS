package com.plateandpic.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Comment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private long commentId;
	
	@ManyToOne
	private User user;
	
	@NotNull
	@Size(min = 1, max = 150)
    private String comment;
	
	@NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date registeredOn;
	
	@ManyToOne
	private PlatePicture platePicture;

	public long getCommentId() {
		return commentId;
	}

	public void setCommentId(long commentId) {
		this.commentId = commentId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getRegisteredOn() {
		return registeredOn;
	}

	public void setRegisteredOn(Date registeredOn) {
		this.registeredOn = registeredOn;
	}

	public PlatePicture getPlatePicture() {
		return platePicture;
	}

	public void setPlatePicture(PlatePicture platePicture) {
		this.platePicture = platePicture;
	}
	
	
	
}
