package com.eatandpic.controller;

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

import com.eatandpic.dao.CityDao;
import com.eatandpic.dao.RestaurantDao;
import com.eatandpic.exceptions.IPNotFoundException;
import com.eatandpic.exceptions.RestaurantNotFoundException;
import com.eatandpic.factory.LocationFactory;
import com.eatandpic.models.City;
import com.eatandpic.models.IpLocation;
import com.eatandpic.models.Restaurant;
import com.eatandpic.security.JwtTokenUtil;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {
	
	private static final Logger log = LoggerFactory.getLogger(RestaurantController.class);
	
	@Autowired
	private CityDao cityDao;
	
	@Autowired
	private RestaurantDao restaurantDao;
	
    /**
     * GET /getRestaurants  --> Get restaurants list
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
     * GET /getRestaurantsByName
     */
	@RequestMapping(value = "/getRestaurantsByName", method = RequestMethod.GET)
	@ResponseBody
	public List<Restaurant> getRestaurantsByName(HttpServletRequest request, HttpServletResponse response,
			@RequestParam String name){
		
		IpLocation ipLocation = null;
		List<Restaurant> restaurants = null;
		City city = null;
		  
		try{
			
			ipLocation = LocationFactory.getLocationFromHost(request.getRemoteHost());
			
			city = cityDao.findByName(ipLocation.getCityName());
			
			restaurants = restaurantDao.findByNameAndCity(name, city);
			
			if(restaurants == null || restaurants.isEmpty()){
				throw new RestaurantNotFoundException("Restaurant not found by name: " + name);
			}
			
			response.setStatus(HttpServletResponse.SC_OK);
			  
		} catch(RestaurantNotFoundException ex){
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
     * POST /save
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public Restaurant save(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Restaurant restaurant){
		
		Restaurant savedRestaurant = null;  
		
		try{
			
			savedRestaurant = restaurantDao.save(restaurant);
			
			response.setStatus(HttpServletResponse.SC_OK);
			  
		} catch(Exception ex){
			
			log.error("Error al guardar restaurante: " + ex.getMessage());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		
		} 
		
		return savedRestaurant;
		 
	}
}
