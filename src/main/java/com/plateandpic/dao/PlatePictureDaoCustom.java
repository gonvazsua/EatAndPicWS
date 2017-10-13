package com.plateandpic.dao;

import java.util.List;

import com.plateandpic.response.PlatePictureResponse;

/**
 * @author gonzalo
 *
 */
public interface PlatePictureDaoCustom {
	
	/**
	 * @return
	 */
	public List<PlatePictureResponse> getLastPlatePicturesByUsername(String username);
	
	/**
	 * @return
	 */
	public List<PlatePictureResponse> getLastFollowersPlatePicturesByUserId(Long userId);
	
}
