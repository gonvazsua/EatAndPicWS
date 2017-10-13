package com.plateandpic.utils;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author gonzalo
 *
 */
public class DateUtils {
	
	public static final String DATE_FORMAT = "dd/MM/yyyy";
	public static final String SEPARATOR = "-";
	public static final String SLASH = "/";
	
	/**
	 * @param date
	 * @return
	 * 
	 * Return a date string with format DD/MM/YYYY from Date object
	 */
	public static String getDateDDMMYYY(Date date){
		
		if(date == null)
			return null;
		
		return new SimpleDateFormat(DATE_FORMAT).format(date);
		
	}
	
	/**
	 * @param date
	 * @return
	 * 
	 * Return a date string with format DD/MM/YYYY from YYYY-MM-DD HH:MM:SS
	 */
	public static String getDDMMYYY(String dateYYYY_MM_DD){
		
		String formatedDate = null;
		
		if(dateYYYY_MM_DD == null || dateYYYY_MM_DD.isEmpty()){
		
			formatedDate = dateYYYY_MM_DD;
		
		} else if(dateYYYY_MM_DD.length() < 10) {
			
			formatedDate = null;
			
		} else {
			
			String[] data = dateYYYY_MM_DD.split("-");
			formatedDate = data[2] + SLASH + data[1] + SLASH + data[0];
			
		}
		
		return formatedDate;
		
	}
	
	/**
	 * @param date
	 * @return
	 * 
	 * Return a date string with format DD/MM/YYYY from Timestamp YYYY-MM-DD HH:MM:SS
	 */
	public static String timeStampToString(Timestamp timeStamp){
		
		String parsedDate = null;
		Date date = null;
		
		if(timeStamp != null){
			
			date = timeStamp;
			
		}
		
		parsedDate = getDateDDMMYYY(date);
		
		return parsedDate;
		
	}
	
}
