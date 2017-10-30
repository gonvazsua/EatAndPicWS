package com.plateandpic.apiplace.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author gonzalo
 *
 */
public class Geometry {
	
	private Location location;
	
	@JsonIgnore
	private String viewport;
	
	@JsonIgnore
	private String bounds;
	
	@JsonIgnore
	private String location_type;
	
	/**
	 * 
	 */
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

	/**
	 * @return the bounds
	 */
	public String getBounds() {
		return bounds;
	}

	/**
	 * @param bounds the bounds to set
	 */
	public void setBounds(String bounds) {
		this.bounds = bounds;
	}

	/**
	 * @return the location_type
	 */
	public String getLocation_type() {
		return location_type;
	}

	/**
	 * @param location_type the location_type to set
	 */
	public void setLocation_type(String location_type) {
		this.location_type = location_type;
	}

}
