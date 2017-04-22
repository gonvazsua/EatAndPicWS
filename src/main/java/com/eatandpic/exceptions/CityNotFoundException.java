package com.eatandpic.exceptions;

public class CityNotFoundException extends Exception {
	
	public CityNotFoundException(){
		super();
	}
	
	public CityNotFoundException(String message){
		super(message);
	}

}
