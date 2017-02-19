package com.eatandpic.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="plate")
public class Plate {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private long plateId;
	
	@NotNull
	private String name;
	
	@ManyToOne
	private Restaurant restaurant;

}
