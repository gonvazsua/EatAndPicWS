package com.plateandpic.factory;

import java.io.File;
import java.io.IOException;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import com.plateandpic.constants.ConstantsProperties;
import com.plateandpic.exceptions.IPNotFoundException;
import com.plateandpic.models.IpLocation;
import com.plateandpic.utils.GetPropertiesValues;

public class LocationFactory {
	
	public static IpLocation getLocationFromHost(String host) throws IPNotFoundException, IOException{
		
		LookupService lookupService = null;
		File databaseFile = null; 
		Location location = null;
		IpLocation ipLocation = null;
		GetPropertiesValues properties = new GetPropertiesValues(ConstantsProperties.APPLICATION_PROPERTIES);
		
		
		databaseFile = FileFactory.getFileFromFileSystem(properties.getValue(ConstantsProperties.IP_LOCATION_FILE_PATH));
		
		lookupService = new LookupService(databaseFile, LookupService.GEOIP_STANDARD);
		
		//location = lookupService.getLocation(host);
		location = lookupService.getLocation("188.78.186.80");
		
		if(location == null)
			throw new IPNotFoundException("Not found location for: " + host);
		
		ipLocation = new IpLocation(location);
		
		return ipLocation;
		
	}

}
