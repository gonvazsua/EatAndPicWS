package com.plateandpic.factory;

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
import com.plateandpic.exceptions.PlateAndPicException;
import com.plateandpic.exceptions.PlatePictureException;
import com.plateandpic.exceptions.UserException;
import com.plateandpic.models.PlatePicture;
import com.plateandpic.models.User;
import com.plateandpic.response.PlatePictureResponse;

/**
 * @author gonzalo
 *
 */
@Service
public class PlatePictureFactory {
	
	private static final Integer ROW_LIMIT = 20;
	private static final String QUERY_SORT = "registeredOn";
	
	@Autowired
	private PlatePictureDao platePictureDao;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private UserFactory userFactory;
	
	
	/**
	 * @param picture
	 * @param platePicture
	 * @param token
	 * @return
	 * @throws PlatePictureException
	 * @throws IOException
	 * @throws UserException
	 */
	public PlatePicture save(MultipartFile picture, PlatePicture platePicture, String token) throws PlatePictureException, IOException, UserException{
		
		PlatePicture savedPlatePicture;
		String newPictureName = "";
		
		try{
			
			platePicture.setUser(userFactory.getUserFromToken(token));
			
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
	
	/**
	 * @param platePicture
	 * @return
	 * @throws PlatePictureException
	 */
	public PlatePicture savePlatePicture(PlatePicture platePicture) throws PlatePictureException{
		
		platePicture.setRegisteredOn(new Date());
		
		PlatePicture savedPlatePicture = platePictureDao.save(platePicture);
		
		if(savedPlatePicture == null){
			throw new PlatePictureException("PlatePicture not saved!!");
		}
		
		return savedPlatePicture;
		
	}
	
	/**
	 * @return
	 */
	private String getPlatePicturesPath(){
		
		return env.getProperty(ConstantsProperties.PLATE_PICTURES_PATH);
		
	}
	
	/**
	 * @return
	 */
	private String getProfilePicturePath(){
		
		return env.getProperty(ConstantsProperties.USER_PROFILE_PICTURE_PATH);
		
	}
	
	/**
	 * @return
	 */
	private String getNewPlatePictureFileName(){
		
		Integer numFiles = FileFactory.getFileCount(getPlatePicturesPath());
		
		numFiles = numFiles + 1;
		
		return numFiles.toString() + FileFactory.JPG;
		
	}
	
	/**
	 * @param token
	 * @param page
	 * @return
	 * @throws IOException
	 * @throws PlateAndPicException 
	 */
	public List<PlatePictureResponse> getLastPlatePictures(String token, Integer page) throws PlateAndPicException, IOException{
		
		List<PlatePicture> platePictures = null;
		List<PlatePictureResponse> platePicturesResponse;
		Pageable pageable = null;
		
		Long userId = userFactory.getUserIdFromToken(token);
		
		Integer fromLimit = calculateFromLimitPagination(page);
		Integer toLimit = calculateToLimitPagination(fromLimit);
		
		platePicturesResponse = platePictureDao.getLastFollowersPlatePicturesByUserId(userId, fromLimit, toLimit);
		
		convertImagesToBase64(platePicturesResponse);
		
		return platePicturesResponse;
		
	}
	
	private void convertImagesToBase64(List<PlatePictureResponse> platePictureResponse) throws PlateAndPicException{
		
		String base64ImgPlatePicture = "";
		
		for(PlatePictureResponse ppr : platePictureResponse){
			
			base64ImgPlatePicture = FileFactory.getBase64FromProfilePictureName(getPlatePicturesPath(), ppr.getPicture());
			ppr.setPicture(base64ImgPlatePicture);
			
		}
		
	}
	
	/**
	 * @param platePictures
	 * @param user
	 * @return
	 * @throws IOException
	 * @throws PlateAndPicException 
	 */
	private List<PlatePictureResponse> buildPlatePictureResponseFromPlatePictureList(List<PlatePicture> platePictures, User user) throws IOException, PlateAndPicException{
		
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
	
	/**
	 * @param token
	 * @param platePictureId
	 * @throws UserException
	 * @throws PlatePictureException
	 */
	public void likePlatePicture(String token, Long platePictureId) throws UserException, PlatePictureException{
		
		User user = null;
		PlatePicture platePicture = null;
		
		user = userFactory.getUserFromToken(token);
		
		platePicture = getPlatePictureById(platePictureId);
		
		platePicture.getLikes().add(user);
		
		platePictureDao.save(platePicture);
		
	}
	
	/**
	 * @param token
	 * @param platePictureId
	 * @throws UserException
	 * @throws PlatePictureException
	 */
	public void unlikePlatePicture(String token, Long platePictureId) throws UserException, PlatePictureException{
		
		User user = null;
		PlatePicture platePicture = null;
		
		user = userFactory.getUserFromToken(token);
		
		platePicture = getPlatePictureById(platePictureId);
		
		platePicture.getLikes().remove(user);
		
		platePictureDao.save(platePicture);
		
	}
	
	
	/**
	 * @param platePictureId
	 * @return
	 * @throws PlatePictureException
	 */
	public PlatePicture getPlatePictureById(Long platePictureId) throws PlatePictureException{
		
		PlatePicture platePicture = platePictureDao.findOne(platePictureId);
		
		if(platePicture == null){
			throw new PlatePictureException("PlatePicture not found with ID: " + platePictureId);
		}
		
		return platePicture;
		
	}
	
	/**
	 * @param username
	 * @param page
	 * @return
	 * @throws IOException
	 * @throws PlateAndPicException 
	 */
	public List<PlatePictureResponse> getPlatePictureByUsername(String username, Integer page) throws IOException, PlateAndPicException{
		
		List<PlatePictureResponse> platePicturesResponse;
		
		Integer fromLimit = calculateFromLimitPagination(page);
		Integer toLimit = calculateToLimitPagination(fromLimit);
		
		platePicturesResponse = platePictureDao.getLastPlatePicturesByUsername(username, fromLimit, toLimit);
		
		convertImagesToBase64(platePicturesResponse);
		
		return platePicturesResponse;
		
	}
	
	/**
	 * @param restaurantId
	 * @param page
	 * @return
	 * @throws IOException
	 * @throws PlateAndPicException 
	 */
	public List<PlatePictureResponse> getPlatePictureByRestaurantId(Long restaurantId, Integer page) throws IOException, PlateAndPicException{
		
		List<PlatePictureResponse> platePicturesResponse;
		
		Integer fromLimit = calculateFromLimitPagination(page);
		Integer toLimit = calculateToLimitPagination(fromLimit);
		
		platePicturesResponse = platePictureDao.getLastPlatePicturesByRestaurantId(restaurantId, fromLimit, toLimit);
		
		convertImagesToBase64(platePicturesResponse);
		
		return platePicturesResponse;
		
	}
	
	/**
	 * @param plateId
	 * @param page
	 * @return
	 * @throws IOException
	 * @throws PlateAndPicException 
	 */
	public List<PlatePictureResponse> getPlatePictureByPlateId(Long plateId, Integer page) throws IOException, PlateAndPicException{
		
		List<PlatePictureResponse> platePicturesResponse;
		
		Integer fromLimit = calculateFromLimitPagination(page);
		Integer toLimit = calculateToLimitPagination(fromLimit);
		
		platePicturesResponse = platePictureDao.getLastPlatePicturesByPlateId(plateId, fromLimit, toLimit);
		
		convertImagesToBase64(platePicturesResponse);
		
		return platePicturesResponse;
		
	}
	
	/**
	 * @param page
	 * 
	 * Calculate the lower limit of the query: 
	 */
	private Integer calculateFromLimitPagination(Integer page){
		
		Integer fromLimit = page * ROW_LIMIT;
		
		return fromLimit;
		
	}
	
	/**
	 * @param fromLimit
	 * 
	 * Calculate the upper limit of the query: 
	 */
	private Integer calculateToLimitPagination(Integer fromLimit){
		
		Integer toLimit = fromLimit + (ROW_LIMIT - 1);
		
		return toLimit;
		
	}

}
