package com.plateandpic.dao;

import com.plateandpic.exceptions.PlateAndPicException;
import com.plateandpic.response.FollowersResponse;

/**
 * @author gonzalo
 *
 */
public interface UserDaoCustom {
	
	/**
	 * @param loggedUserId
	 * @param userId
	 * @return
	 */
	public FollowersResponse getFollowersData(Long loggedUserId, Long userId) throws PlateAndPicException;

}
