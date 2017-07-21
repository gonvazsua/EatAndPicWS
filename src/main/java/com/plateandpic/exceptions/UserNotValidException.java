package com.plateandpic.exceptions;

/**
 * @author gonzalo
 *
 */
public class UserNotValidException extends Exception {
	
	  /**
	 * 
	 */
	public UserNotValidException() { 
		  super(); 
	  }
	  
	  /**
	 * @param message
	 */
	public UserNotValidException(String message) { 
		  super(message); 
	  }
	  
	  /**
	 * @param message
	 * @param cause
	 */
	public UserNotValidException(String message, Throwable cause) { 
		  super(message, cause); 
	  }
	  
	  /**
	 * @param cause
	 */
	public UserNotValidException(Throwable cause) { 
		  super(cause); 
	  }
}