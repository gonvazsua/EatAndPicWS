package com.plateandpic.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.plateandpic.dao.CityDao;
import com.plateandpic.dao.RestaurantDao;
import com.plateandpic.exceptions.IPNotFoundException;
import com.plateandpic.exceptions.RestaurantException;
import com.plateandpic.factory.LocationFactory;
import com.plateandpic.factory.RestaurantFactory;
import com.plateandpic.models.City;
import com.plateandpic.models.IpLocation;
import com.plateandpic.models.Restaurant;
import com.plateandpic.security.JwtTokenUtil;

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
	 * @return
	 */
	@RequestMapping(value = "/getRestaurantsByName", method = RequestMethod.GET)
	@ResponseBody
	public List<Restaurant> getRestaurantsByName(HttpServletRequest request, HttpServletResponse response,
			@RequestParam String name){
		
		IpLocation ipLocation = null;
		List<Restaurant> restaurants = null;
		City city = null;
		  
		try{
			
			if(name == null || "".equals(name)){
				throw new RestaurantException("Restaurant not found: Empty name!");
			}
			
			ipLocation = LocationFactory.getLocationFromHost(request.getRemoteHost());
			
			city = cityDao.findByName(ipLocation.getCityName());
			
			restaurants = restaurantDao.findByNameAndCityId(name, city.getCityId());
			
			if(restaurants == null || restaurants.isEmpty()){
				throw new RestaurantException("Restaurant not found by name: " + name);
			}
			
			response.setStatus(HttpServletResponse.SC_OK);
			  
		} catch(RestaurantException ex){
			restaurants = null;
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		
		} catch(IOException ex){
			restaurants = null;
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		
		} catch(IPNotFoundException ex){
			restaurants = null;
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		
		  
		return restaurants;
		 
	}
	

	/**
	 * @param request
	 * @param response
	 * @param restaurant
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public Restaurant save(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Restaurant restaurant){
		
		Restaurant savedRestaurant = null;
		
		try{
			
			savedRestaurant = restaurantFactory.findByApiPlaceId(restaurant);
			
			if(savedRestaurant == null)
				savedRestaurant = restaurantFactory.buildAndSave(restaurant);
			
			response.setStatus(HttpServletResponse.SC_OK);
			  
		} catch(Exception ex){
			
			log.error("Error al guardar restaurante: " + ex.getMessage());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		
		} 
		
		return savedRestaurant;
		 
	}
}
