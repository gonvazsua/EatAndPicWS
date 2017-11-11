package com.plateandpic.validator;

import com.plateandpic.constants.MessageConstants;
import com.plateandpic.exceptions.PasswordException;
import com.plateandpic.utils.UpdatePasswordRequest;

/**
 * @author gonzalo
 *
 */
public class PasswordValidator {
	
	private UpdatePasswordRequest request;
	
	/**
	 * @param request
	 */
	public PasswordValidator(UpdatePasswordRequest request){
		
		this.request = request;
		
	}
	
	/**
	 * @throws PasswordException
	 */
	public void validate() throws PasswordException{
		
		this.validateNewPasswordsNotEmpty();
		
		this.validatePasswordsAreEquals();
		
	}
	
	/**
	 * @throws PasswordException
	 */
	private void validateNewPasswordsNotEmpty() throws PasswordException{
		
		if(this.request.getNewPassword1() == null || this.request.getNewPassword1().isEmpty()){
			throw new PasswordException(MessageConstants.PASSWORD_NOT_VALID);
		}
		
		if(this.request.getNewPassword2() == null || this.request.getNewPassword2().isEmpty()){
			throw new PasswordException(MessageConstants.PASSWORD_NOT_VALID);
		}
		
	}
	
	/**
	 * @throws PasswordException
	 */
	private void validatePasswordsAreEquals() throws PasswordException{
		
		if(!this.request.getNewPassword1().equals(this.request.getNewPassword2())){
			throw new PasswordException(MessageConstants.PASSWORD_NOT_EQUALS);
		}
		
	}

}
