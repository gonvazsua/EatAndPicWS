package com.plateandpic.models;

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
import javax.validation.constraints.Size;

@Entity
@Table(name="restaurant")
public class Restaurant {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private long restaurantId;
	
	@NotNull
	@Size(min = 4, max = 50)
	private String name;
	
	@Size(min = 4, max = 200)
	private String address;
	
	@Size(min = 4, max = 12)
	private String phoneNumber;
	
	@NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date registeredOn;
	
	@ManyToMany(targetEntity=Category.class)
	private Set categories;
	
	@Size(min = 1, max = 50)
	private String longitude;
	
	@Size(min = 1, max = 50)
	private String latitude;
	
	@Size(min = 1, max = 100)
	private String apiPlaceId;
	
	@ManyToOne
	@NotNull
	private City city;
	
	@Size(min = 1, max = 12)
	private String priceAverage;
	
	@Size(min = 1, max = 50)
	private String picture;
	
	private Double rating;
	
	@Size(min = 1, max = 150)
	private String description;
	
	@NotNull
	private Boolean active;
	
	
	public long getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(long restaurantId) {
		this.restaurantId = restaurantId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Date getRegisteredOn() {
		return registeredOn;
	}

	public void setRegisteredOn(Date registeredOn) {
		this.registeredOn = registeredOn;
	}

	public Set getCategories() {
		return categories;
	}

	public void setCategories(Set categories) {
		this.categories = categories;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public String getPriceAverage() {
		return priceAverage;
	}

	public void setPriceAverage(String priceAverage) {
		this.priceAverage = priceAverage;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getApiPlaceId() {
		return apiPlaceId;
	}

	public void setApiPlaceId(String apiPlaceId) {
		this.apiPlaceId = apiPlaceId;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	
}
