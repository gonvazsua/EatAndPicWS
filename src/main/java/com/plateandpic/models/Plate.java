package com.plateandpic.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.classmate.types.TypePlaceHolder;

/**
 * @author gonzalo
 *
 */
@Entity
@Table(name="plate")
public class Plate {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private long plateId;
	
	@NotNull
	@Size(min = 4, max = 50)
	private String name;
	
	@ManyToOne
	private Restaurant restaurant;
	
	@ManyToOne
	private PlateType plateType;
	
	@NotNull
	private Boolean active;
	
	
	/**
	 * @return
	 */
	public long getPlateId() {
		return plateId;
	}

	/**
	 * @param plateId
	 */
	public void setPlateId(long plateId) {
		this.plateId = plateId;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return
	 */
	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	/**
	 * @return
	 */
	public PlateType getPlateType() {
		return plateType;
	}

	/**
	 * @param plateType
	 */
	public void setPlateType(PlateType plateType) {
		this.plateType = plateType;
	}

	/**
	 * @return
	 */
	public Boolean getActive() {
		return active;
	}

	/**
	 * @param active
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}

}
