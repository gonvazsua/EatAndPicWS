package com.plateandpic.validator;

import com.plateandpic.constants.MessageConstants;
import com.plateandpic.exceptions.PlateAndPicException;
import com.plateandpic.exceptions.UserException;
import com.plateandpic.security.JwtSignUpRequest;
import com.plateandpic.utils.ValidationUtils;

/**
 * @author gonzalo
 *
 */
public class JwtSignUpRequestValidator {
	
	private JwtSignUpRequest request;
	
	private static final Integer LENGTH_USERNAME = 40;
	private static final Integer LENGTH_EMAIL = 50;
	private static final Integer LENGTH_PASSWORD = 20;
	
	/**
	 * @param request
	 */
	public JwtSignUpRequestValidator(JwtSignUpRequest request){
		
		this.request = request;
		
	}
	
	/**
	 * @throws PlateAndPicException
	 */
	public void validate() throws PlateAndPicException{
		
		this.validateMandatoryFields();
		
		this.validateLength();
		
		this.validatePasswords();
		
	}
	
	/**
	 * @throws PlateAndPicException
	 */
	private void validateMandatoryFields() throws PlateAndPicException{
		
		ValidationUtils.validateMandatory(request.getUsername(), getException(MessageConstants.USER_USERNAME_EMPTY));
		ValidationUtils.validateMandatory(request.getEmail(), getException(MessageConstants.USER_EMAIL_EMPTY));
		ValidationUtils.validateMandatory(request.getPassword(), getException(MessageConstants.USER_PASSWORD_EMPTY));
		ValidationUtils.validateMandatory(request.getRepeatPassword(), getException(MessageConstants.USER_PASSWORD_EMPTY));
		
	}
	
	/**
	 * @throws PlateAndPicException
	 */
	private void validateLength() throws PlateAndPicException{
		
		ValidationUtils.validateMandatoryStringLength(request.getUsername(), LENGTH_USERNAME, getException(MessageConstants.USER_USERNAME_TOO_LONG));
		ValidationUtils.validateMandatoryStringLength(request.getEmail(), LENGTH_EMAIL, getException(MessageConstants.USER_EMAIL_TOO_LONG));
		ValidationUtils.validateMandatoryStringLength(request.getPassword(), LENGTH_PASSWORD, getException(MessageConstants.USER_PASSWORD_TOO_LONG));
		
	}
	
	/**
	 * @throws PlateAndPicException
	 */
	private void validatePasswords() throws PlateAndPicException{
		
		ValidationUtils.validateEquals(request.getPassword(), request.getRepeatPassword(), getException(MessageConstants.USER_PASSWORDS_NOT_MATCH));
		
	}
	
	/**
	 * @param message
	 * @return
	 */
	private UserException getException(String message){
		
		return new UserException(message);
		
	}

}
