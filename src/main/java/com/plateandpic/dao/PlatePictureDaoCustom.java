package com.plateandpic.dao;

import java.util.List;

import com.plateandpic.exceptions.PlatePictureException;
import com.plateandpic.response.PlatePictureResponse;

/**
 * @author gonzalo
 *
 */
public interface PlatePictureDaoCustom {
	
	/**
	 * @return
	 */
	public List<PlatePictureResponse> getLastPlatePicturesByUserId(String userId, Integer from, Integer to) throws PlatePictureException;
	
	/**
	 * @return
	 */
	public List<PlatePictureResponse> getLastFollowersPlatePicturesByUserId(Long userId, Integer from, Integer to) throws PlatePictureException;
	
	/**
	 * @return
	 */
	public List<PlatePictureResponse> getLastPlatePicturesByRestaurantId(Long restaurantId, Integer from, Integer to) throws PlatePictureException;
	
	/**
	 * @return
	 */
	public List<PlatePictureResponse> getLastPlatePicturesByPlateId(Long plateId, Integer from, Integer to) throws PlatePictureException;
	
	/**
	 * @return
	 */
	public PlatePictureResponse getPlatePictureByPlatePictureId(Long platePictureId) throws PlatePictureException;
}
