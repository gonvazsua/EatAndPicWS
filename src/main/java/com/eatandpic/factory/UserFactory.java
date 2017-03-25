package com.eatandpic.factory;

import com.eatandpic.models.User;

public class UserFactory {
	
	public static void copyFieldsFromPersonalDataChange(User userFrom, User userTo){
		
		userTo.setUsername(userFrom.getUsername());
		userTo.setFirstname(userFrom.getFirstname());
		userTo.setLastname(userFrom.getLastname());
		
	}
	
	public static void copyFieldsFromEmailChange(User userFrom, User userTo){
		
		userTo.setEmail(userFrom.getEmail());
		
	}
	
	public static void copyFieldsFromPasswordChange(User userFrom, User userTo){
		
		userTo.setPassword(userFrom.getPassword());
		
	}

}
