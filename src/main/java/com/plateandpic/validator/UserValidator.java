package com.plateandpic.validator;

import com.plateandpic.models.User;

/**
 * @author gonzalo
 *
 */
public class UserValidator {
	
	private User user;
	
	/**
	 * @param user
	 */
	public UserValidator(User user){
		
		this.user = user;
		
	}
	
	/**
	 * @return
	 */
	public User getUser(){
		return this.user;
	}
	
	/**
	 * 
	 */
	public void validateUserForPersonalDataChange(){
		
		validateUsername();
		
		validateFistName();
		
		validateLastname();
		
		validateEmail();
		
	}
	
	/**
	 * 
	 */
	public void validateUsername() {
		
		if(!"".equals(this.getUser().getUsername()) && this.getUser().getUsername().length() > 50){
			
			this.getUser().setUsername(this.getUser().getUsername().substring(0, 50));
			
		}
		
	}
	
	/**
	 * 
	 */
	public void validateFistName() {
		
		if(this.getUser().getFirstname() != null 
				&& !"".equals(this.getUser().getFirstname()) 
				&& this.getUser().getFirstname().length() > 50){
			
			this.getUser().setFirstname(this.getUser().getFirstname().substring(0, 50));
			
		}
		
	}
	
	/**
	 * 
	 */
	public void validateLastname() {
		
		if(this.getUser().getLastname() != null 
				&& !"".equals(this.getUser().getLastname()) 
				&& this.getUser().getLastname().length() > 50){
			
			this.getUser().setLastname(this.getUser().getLastname().substring(0, 50));
			
		}
		
	}
	
	/**
	 * 
	 */
	public void validateEmail() {
		
		if(this.getUser().getEmail() != null 
				&& !"".equals(this.getUser().getEmail()) && this.getUser().getEmail().length() > 50){
			
			this.getUser().setEmail(this.getUser().getEmail().substring(0, 50));
			
		}

	}
	
	/**
	 * 
	 */
	public void validatePassword() {
		
		if(this.getUser().getPassword() != null 
				&& !"".equals(this.getUser().getPassword()) && this.getUser().getPassword().length() > 100){
			
			this.getUser().setEmail(this.getUser().getEmail().substring(0, 100));
			
		}

	}

}
