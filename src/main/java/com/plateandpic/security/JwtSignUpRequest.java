package com.plateandpic.security;

/**
 * @author gonzalo
 *
 */
public class JwtSignUpRequest {
	
	private String name;
	private String username;
	private String email;
	private String password;
	private String repeatPassword;
	private Boolean isUserRestaurant;
	
	/**
	 * 
	 */
	public JwtSignUpRequest(){
		
		this.name = null;
		this.username = null;
		this.email = null;
		this.password = null;
		this.repeatPassword = null;
		this.isUserRestaurant = null;
		
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
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
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return
	 */
	public String getRepeatPassword() {
		return repeatPassword;
	}

	/**
	 * @param repeatPassword
	 */
	public void setRepeatPassword(String repeatPassword) {
		this.repeatPassword = repeatPassword;
	}

	/**
	 * @return
	 */
	public Boolean getIsUserRestaurant() {
		return isUserRestaurant;
	}

	/**
	 * @param isUserRestaurant
	 */
	public void setIsUserRestaurant(Boolean isUserRestaurant) {
		this.isUserRestaurant = isUserRestaurant;
	}

}
