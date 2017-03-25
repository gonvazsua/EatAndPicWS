package com.eatandpic.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.classmate.types.TypePlaceHolder;

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
	
	@ManyToOne
	private PlateType plateType;

	
	
	public long getPlateId() {
		return plateId;
	}

	public void setPlateId(long plateId) {
		this.plateId = plateId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	public PlateType getPlateType() {
		return plateType;
	}

	public void setPlateType(PlateType plateType) {
		this.plateType = plateType;
	}

}
