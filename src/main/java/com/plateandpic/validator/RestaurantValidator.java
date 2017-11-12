package com.plateandpic.validator;

import com.plateandpic.constants.MessageConstants;
import com.plateandpic.exceptions.PlateAndPicException;
import com.plateandpic.exceptions.RestaurantException;
import com.plateandpic.models.Restaurant;
import com.plateandpic.utils.ValidationUtils;

/**
 * @author gonzalo
 *
 */
public class RestaurantValidator {
	
	private Restaurant restaurant;
	
	private static final Integer LENGTH_50 = 50;
	private static final Integer LENGTH_200 = 200;
	private static final Integer LENGTH_12 = 12;
	
	/**
	 * @param restaurant
	 */
	public RestaurantValidator(Restaurant restaurant) {
		
		this.restaurant = restaurant;
		
	}
	
	/**
	 * @throws PlateAndPicException
	 */
	public void validate() throws PlateAndPicException{
		
		validateMandatoryFields();
		
		validateLengths();
		
	}
	
	/**
	 * @throws PlateAndPicException
	 */
	private void validateMandatoryFields() throws PlateAndPicException{
		
		ValidationUtils.validateMandatory(restaurant.getName(), getException(MessageConstants.RESTAURANT_NAME_NOT_NULL));
		ValidationUtils.validateMandatory(restaurant.getCity(), getException(MessageConstants.RESTAURANT_CITY_NOT_NULL));
		
	}
	
	/**
	 * @throws PlateAndPicException
	 */
	private void validateLengths() throws PlateAndPicException{
		
		ValidationUtils.validateNoMandatoryStringLength(restaurant.getName(), LENGTH_50, getException(MessageConstants.RESTAURANT_NAME_TOO_LONG));
		ValidationUtils.validateNoMandatoryStringLength(restaurant.getAddress(), LENGTH_50, getException(MessageConstants.RESTAURANT_ADDRESS_TOO_LONG));
		ValidationUtils.validateNoMandatoryStringLength(restaurant.getPhoneNumber(), LENGTH_12, getException(MessageConstants.RESTAURANT_PHONENUMBER_TOO_LONG));
		ValidationUtils.validateNoMandatoryStringLength(restaurant.getDescription(), LENGTH_200, getException(MessageConstants.RESTAURANT_DESCRIPTION_TOO_LONG));
		
	}
	
	/**
	 * @param message
	 * @return
	 */
	private RestaurantException getException(String message) {
		
		return new RestaurantException(message);
		
	}

}
