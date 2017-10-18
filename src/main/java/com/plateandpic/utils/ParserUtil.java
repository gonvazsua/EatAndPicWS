package com.plateandpic.utils;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.Set;

import com.plateandpic.models.Category;

/**
 * @author gonzalo
 *
 */
public class ParserUtil {
	
	private static final String SEPARATOR = ",";
	private static final String BLANK = " ";
	
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
	
	/**
	 * @param Set<Category>
	 * @return
	 * 
	 * Convert the categories'set to a String separated by comma
	 * 
	 */
	public static String getCategoriesAsString(Set<Category> categories){
		
		StringBuilder sb = new StringBuilder(100);
		Iterator<Category> itCategories = categories.iterator();
		Category current = null;
		String parsedString;
		
		while(itCategories.hasNext()){
			
			current = itCategories.next();
			sb.append(current.getCategoryName()).append(SEPARATOR);
			
		}
		
		//Remove last ocurrence of SEPARATOR
		parsedString = sb.substring(0, sb.length() - 1);
		
		return parsedString;
		
	}
	
	/**
	 * @param apiAddress
	 * @return
	 * 
	 * Extract the city name of the Adreess line from API Places:
	 * Ex: Calle Eloy Gonzalo, 10, 28010 Madrid, España -> Return Madrid
	 */
	public static String getCityNameFromFormattedAPIAddress(String apiAddress){
		
		String cityNameAndPC = "";
		String cityName = "";
		String[] splitted = null;
		
		if(apiAddress != null){
			
			splitted = apiAddress.split(SEPARATOR);
			cityNameAndPC = splitted[splitted.length - 2];
			
			splitted = cityNameAndPC.split(BLANK);
			
			if(splitted.length >= 2)
				cityName = splitted[splitted.length - 1];
				
		}
		
		return cityName;
	}
	
	/**
	 * @param apiAddress
	 * @return
	 * 
	 * Extract the city name of the Adreess line from API Places:
	 * Ex: Calle Eloy Gonzalo, 10, 28010 Madrid, España -> Return Calle Eloy Gonzalo, 10
	 */
	public static String getAddressFromFormattedAddress(String apiAddress){
		
		String address = "";
		String[] splitted = null;
		
		if(apiAddress != null){
			
			splitted = apiAddress.split(SEPARATOR);
			
			for(int i = 0; i < splitted.length - 2; i++){
				
				if(i == 0){
					address = splitted[0];
				} else if(i != (splitted.length - 1)){
					address = address + SEPARATOR + splitted[i];
				}
				
			}
			
		}
		
		return address;
	}

}
