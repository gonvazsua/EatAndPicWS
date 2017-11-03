package com.plateandpic.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.plateandpic.exceptions.PlateAndPicException;

public class ValidationUtils {
	
	private static final Logger log = LoggerFactory.getLogger(ValidationUtils.class);
	
	/**
	 * @param toValidate
	 * @param length
	 * @param e
	 * @throws PlateAndPicException
	 * 
	 * Validate the length of a string passed as parameter and throw the PlateAndPicException if necessary
	 */
	public static void validateMandatoryStringLength(String toValidate, Integer length, PlateAndPicException e) throws PlateAndPicException{
		
		if(toValidate == null || toValidate.length() > length){
			log.error("Validation mandatory string lengh error. Field: " + toValidate + ", and message: " + e.getMessage());
			throw e;
		}
		
	}
	
	/**
	 * @param toValidate
	 * @param length
	 * @param e
	 * @throws PlateAndPicException
	 * 
	 * Validate the length of a no mandatory string passed as parameter and throw the PlateAndPicException if necessary
	 */
	public static void validateNoMandatoryStringLength(String toValidate, Integer length, PlateAndPicException e) throws PlateAndPicException{
		
		if(toValidate != null && toValidate.length() > length){
			log.error("Validation no mandatory string lengh error. Field: " + toValidate + ", and message: " + e.getMessage());
			throw e;
		}
		
	}
	
	/**
	 * @param toValidate
	 * @param e
	 * @throws PlateAndPicException
	 * 
	 * Validate not null object and throw exception if necssary
	 */
	public static void validateMandatory(Object toValidate, PlateAndPicException e) throws PlateAndPicException {
		
		if(toValidate == null){
			log.error("Validation mandatory error. Field: " + toValidate + ", and message: " + e.getMessage());
			throw e;
		}
		
	}

}
