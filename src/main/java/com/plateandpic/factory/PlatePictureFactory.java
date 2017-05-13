package com.plateandpic.factory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.plateandpic.constants.ConstantsProperties;
import com.plateandpic.dao.PlatePictureDao;
import com.plateandpic.dao.UserDao;
import com.plateandpic.exceptions.PlatePictureException;
import com.plateandpic.models.PlatePicture;
import com.plateandpic.models.User;
import com.plateandpic.response.PlatePictureResponse;
import com.plateandpic.security.JwtTokenUtil;

@Service
public class PlatePictureFactory {
	
	private static final Integer ROW_LIMIT = 20;
	private static final String QUERY_SORT = "registeredOn";
	
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
			
			FileFactory.uploadFile(getPlatePicturesPath(), newPictureName, picture);
			
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
	
	private String getPlatePicturesPath(){
		
		return env.getProperty(ConstantsProperties.PLATE_PICTURES_PATH);
		
	}
	
	private String getProfilePicturePath(){
		
		return env.getProperty(ConstantsProperties.USER_PROFILE_PICTURE_PATH);
		
	}
	
	private String getNewPlatePictureFileName(){
		
		Integer numFiles = FileFactory.getFileCount(getPlatePicturesPath());
		
		numFiles = numFiles + 1;
		
		return numFiles.toString() + FileFactory.JPG;
		
	}
	
	private User getUserFromToken(String token){
		
		Long userId = jwtTokenUtil.getUserIdFromToken(token);
		
		User user = userDao.findOne(userId);
		
		return user;
		
	}
	
	public List<PlatePictureResponse> getLastPlatePictures(String token, Integer page) throws PlatePictureException, IOException{
		
		User user = null;
		List<PlatePicture> platePictures = null;
		List<PlatePictureResponse> platePicturesResponse;
		Pageable pageable = null;
		
		user = getUserFromToken(token);
		
		if(user == null){
			throw new PlatePictureException("User not found under token: " + token);
		}
		
		pageable = new PageRequest(page, ROW_LIMIT, Sort.Direction.DESC, QUERY_SORT);
		
		platePictures = platePictureDao.findByUserIn(user.getFollowers(), pageable);
		
		platePicturesResponse = buildPlatePictureResponseFromPlatePictureList(platePictures, user);
		
		return platePicturesResponse;
		
	}
	
	private List<PlatePictureResponse> buildPlatePictureResponseFromPlatePictureList(List<PlatePicture> platePictures, User user) throws IOException{
		
		List<PlatePictureResponse> response = new ArrayList<PlatePictureResponse>();
		PlatePictureResponse ppr = null;
		String base64ImgPlatePicture = "";
		String base64UserPicture = "";
		
		for(PlatePicture platePicture : platePictures){
			
			ppr = new PlatePictureResponse(platePicture);
			ppr.checkLikeToUser(platePicture, user);
			base64ImgPlatePicture = FileFactory.getBase64FromProfilePictureName(getPlatePicturesPath(), platePicture.getPicture());
			ppr.setPicture(base64ImgPlatePicture);
			
			if(user.getPicture() != null && !"".equals(user.getPicture())){
				base64UserPicture = FileFactory.getBase64FromProfilePictureName(getProfilePicturePath(), ppr.getUserImage());
				ppr.setUserImage(base64UserPicture);
			}
			
			response.add(ppr);
			
		}
		
		return response;
	}

}
