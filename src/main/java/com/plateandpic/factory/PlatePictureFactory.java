package com.plateandpic.factory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.plateandpic.constants.ConstantsProperties;
import com.plateandpic.constants.MessageConstants;
import com.plateandpic.dao.PlatePictureDao;
import com.plateandpic.exceptions.PlateAndPicException;
import com.plateandpic.exceptions.PlateException;
import com.plateandpic.exceptions.PlatePictureException;
import com.plateandpic.exceptions.UserException;
import com.plateandpic.models.PlatePicture;
import com.plateandpic.models.User;
import com.plateandpic.response.PlatePictureResponse;
import com.plateandpic.validator.PlatePictureValidator;

/**
 * @author gonzalo
 *
 */
@Service
public class PlatePictureFactory {
	
	private static final Logger log = LoggerFactory.getLogger(PlatePictureFactory.class);
	
	private static final Integer ROW_LIMIT = 20;
	private static final String QUERY_SORT = "registeredOn";
	private static final String SEPARATOR = "_";
	
	@Autowired
	private PlatePictureDao platePictureDao;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private UserFactory userFactory;
	
	@Autowired
	private PlateFactory plateFactory;
	
	
	/**
	 * @param picture
	 * @param platePicture
	 * @param token
	 * @return
	 * @throws IOException
	 * @throws PlateAndPicException 
	 */
	public void save(MultipartFile picture, PlatePictureResponse platePicture, String token) throws IOException, PlateAndPicException{
		
		PlatePictureResponse savedPlatePicture;
		String newPictureName = "";
		PlatePicture toSave = null;
		
		try{
			
			toSave = buildPlatePictureForSaving(token, platePicture.getPlateId(), platePicture.getTitle(), true);

			validatePlatePicture(toSave);
			
			FileFactory.uploadFile(getPlatePicturesPath(), toSave.getPicture(), picture);
			
			toSave = savePlatePicture(toSave);
			
		} catch(PlatePictureException e){
			throw e;
		} catch (IOException e) {
			throw e;
		}
	
	}
	
	/**
	 * @param platePicture
	 * @throws PlateAndPicException
	 * 
	 * Validate the PlatePiture object using the PlatePictureValidator class
	 */
	private void validatePlatePicture(PlatePicture platePicture) throws PlateAndPicException{
		
		PlatePictureValidator validator = new PlatePictureValidator(platePicture);
		
		validator.validate();
		
	}
	
	/**
	 * @param token
	 * @param plateId
	 * @param title
	 * @param isNew
	 * @return
	 * @throws UserException
	 * @throws PlateException
	 * 
	 * Build a new object of PlatePicture. If the parameter isNew is true, set the registeredOn to the current date
	 */
	private PlatePicture buildPlatePictureForSaving(String token, Long plateId, String title, Boolean isNew) throws UserException, PlateException{
		
		PlatePicture platePicture = new PlatePicture();
		User user = userFactory.getUserFromToken(token);
		
		platePicture.setUser(user);
		platePicture.setPlate(plateFactory.findById(plateId));
		platePicture.setTitle(title);
		platePicture.setPicture(getNewPlatePictureFileName(user.getId()));
		
		if(isNew){
			platePicture.setRegisteredOn(new Date());
		}
		
		return platePicture;
		
	}
	
	/**
	 * @param platePicture
	 * @return
	 * @throws PlatePictureException
	 */
	public PlatePicture savePlatePicture(PlatePicture platePicture) throws PlatePictureException {
		
		PlatePicture savedPlatePicture = platePictureDao.save(platePicture);
		
		if(savedPlatePicture == null){
			log.error("PlatePicture not saved");
			throw new PlatePictureException(MessageConstants.PLATEPICTURE_NOT_SAVED);
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
	 * 
	 * Example: 2017-03-11_18-51-33_1
	 */
	private String getNewPlatePictureFileName(Long userId){
		
		Date today = new Date();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		
		String fileName = dateFormat.format(today) + SEPARATOR + userId;
		
		return fileName;
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
	
	/**
	 * @param platePictureResponse
	 * @throws PlateAndPicException
	 * 
	 * Convert the images of a list of Plate pictures to base64 format
	 */
	private void convertImagesToBase64(List<PlatePictureResponse> platePictureResponse) throws PlateAndPicException{
		
		String base64ImgPlatePicture = "";
		String base64UserImage = "";
		
		for(PlatePictureResponse ppr : platePictureResponse){
			
			convertImageToBase64(ppr);
			
		}
		
	}
	
	/**
	 * @param ppr
	 * @throws PlateAndPicException
	 * 
	 * Convert image and the user picture to a bas64 format
	 */
	private void convertImageToBase64(PlatePictureResponse ppr) throws PlateAndPicException{
		
		String base64ImgPlatePicture = "";
		String base64UserImage = "";
		
		base64ImgPlatePicture = FileFactory.getBase64FromProfilePictureName(getPlatePicturesPath(), ppr.getPicture());
		ppr.setPicture(base64ImgPlatePicture);
		
		if(ppr.getUserImage() != null){
			base64UserImage = FileFactory.getBase64FromProfilePictureName(getProfilePicturePath(), ppr.getUserImage());
			ppr.setUserImage(base64UserImage);
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
	
	public PlatePictureResponse getPlatePictureResponseById(Long platePictureId) throws PlateAndPicException{
		
		PlatePictureResponse platePicture = null;
		
		platePicture = platePictureDao.getPlatePictureByPlatePictureId(platePictureId);
		
		convertImageToBase64(platePicture);
		
		return platePicture;
		
	}

}
