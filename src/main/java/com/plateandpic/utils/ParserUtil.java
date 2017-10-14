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

}
