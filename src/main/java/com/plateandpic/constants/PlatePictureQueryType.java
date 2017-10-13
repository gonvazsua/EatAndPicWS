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
	TYPE_FOLLOWERS(2);
	
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
