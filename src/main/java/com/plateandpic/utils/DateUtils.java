package com.plateandpic.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	
	public static final String DATE_FORMAT = "dd/MM/yyyy";
	
	public static String getDateDDMMYYY(Date date){
		
		return new SimpleDateFormat(DATE_FORMAT).format(date);
		
	}
	
}
