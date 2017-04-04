package com.eatandpic.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.eatandpic.exceptions.IPNotFoundException;
import com.eatandpic.factory.FileFactory;
import com.eatandpic.factory.LocationFactory;
import com.eatandpic.models.IpLocation;
import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;

@RestController
@RequestMapping("/ipLocation")
public class IpLocationController {
	
	private static final Logger log = LoggerFactory.getLogger(IpLocationController.class);
	
	@Autowired
	private Environment env;
	
	
	@RequestMapping(value = "/getIpLocation", method = RequestMethod.GET)
	@ResponseBody
  	public IpLocation getIpLocation(HttpServletRequest request, HttpServletResponse response) {
		
		IpLocation ipLocation = null;
		
		try{
			
			ipLocation = LocationFactory.getLocationFromHost(request.getRemoteHost());
			
			response.setStatus(HttpServletResponse.SC_OK);
			
		} catch (IOException ex){
			log.error(ex.getMessage());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
			
		} catch (IPNotFoundException ex) {
			log.error(ex.getMessage());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
		
		return ipLocation;
		
	}
	
}
