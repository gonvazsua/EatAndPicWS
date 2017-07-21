package com.plateandpic.exceptions;

/**
 * @author gonzalo
 *
 */
public class CityNotFoundException extends Exception {
	
	/**
	 * 
	 */
	public CityNotFoundException(){
		super();
	}
	
	/**
	 * @param message
	 */
	public CityNotFoundException(String message){
		super(message);
	}

}
