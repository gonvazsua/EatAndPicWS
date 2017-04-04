package com.eatandpic.factory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.eatandpic.constants.ConstantsProperties;
import com.eatandpic.utils.GetPropertiesValues;

public class FileFactory {
	
	private static final String JPG = ".jpg";
	private static final String BASE_64 = "data:image/png;base64,";
	private static final String systemPath = "user.dir";
	
	public static String uploadProfilePicture(MultipartFile image, Long userId) throws IOException{
		
		File newImage = null;
		String newImageName = null;
		String realPath = "";
		GetPropertiesValues properties = null;
		String path = "";
		
		try{
			
			properties = new GetPropertiesValues(ConstantsProperties.APPLICATION_PROPERTIES);
			
			path = properties.getValue(ConstantsProperties.USER_PROFILE_PICTURE_PATH);
			
			realPath = System.getProperty(systemPath) + path;
			
			newImage = new File(realPath + userId + JPG);
			
			if(!newImage.exists()){
				newImage.createNewFile();
			}
			
			image.transferTo(newImage);
			
			newImageName = newImage.getName();
			
		}
		catch (IOException e){
			throw e;
		}
		
		return newImageName;
		
	}
	
	public static String getBase64FromProfilePictureName(String path, String profilePictureName) throws IOException{
		
		byte[] imageBytes = null;
		File fi = null;
		String realPath = null;
		String uriBase64 = null;
		
		try{
			
			imageBytes = getBytesFromImage(path, profilePictureName);
			
			uriBase64 = getBase64FromBytes(imageBytes);
			
		} catch(IOException e){
			throw e;
		}
		
		return uriBase64;
	}

	public static byte[] getBytesFromImage(String path, String profilePictureName) throws IOException {
		
		byte[] imageBytes;
		File fi;
		String realPath;
		
		realPath = System.getProperty(systemPath);
		fi = new File(realPath + path + profilePictureName);
		imageBytes = Files.readAllBytes(fi.toPath());
			
		return imageBytes;
	}
	
	public static String getBase64FromBytes(byte[] imageBytes){
		
		String uriBase64 = null;
		
		StringBuilder sb = new StringBuilder();
		sb.append(BASE_64);
		sb.append(StringUtils.newStringUtf8(Base64.encodeBase64(imageBytes, false)));
		uriBase64 = sb.toString();
		
		return uriBase64;
	}
	
	public static File getFileFromFileSystem(String path) throws FileNotFoundException{
		
		String realPath;
		File file = null;
		
		realPath = System.getProperty(systemPath);
		
		file = new File(realPath + path);
		
		if(!file.exists())
			throw new FileNotFoundException();
		
		return file;
		
	}

}
