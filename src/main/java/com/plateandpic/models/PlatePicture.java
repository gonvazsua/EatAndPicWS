package com.plateandpic.models;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.plateandpic.factory.PlatePictureFactory;

@Entity
@Table(name="plate_picture")
public class PlatePicture {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private long platePictureId;
	
	@ManyToOne
	@NotNull
	private Plate plate;
	
	@ManyToOne
	private User user;
	
	private String title;
	
	@NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date registeredOn;
	
	@ManyToMany(targetEntity=User.class)
	private Set likes;
	
	@OneToMany(targetEntity=Comment.class, fetch=FetchType.LAZY)
	private Set comments;
	
	@NotNull
	@Size(min = 4, max = 50)
	private String picture;

	public long getPictureId() {
		return platePictureId;
	}

	public void setPictureId(long platePictureId) {
		this.platePictureId = platePictureId;
	}

	public Plate getPlate() {
		return plate;
	}

	public void setPlate(Plate plate) {
		this.plate = plate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getRegisteredOn() {
		return registeredOn;
	}

	public void setRegisteredOn(Date registeredOn) {
		this.registeredOn = registeredOn;
	}

	public Set getLikes() {
		return likes;
	}

	public void setLikes(Set likes) {
		this.likes = likes;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	public Set getComments() {
		return comments;
	}

	public void setComments(Set comments) {
		this.comments = comments;
	}

}
