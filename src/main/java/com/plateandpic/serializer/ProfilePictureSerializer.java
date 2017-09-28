package com.plateandpic.serializer;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.plateandpic.exceptions.PlateAndPicException;
import com.plateandpic.factory.FileFactory;
import com.plateandpic.utils.GetPropertiesValues;

public class ProfilePictureSerializer extends JsonSerializer<String>{
	
	private static final Logger log = LoggerFactory.getLogger(ProfilePictureSerializer.class);
	
	private static String propertyFile = "application.properties";
	private static String key = "userProfilePicturesPath";

	@Override
	public void serialize(String picture, JsonGenerator jgen, SerializerProvider provider)
			throws IOException {
		
		try {
		
			//jgen.writeStartArray();
			
			GetPropertiesValues properties = new GetPropertiesValues(propertyFile);
			
			String path = properties.getValue(key);
			
			jgen.writeObject((String) FileFactory.getBase64FromProfilePictureName(path, picture));
			
		} catch (PlateAndPicException e) {
			log.error(e.toString());
			throw new IOException(e);
		}
		
		//jgen.writeEndArray();
		
	}
	
	
	
}
