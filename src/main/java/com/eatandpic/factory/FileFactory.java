package com.eatandpic.factory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileFactory {
	
	private static final String JPG = ".jpg";
	private static final String BASE_64 = "data:image/png;base64,";
	
	public static String uploadProfilePicture(String path, MultipartFile image, Long userId) throws IOException{
		
		File newImage = null;
		String newImageName = null;
		String realPath = "";
		
		try{
			
			realPath = System.getProperty("user.dir") + path;
			
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
		
		realPath = System.getProperty("user.dir");
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

}
