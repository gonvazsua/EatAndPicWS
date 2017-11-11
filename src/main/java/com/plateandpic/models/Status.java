package com.plateandpic.models;

/**
 * @author gonzalo
 *
 */
public enum Status {
	
	/**
	 * Inactive user
	 */
	INACTIVE(0),
	
	/**
	 * Active user 
	 */
	ACTIVE(1),
	
	/**
	 * Pending of restaurant owner confirmation
	 */
	RESTAURANT_PENDING(2);
	
	private Integer code;
	
	/**
	 * @param code
	 */
	private Status(Integer code){
		this.code = code;
	}
	
	/**
	 * @return
	 */
	public Integer getCode(){
		return this.code;
	}
	
}
