package com.plateandpic.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.plateandpic.dao.CityDao;
import com.plateandpic.exceptions.CityNotFoundException;
import com.plateandpic.models.City;

@RestController
@RequestMapping("/city")
public class CityController { 
	
	private static final Logger log = LoggerFactory.getLogger(CityController.class);
	
	@Autowired
	private CityDao cityDao;
	
	
	/**
     * GET /getCityByName  --> Get city by name
     */
	@RequestMapping(value = "/getCityByName", method = RequestMethod.GET)
	@ResponseBody
	public City getCityByName(HttpServletRequest request, HttpServletResponse response,
			@RequestParam String name){
		  
		City city = null;
		
		try{
			
			city = cityDao.findByName(name);
			
			if(city == null){
				throw new CityNotFoundException("No encontrada ciudad con nombre: " + name);
			}
			
			response.setStatus(HttpServletResponse.SC_OK);
			  
		} catch(CityNotFoundException e){
			log.error("No encontrada ciudad. " + e.getMessage());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		  
		return city;
		 
	}

}
