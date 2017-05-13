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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plateandpic.dao.PlatePictureDao;
import com.plateandpic.exceptions.PlatePictureException;
import com.plateandpic.factory.PlatePictureFactory;
import com.plateandpic.models.PlatePicture;
import com.plateandpic.response.PlatePictureResponse;

@RestController
@RequestMapping("/platePicture")
public class PlatePictureController {
	
	private static final Logger log = LoggerFactory.getLogger(PlatePictureController.class);
	
	@Autowired
	private PlatePictureFactory platePictureFactory;
	
	@Value("${jwt.header}")
	private String tokenHeader;
	
	/**
     * POST /save
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public PlatePicture save(HttpServletRequest request, HttpServletResponse response){
		
		PlatePicture savedPlatePicture = null, platePicture = null;  
		MultipartRequest multipartRequest = null;
		String jsonPlatePicture = null;	
		MultipartFile picture = null;
		ObjectMapper mapper = null;
		String token = "";
		
		try{
			
			token = request.getHeader(tokenHeader);
			
			mapper = new ObjectMapper();
			
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			
			multipartRequest = (MultipartRequest) request;
			
			picture = multipartRequest.getFile("image");
			
			jsonPlatePicture = request.getParameter("platePicture");
			
			platePicture = mapper.readValue(jsonPlatePicture, PlatePicture.class);
			
			if(picture != null && platePicture != null){
				savedPlatePicture = platePictureFactory.save(picture, platePicture, token);
			}
			else{
				throw new PlatePictureException("Incorrect parameters platePicture!");
			}
			
			response.setStatus(HttpServletResponse.SC_OK);
			  
		} catch(Exception ex){
			
			log.error("Error al guardar platePicture: " + ex.getMessage());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		
		} 
		
		return savedPlatePicture;
		 
	}
	
	/**
     * GET /lastPlatePictures
     */
	@RequestMapping(value = "/lastPlatePictures", method = RequestMethod.GET)
	@ResponseBody
	public List<PlatePictureResponse> lastPlatePictures(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam Integer page){
		
		List<PlatePictureResponse> lastPlatePictures;
		String token = "";
		
		try{
			
			token = request.getHeader(tokenHeader);
			
			lastPlatePictures = platePictureFactory.getLastPlatePictures(token, page);
			
			response.setStatus(HttpServletResponse.SC_OK);
			  
		} catch(PlatePictureException ex){
			
			lastPlatePictures = null;
			log.error("Error getting last PlatePictures: page = " + page + " -> " + ex.getMessage());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		
		} catch (IOException e) {
			
			lastPlatePictures = null;
			log.error("Error getting last PlatePictures: page = " + page + " -> " + e.getMessage());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			
		} 
		
		return lastPlatePictures;
		 
	}

}
