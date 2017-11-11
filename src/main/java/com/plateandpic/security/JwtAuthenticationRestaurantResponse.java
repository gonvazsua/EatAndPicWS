package com.plateandpic.security;

/**
 * @author gonzalo
 *
 * Extend to JwtAuthenticationResponse for managing the Users who have a restaurant assigned
 *
 */
public class JwtAuthenticationRestaurantResponse extends JwtAuthenticationResponse {
	
	private Long restaurantId;
	
	/**
	 * @param token
	 * @param restaurantId
	 */
	public JwtAuthenticationRestaurantResponse(String token, Long restaurantId) {
		
		super(token);
		this.restaurantId = restaurantId;
		
	}
	
	/**
	 * @return
	 */
	public Long getRestaurantId(){
		return this.restaurantId;
	}

}
