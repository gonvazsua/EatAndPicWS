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

/**
 * @author gonzalo
 *
 */
@Entity
@Table(name="restaurant")
public class Restaurant {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private long restaurantId;
	
	@NotNull
	@Size(min = 4, max = 50)
	private String name;
	
	@Size(min = 4, max = 50)
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
	
	@Size(min = 1, max = 200)
	private String description;
	
	@NotNull
	private Boolean active;
	
	
	/**
	 * @return
	 */
	public long getRestaurantId() {
		return restaurantId;
	}

	/**
	 * @param restaurantId
	 */
	public void setRestaurantId(long restaurantId) {
		this.restaurantId = restaurantId;
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
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return
	 */
	public Date getRegisteredOn() {
		return registeredOn;
	}

	/**
	 * @param registeredOn
	 */
	public void setRegisteredOn(Date registeredOn) {
		this.registeredOn = registeredOn;
	}

	/**
	 * @return
	 */
	public Set getCategories() {
		return categories;
	}

	/**
	 * @param categories
	 */
	public void setCategories(Set categories) {
		this.categories = categories;
	}

	/**
	 * @return
	 */
	public City getCity() {
		return city;
	}

	/**
	 * @param city
	 */
	public void setCity(City city) {
		this.city = city;
	}

	/**
	 * @return
	 */
	public String getPriceAverage() {
		return priceAverage;
	}

	/**
	 * @param priceAverage
	 */
	public void setPriceAverage(String priceAverage) {
		this.priceAverage = priceAverage;
	}

	/**
	 * @return
	 */
	public String getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return
	 */
	public String getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return
	 */
	public String getPicture() {
		return picture;
	}

	/**
	 * @param picture
	 */
	public void setPicture(String picture) {
		this.picture = picture;
	}

	/**
	 * @return
	 */
	public String getApiPlaceId() {
		return apiPlaceId;
	}

	/**
	 * @param apiPlaceId
	 */
	public void setApiPlaceId(String apiPlaceId) {
		this.apiPlaceId = apiPlaceId;
	}

	/**
	 * @return
	 */
	public Double getRating() {
		return rating;
	}

	/**
	 * @param rating
	 */
	public void setRating(Double rating) {
		this.rating = rating;
	}

	/**
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
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
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 * 
	 * Two restaurants are equals if they have the same API_PLACE_ID and NAME
	 */
	@Override
    public boolean equals(Object obj) {
		
        if (obj == null) return false;
        
        if (getClass() != obj.getClass()) return false;
        
        final Restaurant other = (Restaurant) obj;
        
        //Equals apiPlaceId
        boolean sameA = (this.apiPlaceId == other.apiPlaceId) 
        		|| (this.apiPlaceId != null && this.apiPlaceId.equalsIgnoreCase(other.apiPlaceId)
        );
        
        if (!sameA) return false;
        
        //Equals name
        boolean sameB = (this.name == other.name) 
        		|| (this.name != null && this.name.equalsIgnoreCase(other.name));
        
        if (!sameB) return false;
        
        return true;
    }
	
}
