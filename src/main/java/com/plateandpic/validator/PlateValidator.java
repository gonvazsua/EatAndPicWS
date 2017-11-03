package com.plateandpic.validator;

import com.plateandpic.constants.MessageConstants;
import com.plateandpic.exceptions.PlateAndPicException;
import com.plateandpic.exceptions.PlateException;
import com.plateandpic.response.PlateResponse;
import com.plateandpic.utils.ValidationUtils;

/**
 * @author gonzalo
 *	
 * PlateResponse validator
 */
public class PlateValidator {
	
	private Integer NAME_LENGTH = 50;
	
	private PlateResponse plate;
	
	/**
	 * @param plate
	 */
	public PlateValidator(PlateResponse plate) {
		
		this.plate = plate;
		
	}
	
	/**
	 * @throws PlateAndPicException
	 * 
	 * Validate PlateResponse object
	 */
	public void validate() throws PlateAndPicException{
		
		ValidationUtils.validateMandatoryStringLength(plate.getPlateName(), NAME_LENGTH, getPlateException(MessageConstants.PLATE_NAME_NOT_NULL));
		
		ValidationUtils.validateMandatory(plate.getRestaurantId(), getPlateException(MessageConstants.PLATE_RESTAURANT_NOT_NULL));
		
	}
	
	/**
	 * @param message
	 * @return
	 * 
	 * Get a PlateException instance
	 */
	private PlateException getPlateException(String message){
		
		return new PlateException(message);
		
	}

}
