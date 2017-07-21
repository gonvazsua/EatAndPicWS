package com.plateandpic.utils;

/**
 * @author gonzalo
 *
 */
public class StringUtils {
	
	/**
	 * @param text
	 * @param length
	 * @return
	 */
	public static String validateLength(String text, Integer length){
		
		String textAux = text;
		
		if(textAux.length() > length){
			textAux = text.substring(0, length);
		}
		
		return textAux;
		
	}

}
