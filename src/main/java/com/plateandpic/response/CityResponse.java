package com.plateandpic.response;

import com.plateandpic.models.City;

/**
 * @author gonzalo
 *
 */
public class CityResponse {
	
	private Long cityId;
	private String cityName;
	private Long provinceId;
	private String provinceName;
	
	/**
	 * 
	 */
	public CityResponse(){
		
		this.cityId = null;
		this.cityName = "";
		this.provinceId = null;
		this.provinceName = "";
		
	}
	
	/**
	 * @param city
	 */
	public CityResponse(City city){
		
		this.cityId = city.getCityId();
		this.cityName = city.getName();
		this.provinceId = city.getProvince().getProvinceId();
		this.provinceName = city.getProvince().getName();
		
	}

	/**
	 * @return the cityId
	 */
	public Long getCityId() {
		return cityId;
	}

	/**
	 * @param cityId the cityId to set
	 */
	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	/**
	 * @return the cityName
	 */
	public String getCityName() {
		return cityName;
	}

	/**
	 * @param cityName the cityName to set
	 */
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	/**
	 * @return the provinceId
	 */
	public Long getProvinceId() {
		return provinceId;
	}

	/**
	 * @param provinceId the provinceId to set
	 */
	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	/**
	 * @return the provinceName
	 */
	public String getProvinceName() {
		return provinceName;
	}

	/**
	 * @param provinceName the provinceName to set
	 */
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

}
