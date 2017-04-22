package com.plateandpic.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.plateandpic.dao.PlateDao;
import com.plateandpic.dao.RestaurantDao;
import com.plateandpic.models.Plate;
import com.plateandpic.models.Restaurant;

@RestController
@RequestMapping("/plate")
public class PlateController {
	
	@Autowired
	private PlateDao plateDao;
	
	@Autowired
	private RestaurantDao restaurantDao;
	
	/**
     * POST /savePlate  --> Save new Plate and return it
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public Plate save(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Plate plate){
		  
		Restaurant restaurant = null;	
		
		try{
			
			restaurant = restaurantDao.findOne(plate.getRestaurant().getRestaurantId());
			
			plate.setRestaurant(restaurant);
			
			plateDao.save(plate);
			
			response.setStatus(HttpServletResponse.SC_OK);
			  
		} catch(Exception e){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			plate = null;
		}
		  
		return plate;
		 
	}
	
}
