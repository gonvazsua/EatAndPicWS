package com.plateandpic.response;

import com.plateandpic.models.Plate;
import com.plateandpic.utils.DateUtils;

/**
 * @author gonzalo
 *
 */
public class PlateResponse {
	
	private Long plateId;
	private String plateName;
	private Long restaurantId;
	private String restaurantName;
	private String plateType;
	private Boolean plateActive;
	
	/**
	 * @param plate
	 */
	public PlateResponse(Plate plate){
		
		this.plateId = plate.getPlateId();
		this.plateName = plate.getName();
		this.restaurantId = plate.getRestaurant().getRestaurantId();
		this.restaurantName = plate.getRestaurant().getName();
		
		if(plate.getPlateType() != null){
			this.plateType = plate.getPlateType().getName();
		}
		
		this.plateActive = plate.getActive();
		
	}

	/**
	 * @return the plateId
	 */
	public Long getPlateId() {
		return plateId;
	}

	/**
	 * @param plateId the plateId to set
	 */
	public void setPlateId(Long plateId) {
		this.plateId = plateId;
	}

	/**
	 * @return the plateName
	 */
	public String getPlateName() {
		return plateName;
	}

	/**
	 * @param plateName the plateName to set
	 */
	public void setPlateName(String plateName) {
		this.plateName = plateName;
	}

	/**
	 * @return the restaurantId
	 */
	public Long getRestaurantId() {
		return restaurantId;
	}

	/**
	 * @param restaurantId the restaurantId to set
	 */
	public void setRestaurantId(Long restaurantId) {
		this.restaurantId = restaurantId;
	}

	/**
	 * @return the restaurantName
	 */
	public String getRestaurantName() {
		return restaurantName;
	}

	/**
	 * @param restaurantName the restaurantName to set
	 */
	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}

	/**
	 * @return the plateType
	 */
	public String getPlateType() {
		return plateType;
	}

	/**
	 * @param plateType the plateType to set
	 */
	public void setPlateType(String plateType) {
		this.plateType = plateType;
	}

	/**
	 * @return the plateActive
	 */
	public Boolean getPlateActive() {
		return plateActive;
	}

	/**
	 * @param plateActive the plateActive to set
	 */
	public void setPlateActive(Boolean plateActive) {
		this.plateActive = plateActive;
	}

}
