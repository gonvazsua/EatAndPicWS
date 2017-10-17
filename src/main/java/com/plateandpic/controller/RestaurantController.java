package com.plateandpic.controller;

import java.io.IOException;
import java.util.ArrayList;
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
import com.plateandpic.dao.CityDao;
import com.plateandpic.dao.RestaurantDao;
import com.plateandpic.exceptions.IPNotFoundException;
import com.plateandpic.exceptions.PlateAndPicException;
import com.plateandpic.exceptions.RestaurantException;
import com.plateandpic.factory.LocationFactory;
import com.plateandpic.factory.RestaurantFactory;
import com.plateandpic.models.City;
import com.plateandpic.models.IpLocation;
import com.plateandpic.models.Restaurant;
import com.plateandpic.response.RestaurantRequestResponse;

/**
 * @author gonzalo
 *
 */
@RestController
@RequestMapping("/restaurant")
public class RestaurantController {
	
	private static final Logger log = LoggerFactory.getLogger(RestaurantController.class);
	
	@Autowired
	private CityDao cityDao;
	
	@Autowired
	private RestaurantDao restaurantDao;
	
	@Autowired
	private RestaurantFactory restaurantFactory;
	

	/**
	 * @param chars
	 * @return
	 */
	@RequestMapping(value = "/getRestaurants", method = RequestMethod.GET)
	@ResponseBody
	public List<Restaurant> getRestaurants(String chars){
		  
		List<Restaurant> restaurants = null;
		
		try{
			
			
			  
		} catch(Exception e){
			restaurants = null;
		}
		  
		return restaurants;
		 
	}
	
	

	/**
	 * @param request
	 * @param response
	 * @param name
	 * @param latitude
	 * @param longitude
	 * @return
	 * @throws PlateAndPicException 
	 * @throws RestaurantException 
	 */
	@RequestMapping(value = "/getRestaurantsByName", method = RequestMethod.GET)
	@ResponseBody
	public List<RestaurantRequestResponse> getRestaurantsByName(HttpServletRequest request, HttpServletResponse response,
			@RequestParam String name, @RequestParam Double latitude, @RequestParam Double longitude) throws PlateAndPicException {
		
		List<RestaurantRequestResponse> restaurants = null;
		  
		if(name == null || "".equals(name)){
			
			restaurants = new ArrayList<RestaurantRequestResponse>();
			
		} else {
			
			restaurants = restaurantFactory.searchRestaurantsByName(name, latitude, longitude);
			
		}
		
		response.setStatus(HttpServletResponse.SC_OK);
		  
		return restaurants;
		 
	}
	

	/**
	 * @param request
	 * @param response
	 * @param restaurant
	 * @return
	 * @throws RestaurantException 
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public RestaurantRequestResponse save(HttpServletRequest request, HttpServletResponse response,
			@RequestBody RestaurantRequestResponse restaurantRequest) throws RestaurantException{
		
		RestaurantRequestResponse savedRestaurant = null;
		
		savedRestaurant = restaurantFactory.saveIfNotExists(restaurantRequest);
			
		response.setStatus(HttpServletResponse.SC_OK);
		
		return savedRestaurant;
		 
	}
	
	/**
	 * @param request
	 * @param response
	 * @param restaurant
	 * @return
	 * @throws RestaurantException 
	 */
	@RequestMapping(value = "/getById", method = RequestMethod.GET)
	@ResponseBody
	public RestaurantRequestResponse getById(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Long restaurantId) throws RestaurantException{
		
		if(restaurantId == null || restaurantId <= 0){
			throw new RestaurantException(MessageConstants.RESTAURANT_NOT_FOUND);
		}
		
		RestaurantRequestResponse restaurant = restaurantFactory.getById(restaurantId);
		
		return restaurant;
		 
	}
	
}
