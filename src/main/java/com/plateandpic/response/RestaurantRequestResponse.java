package com.plateandpic.response;

import java.util.Date;

/**
 * @author gonzalo
 *
 */
public class RestaurantRequestResponse {
	
	private Long restaurantId;
	private String name;
	private String address;
	private String phoneNumber;
	private String registeredOn;
	private Long cityId;
	private String cityName;
	private String priceAverage;
	private String picture;
	private String description;
	
	/**
	 * @return
	 */
	public Long getRestaurantId() {
		return restaurantId;
	}
	
	/**
	 * @param restaurantId
	 */
	public void setRestaurantId(Long restaurantId) {
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
	public String getRegisteredOn() {
		return registeredOn;
	}
	
	/**
	 * @param registeredOn
	 */
	public void setRegisteredOn(String registeredOn) {
		this.registeredOn = registeredOn;
	}
	
	/**
	 * @return
	 */
	public Long getCityId() {
		return cityId;
	}
	
	/**
	 * @param cityId
	 */
	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}
	
	/**
	 * @return
	 */
	public String getCityName() {
		return cityName;
	}
	
	/**
	 * @param cityName
	 */
	public void setCityName(String cityName) {
		this.cityName = cityName;
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
	public String getDescription() {
		return description;
	}

	/**
	 * @param picture
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}
