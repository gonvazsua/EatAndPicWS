package com.plateandpic.response;

import java.text.SimpleDateFormat;
import java.util.Iterator;

import com.plateandpic.models.PlatePicture;
import com.plateandpic.models.User;

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
	private Boolean likeToUser;
	private String registeredOn;
	
	
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
		this.likeToUser = false;
		this.registeredOn = new SimpleDateFormat("dd/MM/yyyy").format(platePicture.getRegisteredOn());
		
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public Long getPlatePictureId() {
		return platePictureId;
	}
	
	public void setPlatePictureId(Long platePictureId) {
		this.platePictureId = platePictureId;
	}
	
	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public Long getRestaurantId() {
		return restaurantId;
	}
	
	public void setRestaurantId(Long restaurantId) {
		this.restaurantId = restaurantId;
	}
	
	public String getRestaurantName() {
		return restaurantName;
	}
	
	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}
	
	public Long getCityId() {
		return cityId;
	}
	
	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}
	
	public String getCityName() {
		return cityName;
	}
	
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	public String getPicture() {
		return picture;
	}
	
	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	public Integer getLikesNumber() {
		return likesNumber;
	}
	
	public void setLikesNumber(Integer likesNumber) {
		this.likesNumber = likesNumber;
	}
	
	public Boolean getLikeToUser() {
		return likeToUser;
	}
	
	public void setLikeToUser(Boolean likeToUser) {
		this.likeToUser = likeToUser;
	}
	
	public Long getPlateId() {
		return plateId;
	}

	public void setPlateId(Long plateId) {
		this.plateId = plateId;
	}

	public String getPlateName() {
		return plateName;
	}

	public void setPlateName(String plateName) {
		this.plateName = plateName;
	}

	public String getRegisteredOn() {
		return registeredOn;
	}

	public void setRegisteredOn(String registeredOn) {
		this.registeredOn = registeredOn;
	}
	
	public String getUserImage() {
		return userImage;
	}

	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}

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
		
		this.setLikeToUser(likeToUser);
		
	}

}
