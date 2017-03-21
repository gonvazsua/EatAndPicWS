package com.eatandpic.manager;

import java.io.File;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public class FileManager {
	
	private static final String JPG = ".jpg";
	
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
			
			newImage.createNewFile();
			
			newImageName = newImage.getName();
			
		}
		catch (IOException e){
			throw e;
		}
		
		return newImageName;
		
	}

}
