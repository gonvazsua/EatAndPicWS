package com.eatandpic.models;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="picture")
public class Picture {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private long pictureId;
	
	@NotNull
	private String pictureLocation;
	
	@ManyToOne
	private Plate plate;
	
	@ManyToOne
	private User user;
	
	@NotNull
	private String title;
	
	@NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date registeredOn;
	
	@ManyToMany(targetEntity=User.class)
	private Set likes;
}
