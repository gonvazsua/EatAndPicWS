package com.plateandpic.apiplace.dto;

import java.util.Iterator;
import java.util.List;

import com.plateandpic.constants.ApiPlacesConstants;

/**
 * @author gonzalo
 *
 */
public class Address_Components {
	
	private String long_name;
	private String short_name;
	private List<String> types;
	
	/**
	 * 
	 */
	public Address_Components(){}

	/**
	 * @return the long_name
	 */
	public String getLong_name() {
		return long_name;
	}

	/**
	 * @param long_name the long_name to set
	 */
	public void setLong_name(String long_name) {
		this.long_name = long_name;
	}

	/**
	 * @return the short_name
	 */
	public String getShort_name() {
		return short_name;
	}

	/**
	 * @param short_name the short_name to set
	 */
	public void setShort_name(String short_name) {
		this.short_name = short_name;
	}

	/**
	 * @return the types
	 */
	public List<String> getTypes() {
		return types;
	}

	/**
	 * @param types the types to set
	 */
	public void setTypes(List<String> types) {
		this.types = types;
	}
	
	/**
	 * @return
	 * 
	 * Return true if the address components is the City Name:
	 * types contains "locality"
	 */
	public Boolean isCityComponent(){
		
		Boolean contains = false;
		Iterator<String> itTypes = this.getTypes().iterator();
		String type = null;
		
		while(itTypes.hasNext()){
			
			type = itTypes.next();
			
			if(ApiPlacesConstants.TYPE_LOCALITY.equals(type)){
				contains = true;
				break;
			}
			
		}
		
		
		return contains;		
		
	}
	
	/**
	 * @return
	 * 
	 * Return true if the address components is the Province Name:
	 * types contains "administrative_area_level_2"
	 */
	public Boolean isProvinceComponent(){
		
		Boolean contains = false;
		Iterator<String> itTypes = this.getTypes().iterator();
		String type = null;
		
		while(itTypes.hasNext()){
			
			type = itTypes.next();
			
			if(ApiPlacesConstants.TYPE_PROVINCE.equals(type)){
				contains = true;
				break;
			}
			
		}
		
		
		return contains;		
		
	}

}
