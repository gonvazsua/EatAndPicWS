package com.plateandpic.response;

import java.text.SimpleDateFormat;
import java.util.Iterator;

import com.plateandpic.models.PlatePicture;
import com.plateandpic.models.User;
import com.plateandpic.utils.DateUtils;

/**
 * @author gonzalo
 *
 */
public class PlatePictureResponse {
	
	private Long platePictureId;
	private String title;
	private Long userId;
	private String username;
	private String userImage;
	private Long restaurantId;
	private String restaurantName;
	private Long cityId;
	private String cityName;
	private Long plateId;
	private String plateName;
	private String picture;
	private Integer likesNumber;
	private Integer commentsNumber;
	private Boolean likeToUser;
	private String registeredOn;
	
	
	/**
	 * @param platePicture
	 */
	public PlatePictureResponse(PlatePicture platePicture){
		
		this.platePictureId = platePicture.getPictureId();
		this.title = platePicture.getTitle();
		this.userId = platePicture.getUser().getId();
		this.username = platePicture.getUser().getUsername();
		this.userImage = platePicture.getUser().getPicture();
		this.restaurantId = platePicture.getPlate().getRestaurant().getRestaurantId();
		this.restaurantName = platePicture.getPlate().getRestaurant().getName();
		this.cityId = platePicture.getPlate().getRestaurant().getCity().getCityId();
		this.cityName = platePicture.getPlate().getRestaurant().getCity().getName();
		this.picture = platePicture.getPicture();
		this.plateId = platePicture.getPlate().getPlateId();
		this.plateName = platePicture.getPlate().getName();
		this.likesNumber = platePicture.getLikes().size();
		this.commentsNumber = platePicture.getComments().size();
		this.likeToUser = false;
		this.registeredOn = DateUtils.getDateDDMMYYY(platePicture.getRegisteredOn());
		
	}
	
	/**
	 * @return
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * @return
	 */
	public Long getPlatePictureId() {
		return platePictureId;
	}
	
	/**
	 * @param platePictureId
	 */
	public void setPlatePictureId(Long platePictureId) {
		this.platePictureId = platePictureId;
	}
	
	/**
	 * @return
	 */
	public Long getUserId() {
		return userId;
	}
	
	/**
	 * @param userId
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	/**
	 * @return
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * @return
	 */
	public Long getRestaurantId() {
		return restaurantId;
	}
	
	/**
	 * @param restaurantId
	 */
	public void setRestaurantId(Long restaurantId) {
		this.restaurantId = restaurantId;
	}
	
	/**
	 * @return
	 */
	public String getRestaurantName() {
		return restaurantName;
	}
	
	/**
	 * @param restaurantName
	 */
	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}
	
	/**
	 * @return
	 */
	public Long getCityId() {
		return cityId;
	}
	
	/**
	 * @param cityId
	 */
	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}
	
	/**
	 * @return
	 */
	public String getCityName() {
		return cityName;
	}
	
	/**
	 * @param cityName
	 */
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	/**
	 * @return
	 */
	public String getPicture() {
		return picture;
	}
	
	/**
	 * @param picture
	 */
	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	/**
	 * @return
	 */
	public Integer getLikesNumber() {
		return likesNumber;
	}
	
	/**
	 * @param likesNumber
	 */
	public void setLikesNumber(Integer likesNumber) {
		this.likesNumber = likesNumber;
	}
	
	/**
	 * @return
	 */
	public Integer getCommentsNumber() {
		return commentsNumber;
	}
	
	/**
	 * @param commentsNumber
	 */
	public void setCommentsNumber(Integer commentsNumber) {
		this.commentsNumber = commentsNumber;
	}
	
	/**
	 * @return
	 */
	public Boolean getLikeToUser() {
		return likeToUser;
	}
	
	/**
	 * @param likeToUser
	 */
	public void setLikeToUser(Boolean likeToUser) {
		this.likeToUser = likeToUser;
	}
	
	/**
	 * @return
	 */
	public Long getPlateId() {
		return plateId;
	}

	/**
	 * @param plateId
	 */
	public void setPlateId(Long plateId) {
		this.plateId = plateId;
	}

	/**
	 * @return
	 */
	public String getPlateName() {
		return plateName;
	}

	/**
	 * @param plateName
	 */
	public void setPlateName(String plateName) {
		this.plateName = plateName;
	}

	/**
	 * @return
	 */
	public String getRegisteredOn() {
		return registeredOn;
	}

	/**
	 * @param registeredOn
	 */
	public void setRegisteredOn(String registeredOn) {
		this.registeredOn = registeredOn;
	}
	
	/**
	 * @return
	 */
	public String getUserImage() {
		return userImage;
	}

	/**
	 * @param userImage
	 */
	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}

	/**
	 * @param platePicture
	 * @param user
	 */
	public void checkLikeToUser(PlatePicture platePicture, User user){
		
		Boolean userLike = false;
		Iterator<User> likes = platePicture.getLikes().iterator();
		User userIt = null;		
		
		while(likes.hasNext()){
			
			userIt = (User) likes.next();
			
			if(userIt.getId().equals(user.getId())){
				
				userLike = true;
				break;
				
			}
			
		}
		
		this.setLikeToUser(userLike);
		
	}

}
