package com.plateandpic.apiplace.dto;

import com.plateandpic.constants.ApiPlacesConstants;
import com.plateandpic.exceptions.PlateAndPicException;

/**
 * @author gonzalo
 *
 */
public class ApiCityRequest extends Api {
	
	private String latitude;
	private String longitude;
	
	/**
	 * @param endpoint
	 * @param latitude
	 * @param longitude
	 * @param apiKey
	 */
	public ApiCityRequest(String endpoint, String latitude, String longitude, String apiKey){
		
		super(endpoint, apiKey);
		
		this.latitude = latitude;
		this.longitude = longitude;
		
	}

	/**
	 * @return the latitude
	 */
	public String getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public String getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	
	/* (non-Javadoc)
	 * @see com.plateandpic.apiplace.dto.Api#buildUrl()
	 * 
	 * Ex: https://maps.googleapis.com/maps/api/geocode/json?latlng=40.714224,-73.961452&key=YOUR_API_KEY
	 */
	@Override
	public String buildUrl() throws PlateAndPicException {
		
		StringBuilder url = new StringBuilder(100);
		
		url.append(super.getEndpoint());
		url.append(ApiPlacesConstants.LATLNG)
			.append(this.latitude).append(ApiPlacesConstants.COMMA).append(this.longitude);
		url.append(ApiPlacesConstants.AMPERSAND);
		url.append(ApiPlacesConstants.KEY).append(super.getApiKey());
		
		return url.toString();
		
	}

}
