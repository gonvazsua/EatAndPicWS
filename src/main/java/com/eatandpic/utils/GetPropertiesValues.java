package com.eatandpic.utils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class GetPropertiesValues {
	
	private String propFileName;
	
	public GetPropertiesValues(String propFileName){
		
		this.propFileName = propFileName;
		
	}
	
	public String getPropFileName(){
		return this.propFileName;
	}
	
	public String getValue(String key){
		
		String value = "";
		InputStream inputStream;
		
		try {
			
			Properties prop = new Properties();
 
			inputStream = getClass().getClassLoader().getResourceAsStream(this.getPropFileName());
			
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
			
			value = prop.getProperty(key);
			
		} catch (Exception e){
			value = "";
		}
		
		return value;
		
	}

}
