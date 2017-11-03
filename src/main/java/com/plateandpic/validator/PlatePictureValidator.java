package com.plateandpic.validator;

import com.plateandpic.constants.MessageConstants;
import com.plateandpic.exceptions.PlateAndPicException;
import com.plateandpic.exceptions.PlatePictureException;
import com.plateandpic.models.PlatePicture;
import com.plateandpic.utils.ValidationUtils;

/**
 * @author gonzalo
 *
 */
public class PlatePictureValidator {
	
	private static final Integer TITLE_LENGTH = 200;
	
	private PlatePicture platePicture;
	
	/**
	 * @param platePicture
	 */
	public PlatePictureValidator(PlatePicture platePicture){
		
		this.platePicture = platePicture;
		
	}
	
	/**
	 * @throws PlateAndPicException
	 */
	public void validate() throws PlateAndPicException{
		
		ValidationUtils.validateMandatory(platePicture.getPlate(), getException(MessageConstants.PLATE_NOT_FOUND));
		
		ValidationUtils.validateMandatory(platePicture.getUser(), getException(MessageConstants.USER_USER_NOT_FOUND));
		
		ValidationUtils.validateNoMandatoryStringLength(platePicture.getTitle(), TITLE_LENGTH, getException(MessageConstants.PLATEPICTURE_TITLE_TOO_LONG));
		
	}
	
	/**
	 * @param message
	 * @return
	 */
	private PlatePictureException getException(String message){
		
		return new PlatePictureException(message);
		
	}

}
