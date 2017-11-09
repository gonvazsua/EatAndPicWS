package com.plateandpic.response;

/**
 * @author gonzalo
 * 
 * Class used for getting the additional user data
 *
 */
public class FollowersResponse {
	
	private Long userId;
	private Integer followersNumber;
	private Integer platePicturesNumber;
	private Boolean isFollowing;
	private Boolean isLoggedUser;
	
	/**
	 * Constructor
	 */
	public FollowersResponse(){
		
		this.userId = null;
		this.followersNumber = null;
		this.platePicturesNumber = null;
		this.isFollowing = null;
		this.isLoggedUser = null;
		
	}

	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * @return the followersNumber
	 */
	public Integer getFollowersNumber() {
		return followersNumber;
	}

	/**
	 * @param followersNumber the followersNumber to set
	 */
	public void setFollowersNumber(Integer followersNumber) {
		this.followersNumber = followersNumber;
	}

	/**
	 * @return the platePicturesNumber
	 */
	public Integer getPlatePicturesNumber() {
		return platePicturesNumber;
	}

	/**
	 * @param platePicturesNumber the platePicturesNumber to set
	 */
	public void setPlatePicturesNumber(Integer platePicturesNumber) {
		this.platePicturesNumber = platePicturesNumber;
	}

	/**
	 * @return the isFollowing
	 */
	public Boolean getIsFollowing() {
		return isFollowing;
	}

	/**
	 * @param isFollowing the isFollowing to set
	 */
	public void setIsFollowing(Boolean isFollowing) {
		this.isFollowing = isFollowing;
	}

	/**
	 * @return the isLoggedUser
	 */
	public Boolean getIsLoggedUser() {
		return isLoggedUser;
	}

	/**
	 * @param isLoggedUser the isLoggedUser to set
	 */
	public void setIsLoggedUser(Boolean isLoggedUser) {
		this.isLoggedUser = isLoggedUser;
	}

}
