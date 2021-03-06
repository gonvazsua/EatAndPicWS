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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plateandpic.constants.MessageConstants;
import com.plateandpic.dao.PlatePictureDao;
import com.plateandpic.exceptions.PlateAndPicException;
import com.plateandpic.exceptions.PlatePictureException;
import com.plateandpic.exceptions.UserException;
import com.plateandpic.factory.PlatePictureFactory;
import com.plateandpic.models.Plate;
import com.plateandpic.models.PlatePicture;
import com.plateandpic.models.User;
import com.plateandpic.response.PlatePictureResponse;
import com.plateandpic.response.UserResponse;

/**
 * @author gonzalo
 *
 */
@RestController
@RequestMapping("/platePicture")
public class PlatePictureController {
	
	private static final Logger log = LoggerFactory.getLogger(PlatePictureController.class);
	
	@Autowired
	private PlatePictureFactory platePictureFactory;
	
	@Value("${jwt.header}")
	private String tokenHeader;
	
	/**
	 * @param request
	 * @param response
	 * @return
	 * @throws PlateAndPicException 
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public void save(HttpServletRequest request, HttpServletResponse response) throws PlateAndPicException{
		
		PlatePictureResponse platePicture = null;  
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
			
			platePicture = mapper.readValue(jsonPlatePicture, PlatePictureResponse.class);
			
			if(picture != null && platePicture != null){
				platePictureFactory.save(picture, platePicture, token);
			}
			else{
				throw new PlatePictureException("Incorrect parameters platePicture!");
			}
			
			response.setStatus(HttpServletResponse.SC_OK);
			  
		} catch(PlatePictureException e){
			log.error(e.getMessage());
			throw new PlatePictureException(MessageConstants.PLATEPICTURE_NOT_SAVED);
		} catch (JsonParseException e) {
			log.error(e.getMessage());
			throw new PlatePictureException(MessageConstants.PLATEPICTURE_NOT_SAVED);
		} catch (JsonMappingException e) {
			log.error(e.getMessage());
			throw new PlatePictureException(MessageConstants.PLATEPICTURE_NOT_SAVED);
		} catch (IOException e) {
			log.error(e.getMessage());
			throw new PlatePictureException(MessageConstants.PLATEPICTURE_NOT_SAVED);
		} catch (UserException e) {
			log.error(e.getMessage());
			throw new PlatePictureException(MessageConstants.PLATEPICTURE_NOT_SAVED);
		}
		 
	}
	
	/**
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws PlateAndPicException 
	 * @throws IOException 
	 */
	@RequestMapping(value = "/lastPlatePictures", method = RequestMethod.GET)
	@ResponseBody
	public List<PlatePictureResponse> lastPlatePictures(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam Integer page) throws PlateAndPicException, IOException{
		
		List<PlatePictureResponse> lastPlatePictures;
		String token = "";
			
		token = request.getHeader(tokenHeader);
		
		lastPlatePictures = platePictureFactory.getLastPlatePictures(token, page);
		
		response.setStatus(HttpServletResponse.SC_OK);
			
		return lastPlatePictures;
		 
	}
	
	/**
	 * @param request
	 * @param response
	 * @param platePictureId
	 * @return
	 */
	@RequestMapping(value = "/like", method = RequestMethod.POST)
	@ResponseBody
	public PlatePicture like(HttpServletRequest request, HttpServletResponse response,
			@RequestBody PlatePictureResponse platePicture){
		
		String token = "";
		
		try{
			
			token = request.getHeader(tokenHeader);
			
			platePictureFactory.likePlatePicture(token, platePicture.getPlatePictureId());
			
			response.setStatus(HttpServletResponse.SC_OK);
			  
		} catch(PlatePictureException ex){
			
			log.error("Error al guardar like platePicture: " + ex.getMessage());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		
		} catch (UserException e) {
			
			log.error("Error al guardar like platePicture: " + e.getMessage());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			
		} 
		
		return null;
		 
	}
	
	/**
	 * @param request
	 * @param response
	 * @param platePictureId
	 * @return
	 */
	@RequestMapping(value = "/unlike", method = RequestMethod.POST)
	@ResponseBody
	public PlatePicture unlike(HttpServletRequest request, HttpServletResponse response,
			@RequestBody PlatePictureResponse platePicture){
		
		String token = "";
		
		try{
			
			token = request.getHeader(tokenHeader);
			
			platePictureFactory.unlikePlatePicture(token, platePicture.getPlatePictureId());
			
			response.setStatus(HttpServletResponse.SC_OK);
			  
		} catch(PlatePictureException ex){
			
			log.error("Error al guardar unlike platePicture: " + ex.getMessage());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		
		} catch (UserException e) {
			
			log.error("Error al guardar unlike platePicture: " + e.getMessage());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			
		} 
		
		return null;
		 
	}
	
	/**
	 * @param request
	 * @param response
	 * @param username
	 * @param page
	 * @return
	 * @throws PlateAndPicException 
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getByUserId", method = RequestMethod.GET)
	@ResponseBody
	public List<PlatePictureResponse> getByUsername(HttpServletRequest request, HttpServletResponse response,
			@RequestParam String userId, @RequestParam Integer page) throws PlateAndPicException, IOException{
		
		List<PlatePictureResponse> lastPlatePictures = null;
		
		lastPlatePictures = platePictureFactory.getPlatePictureByUserId(userId, page);
			
		response.setStatus(HttpServletResponse.SC_OK);
		
		return lastPlatePictures;
		 
	}
	
	/**
	 * @param request
	 * @param response
	 * @param restaurantId
	 * @param page
	 * @return
	 * @throws PlateAndPicException 
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getByRestaurant", method = RequestMethod.GET)
	@ResponseBody
	public List<PlatePictureResponse> getByRestaurant(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Long restaurantId, @RequestParam Integer page) throws PlateAndPicException, IOException{
		
		List<PlatePictureResponse> lastPlatePictures = null;
		
		lastPlatePictures = platePictureFactory.getPlatePictureByRestaurantId(restaurantId, page);
			
		response.setStatus(HttpServletResponse.SC_OK);
		
		return lastPlatePictures;
		 
	}
	
	/**
	 * @param request
	 * @param response
	 * @param plateId
	 * @param page
	 * @return
	 * @throws PlateAndPicException 
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getByPlate", method = RequestMethod.GET)
	@ResponseBody
	public List<PlatePictureResponse> getByPlate(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Long plateId, @RequestParam Integer page) throws PlateAndPicException, IOException{
		
		List<PlatePictureResponse> lastPlatePictures = null;
		
		lastPlatePictures = platePictureFactory.getPlatePictureByPlateId(plateId, page);
			
		response.setStatus(HttpServletResponse.SC_OK);
		
		return lastPlatePictures;
		 
	}
	
	@RequestMapping(value = "/getById", method = RequestMethod.GET)
	@ResponseBody
	public PlatePictureResponse getById(HttpServletResponse response,
			@RequestParam Long platePictureId) throws PlateAndPicException, IOException{
		
		PlatePictureResponse platePicture = null;
		
		platePicture = platePictureFactory.getPlatePictureResponseById(platePictureId);
			
		response.setStatus(HttpServletResponse.SC_OK);
		
		return platePicture;
		 
	}

}
