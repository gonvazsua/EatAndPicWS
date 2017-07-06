package com.plateandpic.utils;

public class StringUtils {
	
	public static String validateLength(String text, Integer length){
		
		String textAux = text;
		
		if(textAux.length() > length){
			textAux = text.substring(0, length);
		}
		
		return textAux;
		
	}

}
