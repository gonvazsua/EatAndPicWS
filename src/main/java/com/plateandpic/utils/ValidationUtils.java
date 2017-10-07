package com.plateandpic.utils;

import com.plateandpic.exceptions.PlateAndPicException;

public class ValidationUtils {
	
	public static void validateMandatoryStringLength(String toValidate, Integer length, PlateAndPicException e) throws PlateAndPicException{
		
		if(toValidate == null || toValidate.length() > length){
			throw e;
		}
		
	}

}
