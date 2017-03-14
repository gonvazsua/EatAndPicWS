package com.eatandpic.validator;

import com.eatandpic.exceptions.UserNotValidException;
import com.eatandpic.models.User;

public class UserValidator {
	
	public static Boolean validateUser(User user){
		
		Boolean isValid = true;
		
		try{
			
			validateUsername(user.getUsername());
			
		} catch (UserNotValidException e) {
			
			isValid = false;
		
		}
		
		return isValid;
		
	}
	
	public static void validateUsername(String username) throws UserNotValidException{
		
		if(!"".equals(username) && username.length() > 50){
			
			throw new UserNotValidException("Username not valid");
			
		}
		
	}
	
	public static void validateEmail(String email) throws UserNotValidException {
		
		if(!"".equals(email) && email.length() > 50){
			
			throw new UserNotValidException("Email not valid");
			
		}

	}

}
