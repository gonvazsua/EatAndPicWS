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
	 * @param vicinity
	 * @return
	 * 
	 * Extract the city name of the Adreess line from API Places:
	 * Ex: Calle Claudio Coelho, 1, Madrid
	 */
	public static String getCityNameFromVicinity(String vicinity){
		
		String cityName = "";
		String[] splitted = null;
		
		if(vicinity != null){
			
			splitted = vicinity.split(SEPARATOR);
			cityName = splitted[splitted.length - 1];
			
		}
		
		return cityName;
	}
	
	/**
	 * @param vicinity
	 * @return
	 * 
	 * Extract the city name of the Adreess line from API Places:
	 * Ex: Calle Claudio Coelho, 1, Madrid
	 */
	public static String getAddressFromVicinity(String vicinity){
		
		String address = "";
		String[] splitted = null;
		
		if(vicinity != null){
			
			splitted = vicinity.split(SEPARATOR);
			
			for(int i = 0; i < splitted.length; i++){
				
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
