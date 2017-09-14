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

import com.plateandpic.dao.PlateDao;
import com.plateandpic.dao.RestaurantDao;
import com.plateandpic.exceptions.PlateException;
import com.plateandpic.exceptions.RestaurantException;
import com.plateandpic.factory.PlateFactory;
import com.plateandpic.models.Plate;
import com.plateandpic.models.Restaurant;

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
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public Plate save(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Plate plate){
		  
		Restaurant restaurant = null;
		Plate savedPlate = null;
		
		try{
			
			savedPlate = plateFactory.savePlateIfNotExists(plate);
			
			response.setStatus(HttpServletResponse.SC_OK);
			  
		} catch(PlateException e){
			log.error(e.getMessage());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			plate = null;
		}
		  
		return savedPlate;
		 
	}
	
	/**
	 * @param request
	 * @param response
	 * @param restaurantId
	 * @return
	 */
	@RequestMapping(value = "/getPlatesByRestaurant", method = RequestMethod.GET)
	@ResponseBody
	public List<Plate> getPlatesByRestaurant(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Long restaurantId){
		  
		Restaurant restaurant = null;
		List<Plate> plates = null;
		
		try{
			
			restaurant = restaurantDao.findOne(restaurantId);
			
			if(restaurant == null){
				throw new RestaurantException("Restaurant not found with ID: " + restaurantId);
			}
			
			plates = plateDao.findByRestaurant(restaurant);
			
			if(plates == null){
				throw new PlateException("Plates not found for restaurantId: " + restaurantId);
			}
			
			response.setStatus(HttpServletResponse.SC_OK);
			  
		} catch(RestaurantException e){
			log.error(e.getMessage());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			plates = null;
		} catch(PlateException e){
			log.error(e.getMessage());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			plates = null;
		}
		  
		return plates;
		 
	}
	
}
