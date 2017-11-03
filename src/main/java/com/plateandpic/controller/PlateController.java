package com.plateandpic.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.plateandpic.constants.MessageConstants;
import com.plateandpic.dao.PlateDao;
import com.plateandpic.dao.RestaurantDao;
import com.plateandpic.exceptions.PlateAndPicException;
import com.plateandpic.exceptions.PlateException;
import com.plateandpic.exceptions.RestaurantException;
import com.plateandpic.factory.PlateFactory;
import com.plateandpic.models.Plate;
import com.plateandpic.models.Restaurant;
import com.plateandpic.response.PlateResponse;

/**
 * @author gonzalo
 *
 */
@RestController
@RequestMapping("/plate")
public class PlateController {
	
	private static final Logger log = LoggerFactory.getLogger(PlateController.class);
	
	@Autowired
	private PlateDao plateDao;
	
	@Autowired
	private RestaurantDao restaurantDao;
	
	@Autowired
	private PlateFactory plateFactory;
	
	/**
	 * @param request
	 * @param response
	 * @param plate
	 * @return
	 * @throws PlateAndPicException 
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public PlateResponse save(HttpServletRequest request, HttpServletResponse response,
			@RequestBody PlateResponse plate) throws PlateAndPicException{
		  
		if(plate == null){
			throw new PlateAndPicException(MessageConstants.GENERAL_ERROR);
		}
		
		plate = plateFactory.validateAndSave(plate);
		
		return plate;
		 
	}
	
	/**
	 * @param request
	 * @param response
	 * @param restaurantId
	 * @return
	 * @throws PlateException 
	 */
	@RequestMapping(value = "/getPlatesByRestaurant", method = RequestMethod.GET)
	@ResponseBody
	public List<PlateResponse> getPlatesByRestaurant(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Long restaurantId) throws PlateException{
		  
		List<PlateResponse> plates = null;
		
		if(restaurantId == null || restaurantId <= 0){
			throw new PlateException(MessageConstants.PLATES_NOT_LOADED);
		}
		
		plates = plateFactory.getPlateResponseByRestaurantId(restaurantId);
		
		response.setStatus(HttpServletResponse.SC_OK);
		  
		return plates;
		 
	}
	
	/**
	 * @param request
	 * @param response
	 * @param restaurantId
	 * @return
	 * @throws PlateException 
	 */
	@RequestMapping(value = "/findPlatesByName", method = RequestMethod.GET)
	@ResponseBody
	public List<PlateResponse> findPlatesByName(HttpServletRequest request, HttpServletResponse response,
			@RequestParam String name, @RequestParam Integer page) throws PlateException{
		  
		List<PlateResponse> plates = null;
		
		if(name == null || name.isEmpty()){
			throw new PlateException(MessageConstants.PLATES_NOT_LOADED);
		}
		
		plates = plateFactory.findPlatesByName(name, page);
		
		response.setStatus(HttpServletResponse.SC_OK);
		  
		return plates;
		 
	}
	
}
