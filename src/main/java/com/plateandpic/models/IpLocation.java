package com.plateandpic.models;

import com.maxmind.geoip.Location;

public class IpLocation {
	
	private String longitude;
	
	private String latitude;
	
	private String cityName;
	
	
	public IpLocation(){}
	
	public IpLocation(Location location){
		
		this.longitude = String.valueOf(location.longitude);
		this.latitude = String.valueOf(location.latitude);
		this.cityName = String.valueOf(location.city);
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

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
}
