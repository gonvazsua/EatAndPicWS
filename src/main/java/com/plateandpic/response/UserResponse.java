package com.plateandpic.response;

import com.plateandpic.models.User;

/**
 * @author gonzalo
 *
 */
/**
 * @author gonzalo
 *
 */
public class UserResponse {
	
	private Long userId;
	private String username;
	private String firstname;
	private String lastname;
	private String email;
	private String picture;
	private String target;
	
	/**
	 * @param user
	 */
	public UserResponse(User user){
		
		this.userId = user.getId();
		this.username = user.getUsername();
		this.firstname = user.getFirstname();
		this.lastname = user.getLastname();
		this.email = user.getEmail();
		this.picture = user.getPicture();
		this.target = user.getTarget();
		
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
	public String getFirstname() {
		return firstname;
	}

	/**
	 * @param firstname
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	/**
	 * @return
	 */
	public String getLastname() {
		return lastname;
	}

	/**
	 * @param lastname
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	/**
	 * @return
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
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
	public String getTarget() {
		return target;
	}

	/**
	 * @param target
	 */
	public void setTarget(String target) {
		this.target = target;
	}
	
	

}
