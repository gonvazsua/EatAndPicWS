package com.plateandpic.apiplace.dto;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.plateandpic.constants.ApiPlacesConstants;
import com.plateandpic.exceptions.PlateAndPicException;

/**
 * @author gonzalo
 * 
 * DTO used for make a ApiPlace request
 * 
 * Extends from ApiRequest and implements the own buildURL method
 *
 */
public class ApiPlaceRequest {

	private String endpoint;
	private String querySearch;
	private String apiKey;
	private String types;
	private Integer radius;
	private Double latitude;
	private Double longitude;
	
	/**
	 * @param endpoint
	 * @param querySearch
	 * @param types
	 * @param apiKey
	 * @param radius
	 * @param latitude
	 * @param longitude
	 */
	public ApiPlaceRequest(String endpoint, String querySearch, String types, String apiKey,
			Integer radius, Double latitude, Double longitude) {
		
		this.endpoint = endpoint; 
		this.querySearch = querySearch;
		this.types = types;
		this.apiKey = apiKey;
		this.radius = radius;
		this.latitude = latitude;
		this.longitude = longitude;
		
	}
	
	/**
	 * @return
	 * 
	 * Encode name to convert whitespaces and illegal characters
	 * @throws PlateAndPicException 
	 */
	private String getFormatedLatLongQuerySearch() throws PlateAndPicException {
		
		String encodedName = "";
		
		try {
			
			encodedName = URLEncoder.encode(this.querySearch, ApiPlacesConstants.UTF8);
			
		} catch (UnsupportedEncodingException e) {
			throw new PlateAndPicException(e.getMessage());
		}
		
		return encodedName;
		
	}
	
	/**
	 * @return
	 * 
	 * Encode name to convert whitespaces and illegal characters
	 * @throws PlateAndPicException 
	 */
	private String getFormatedNoRadiusQuerySearch() throws PlateAndPicException {
		
		String encodedName = "";
		
		try {
			
			this.querySearch += ApiPlacesConstants.EN_ESPANA;
			
			encodedName = URLEncoder.encode(this.querySearch, ApiPlacesConstants.UTF8);
			
		} catch (UnsupportedEncodingException e) {
			throw new PlateAndPicException(e.getMessage());
		}
		
		return encodedName;
		
	}
	
	/**
	 * @return
	 * @throws PlateAndPicException
	 *  
	 * Build a URL of GET request
	 * Ex 1: https://maps.googleapis.com/maps/api/place/textsearch/json?types=restaurant&query=perrachi&language=es&radius=1000&location=40.4233873,-3.6927541&key=AIzaSyD_WL2_rolCu7nOpAbyDtv-uxdh7ZlIv8Q
	 * Ex 2: https://maps.googleapis.com/maps/api/place/textsearch/json?types=restaurant&query=cham+en+espa%C3%B1a&language=es&key=AIzaSyD_WL2_rolCu7nOpAbyDtv-uxdh7ZlIv8Q
	 *
	 */
	public String buildURL() throws PlateAndPicException {
		
		StringBuilder sb = new StringBuilder(100);
		
		sb.append(this.endpoint);
		sb.append(ApiPlacesConstants.TYPES).append(ApiPlacesConstants.RESTAURANT);
		sb.append(ApiPlacesConstants.AMPERSAND);
		sb.append(ApiPlacesConstants.LANGUAGE);
		sb.append(ApiPlacesConstants.AMPERSAND);
		
		if(this.latitude != null && this.longitude != null){
			
			sb.append(ApiPlacesConstants.RADIUS).append(this.radius);
			sb.append(ApiPlacesConstants.AMPERSAND);
			sb.append(ApiPlacesConstants.QUERY).append(this.getFormatedLatLongQuerySearch());
			sb.append(ApiPlacesConstants.AMPERSAND);
			sb.append(ApiPlacesConstants.LOCATION)
				.append(this.latitude).append(ApiPlacesConstants.COMMA).append(this.longitude);
			
		} else {
			
			sb.append(ApiPlacesConstants.QUERY).append(this.getFormatedNoRadiusQuerySearch());
			
		}
		
		sb.append(ApiPlacesConstants.AMPERSAND);
		sb.append(ApiPlacesConstants.KEY).append(this.apiKey);
		
		return sb.toString();
		
	}

}
