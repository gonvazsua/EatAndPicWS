package com.plateandpic.apiplace.dto;

import com.plateandpic.constants.ApiPlacesConstants;

/**
 * @author gonzalo
 * 
 * DTO used for make a ApiPlace request
 *
 */
public class ApiPlaceRequest {
	
	private String endpoint;
	private Integer radius;
	private String types;
	private Double latitude;
	private Double longitude;
	private String name;
	private String apiKey;
	
	/**
	 * @param endpoint
	 * @param radius
	 * @param type
	 * @param latitude
	 * @param longitude
	 * @param name
	 * @param apiKey
	 */
	public ApiPlaceRequest(String endpoint, Integer radius, String type, Double latitude,
			Double longitude, String name, String apiKey) {
		
		this.endpoint = endpoint;
		this.radius = radius;
		this.types = types;
		this.latitude = latitude;
		this.longitude = longitude;
		this.name = name;
		this.apiKey = apiKey;
		
	}
	
	
	/**
	 * @return
	 * 
	 * Build a URL of GET request
	 * Ex: https://maps.googleapis.com/maps/api/place/nearbysearch/json?radius=1000&types=restaurant&location=40.4233873,-3.6927541&name=perr&key=AIzaSyD_WL2_rolCu7nOpAbyDtv-uxdh7ZlIv8Q
	 */
	public String buildURL() {
		
		StringBuilder sb = new StringBuilder(100);
		
		sb.append(this.endpoint);
		sb.append(ApiPlacesConstants.RADIUS).append(this.radius);
		sb.append(ApiPlacesConstants.AMPERSAND);
		sb.append(ApiPlacesConstants.TYPES).append(ApiPlacesConstants.RESTAURANT);
		sb.append(ApiPlacesConstants.AMPERSAND);
		sb.append(ApiPlacesConstants.LOCATION)
			.append(this.latitude).append(ApiPlacesConstants.COMMA).append(this.longitude);
		sb.append(ApiPlacesConstants.AMPERSAND);
		sb.append(ApiPlacesConstants.NAME).append(this.name);
		sb.append(ApiPlacesConstants.AMPERSAND);
		sb.append(ApiPlacesConstants.KEY).append(this.apiKey);
		
		return sb.toString();
		
	}
}
