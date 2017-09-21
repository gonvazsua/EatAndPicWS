package com.plateandpic.validator;

import com.plateandpic.constants.MessageConstants;
import com.plateandpic.exceptions.PasswordException;
import com.plateandpic.utils.UpdatePasswordRequest;

public class PasswordValidator {
	
	private UpdatePasswordRequest request;
	
	public PasswordValidator(UpdatePasswordRequest request){
		
		this.request = request;
		
	}
	
	public void validate() throws PasswordException{
		
		this.validateNewPasswordsNotEmpty();
		
		this.validatePasswordsAreEquals();
		
	}
	
	private void validateNewPasswordsNotEmpty() throws PasswordException{
		
		if(this.request.getNewPassword1() == null || this.request.getNewPassword1().isEmpty()){
			throw new PasswordException(MessageConstants.PASSWORD_NOT_VALID);
		}
		
		if(this.request.getNewPassword2() == null || this.request.getNewPassword2().isEmpty()){
			throw new PasswordException(MessageConstants.PASSWORD_NOT_VALID);
		}
		
	}
	
	private void validatePasswordsAreEquals() throws PasswordException{
		
		if(!this.request.getNewPassword1().equals(this.request.getNewPassword2())){
			throw new PasswordException(MessageConstants.PASSWORD_NOT_EQUALS);
		}
		
	}

}
