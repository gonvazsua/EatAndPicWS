package com.plateandpic.exceptions;

/**
 * @author gonzalo
 *
 */
public class RestaurantNotFoundException extends Exception {
	
	/**
	 * 
	 */
	public RestaurantNotFoundException(){
		super();
	}
	
	/**
	 * @param message
	 */
	public RestaurantNotFoundException(String message){
		super(message);
	}

}
