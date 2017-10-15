package com.plateandpic.apiplace.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Geometry {
	
	private Location location;
	
	@JsonIgnore
	private String viewport;
	
	public Geometry(){}

	/**
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

	/**
	 * @return the viewport
	 */
	public String getViewport() {
		return viewport;
	}

	/**
	 * @param viewport the viewport to set
	 */
	public void setViewport(String viewport) {
		this.viewport = viewport;
	}
	

}
