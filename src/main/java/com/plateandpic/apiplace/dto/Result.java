package com.plateandpic.apiplace.dto;

import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.plateandpic.constants.MessageConstants;
import com.plateandpic.exceptions.CityException;
import com.plateandpic.exceptions.ProvinceException;

/**
 * @author gonzalo
 *
 */
public class Result {
	
	private Geometry geometry;
	
	@JsonIgnore
	private String icon;
	
	@JsonIgnore
	private String id;
	
	private String formatted_address;
	
	private String name;
	
	@JsonIgnore
	private String opening_hours;
	
	@JsonIgnore
	private String photos;
	
	private String place_id;
	
	@JsonIgnore
	private String price_level;
	
	private Double rating;
	
	@JsonIgnore
	private String reference;
	
	@JsonIgnore
	private String scope;
	
	@JsonIgnore
	private String types;
	
	private List<Address_Components> address_components;
	
	/**
	 * 
	 */
	public Result(){}

	/**
	 * @return the geometry
	 */
	public Geometry getGeometry() {
		return geometry;
	}

	/**
	 * @param geometry the geometry to set
	 */
	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @param icon the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the place_id
	 */
	public String getPlace_id() {
		return place_id;
	}

	/**
	 * @param place_id the place_id to set
	 */
	public void setPlace_id(String place_id) {
		this.place_id = place_id;
	}

	/**
	 * @return the rating
	 */
	public Double getRating() {
		return rating;
	}

	/**
	 * @param rating the rating to set
	 */
	public void setRating(Double rating) {
		this.rating = rating;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the opening_hours
	 */
	public String getOpening_hours() {
		return opening_hours;
	}

	/**
	 * @param opening_hours the opening_hours to set
	 */
	public void setOpening_hours(String opening_hours) {
		this.opening_hours = opening_hours;
	}

	/**
	 * @return the photos
	 */
	public String getPhotos() {
		return photos;
	}

	/**
	 * @param photos the photos to set
	 */
	public void setPhotos(String photos) {
		this.photos = photos;
	}

	/**
	 * @return the reference
	 */
	public String getReference() {
		return reference;
	}

	/**
	 * @param reference the reference to set
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * @return the scope
	 */
	public String getScope() {
		return scope;
	}

	/**
	 * @param scope the scope to set
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}

	/**
	 * @return the types
	 */
	public String getTypes() {
		return types;
	}

	/**
	 * @param types the types to set
	 */
	public void setTypes(String types) {
		this.types = types;
	}

	/**
	 * @return the price_level
	 */
	public String getPrice_level() {
		return price_level;
	}

	/**
	 * @param price_level the price_level to set
	 */
	public void setPrice_level(String price_level) {
		this.price_level = price_level;
	}

	/**
	 * @return the formatted_address
	 */
	public String getFormatted_address() {
		return formatted_address;
	}

	/**
	 * @param formatted_address the formatted_address to set
	 */
	public void setFormatted_address(String formatted_address) {
		this.formatted_address = formatted_address;
	}

	/**
	 * @return the address_components
	 */
	public List<Address_Components> getAddress_components() {
		return address_components;
	}

	/**
	 * @param address_components the address_components to set
	 */
	public void setAddress_components(List<Address_Components> address_components) {
		this.address_components = address_components;
	}
	
	/**
	 * @return
	 * @throws CityException
	 * 
	 * Get city name with the long_name attribute from Api response:
	 * 
	 * long_name	"Arcos de la Frontera"
	 * short_name	"Arcos de la Frontera"
	 * types		["locality", "political"]
	 */
	public String getCityFromAddressComponent() throws CityException{
		
		String cityName = "";
		Iterator<Address_Components> itAddressComponents = null;
		Address_Components address_comp = null;
		
		if(this.getAddress_components() == null){
			throw new CityException(MessageConstants.CITY_NOT_FOUND);
		}
		
		itAddressComponents = this.getAddress_components().iterator();
		
		while(itAddressComponents.hasNext()){
			
			address_comp = itAddressComponents.next();
			
			if(address_comp.isCityComponent()){
				cityName = address_comp.getLong_name();
				break;
			}
			
		}
		
		return cityName;
		
	}
	
	/**
	 * @return
	 * @throws CityException
	 * 
	 * Get city name with the long_name attribute from Api response:
	 * 
	 * long_name	"Cadiz"
	 * short_name	"Cadiz"
	 * types		["administrative_area_level_2", "political"]
	 */
	public String getProvinceFromAddresComponent() throws ProvinceException {
		
		String provinceName = "";
		Iterator<Address_Components> itAddressComponents = null;
		Address_Components address_comp = null;
		
		if(this.getAddress_components() == null) {
			throw new ProvinceException(MessageConstants.PROVINCE_NOT_FOUND);
		}
		
		itAddressComponents = this.getAddress_components().iterator();
		
		while(itAddressComponents.hasNext()){
			
			address_comp = itAddressComponents.next();
			
			if(address_comp.isProvinceComponent()){
				provinceName = address_comp.getLong_name();
				break;
			}
			
		}
		
		return provinceName;
	}

}
