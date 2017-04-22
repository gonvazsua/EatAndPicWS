package com.plateandpic.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.plateandpic.dao.PlatePictureDao;
import com.plateandpic.models.PlatePicture;

@RestController
@RequestMapping("/platePicture")
public class PlatePictureController {
	
	private static final Logger log = LoggerFactory.getLogger(PlatePictureController.class);
	
	@Autowired
	private PlatePictureDao platePictureDao;
	
	/**
     * POST /save
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public PlatePicture save(HttpServletRequest request, HttpServletResponse response,
			@RequestBody PlatePicture platePicture){
		
		PlatePicture savedPlatePicture = null;  
		
		try{
			
			savedPlatePicture = platePictureDao.save(platePicture);
			
			response.setStatus(HttpServletResponse.SC_OK);
			  
		} catch(Exception ex){
			
			log.error("Error al guardar platePicture: " + ex.getMessage());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		
		} 
		
		return savedPlatePicture;
		 
	}

}
