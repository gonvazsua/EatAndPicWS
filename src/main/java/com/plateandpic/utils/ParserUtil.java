package com.plateandpic.utils;

import java.math.BigInteger;

/**
 * @author gonzalo
 *
 */
public class ParserUtil {
	
	/**
	 * @param toConvert
	 * @return
	 * 
	 * Convert any Integer not null in true, if it's null, then return false
	 * 
	 */
	public static Boolean convertNotNullToBoolean(Object toConvert){
		
		Boolean result = false;
		
		if(toConvert != null){
			result = true;
		}
		
		return result;
		
	}
	
	/**
	 * @param toConvert
	 * @return
	 * 
	 * Convert BigInteger to Long
	 * 
	 */
	public static Long bigIntegerToLong(BigInteger toConvert){
		
		Long result = null;
		
		if(toConvert != null){
		
			result = new Long(toConvert.toString());
			
		}
		
		return result;
		
	}
	
	/**
	 * @param toConvert
	 * @return
	 * 
	 * Convert BigInteger to Long
	 * 
	 */
	public static Integer bigIntegerToInteger(BigInteger toConvert){
		
		Integer result = null;
		
		if(toConvert != null){
		
			result = new Integer(toConvert.toString());
			
		}
		
		return result;
		
	}

}
