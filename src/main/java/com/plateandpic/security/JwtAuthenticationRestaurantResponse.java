package com.plateandpic.security;

/**
 * @author gonzalo
 *
 * Extend to JwtAuthenticationResponse for managing the Users who have a restaurant assigned
 *
 */
public class JwtAuthenticationRestaurantResponse extends JwtAuthenticationResponse {
	
	private Long restaurantId;
	private Integer verificationStatus;
	
	/**
	 * @param token
	 * @param restaurantId
	 * @param verificationStatus
	 */
	public JwtAuthenticationRestaurantResponse(String token, Long restaurantId, Integer verificationStatus) {
		
		super(token);
		this.restaurantId = restaurantId;
		this.verificationStatus = verificationStatus;
		
	}
	
	/**
	 * @return
	 */
	public Integer getVerificationStatus(){
		return this.verificationStatus;
	}
	
	/**
	 * @return
	 */
	public Long getRestaurantId(){
		return this.restaurantId;
	}

}
