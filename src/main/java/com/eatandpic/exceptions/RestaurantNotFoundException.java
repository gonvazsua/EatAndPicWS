package com.eatandpic.exceptions;

public class RestaurantNotFoundException extends Exception {
	
	public RestaurantNotFoundException(){
		super();
	}
	
	public RestaurantNotFoundException(String message){
		super(message);
	}

}
