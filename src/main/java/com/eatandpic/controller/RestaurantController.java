package com.eatandpic.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.eatandpic.models.Restaurant;
import com.eatandpic.security.JwtTokenUtil;

@RestController
public class RestaurantController {
	
	@Value("${jwt.header}")
	private String tokenHeader;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	/**
	   * POST /updateEmail  --> Update the user email
	   * email.
	   */
	  @RequestMapping(value = "/getRestaurants", method = RequestMethod.POST)
	  @ResponseBody
	  public List<Restaurant> getRestaurants(String chars){
		  
		  List<Restaurant> restaurants = null;
		  
		  try{
			  
		  } catch(Exception e){
			  restaurants = null;
		  }
		  
		  return restaurants;
		  
	  }

}
