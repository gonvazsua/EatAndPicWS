package com.plateandpic.factory;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.plateandpic.constants.ConstantsProperties;
import com.plateandpic.dao.PlatePictureDao;
import com.plateandpic.dao.UserDao;
import com.plateandpic.exceptions.PlatePictureException;
import com.plateandpic.models.PlatePicture;
import com.plateandpic.models.User;
import com.plateandpic.security.JwtTokenUtil;

@Service
public class PlatePictureFactory {
	
	@Autowired
	private PlatePictureDao platePictureDao;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private UserDao userDao;
	
	
	public PlatePicture save(MultipartFile picture, PlatePicture platePicture, String token) throws PlatePictureException, IOException{
		
		PlatePicture savedPlatePicture;
		String newPictureName = "";
		
		try{
			
			platePicture.setUser(getUserFromToken(token));
			
			newPictureName = getNewPlatePictureFileName();
			
			platePicture.setPicture(newPictureName);
			
			savedPlatePicture = savePlatePicture(platePicture);
			
			FileFactory.uploadFile(getProfilePicturesPath(), newPictureName, picture);
			
		} catch(PlatePictureException e){
			throw e;
		} catch (IOException e) {
			throw e;
		}
		
		return savedPlatePicture;
	}
	
	public PlatePicture savePlatePicture(PlatePicture platePicture) throws PlatePictureException{
		
		platePicture.setRegisteredOn(new Date());
		
		PlatePicture savedPlatePicture = platePictureDao.save(platePicture);
		
		if(savedPlatePicture == null){
			throw new PlatePictureException("PlatePicture not saved!!");
		}
		
		return savedPlatePicture;
		
	}
	
	private String getProfilePicturesPath(){
		
		return env.getProperty(ConstantsProperties.PLATE_PICTURES_PATH);
		
	}
	
	private String getNewPlatePictureFileName(){
		
		Integer numFiles = FileFactory.getFileCount(getProfilePicturesPath());
		
		numFiles = numFiles + 1;
		
		return numFiles.toString() + FileFactory.JPG;
		
	}
	
	private User getUserFromToken(String token){
		
		Long userId = jwtTokenUtil.getUserIdFromToken(token);
		
		User user = userDao.findOne(userId);
		
		return user;
		
	}

}
