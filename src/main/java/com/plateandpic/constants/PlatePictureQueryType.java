package com.plateandpic.constants;

/**
 * @author gonzalo
 * 
 * Constants for PlatePictures query type
 *
 */
public enum PlatePictureQueryType {
	
	/**
	 * Type Profile will be used when the platepictures to show are from the logged user
	 */
	TYPE_PROFILE(1),
	
	/**
	 * Type Followers will be used when the platepictures to show are from the followed users 
	 */
	TYPE_FOLLOWERS(2),
	
	/**
	 * Type Restaurant will be used when the platepictures to show are filtered by restaurantId
	 */
	TYPE_RESTAURANT(3),
	
	/**
	 * Type Plate will be used when the platepictures to show are filtered by plateId
	 */
	TYPE_PLATE(4);
	
	/**
	 * Aux. parameter
	 */
	private Integer type;
	
	/**
	 * @param type
	 */
	private PlatePictureQueryType(Integer type){
		this.type = type;
	}
	
	/**
	 * @return
	 */
	public Integer getType(){
		return this.type;
	}

}
