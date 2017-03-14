package com.eatandpic.exceptions;

public class UserNotValidException extends Exception {
	
	  public UserNotValidException() { 
		  super(); 
	  }
	  
	  public UserNotValidException(String message) { 
		  super(message); 
	  }
	  
	  public UserNotValidException(String message, Throwable cause) { 
		  super(message, cause); 
	  }
	  
	  public UserNotValidException(Throwable cause) { 
		  super(cause); 
	  }
}