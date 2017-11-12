package com.plateandpic.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.plateandpic.constants.MessageConstants;
import com.plateandpic.exceptions.PlateAndPicException;
import com.plateandpic.exceptions.RestaurantException;
import com.plateandpic.factory.RestaurantFactory;
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
	private RestaurantFactory restaurantFactory;
	
	@Value("${jwt.header}")
	private String tokenHeader;
	

	/**
	 * @param chars
	 * @return
	 * @throws RestaurantException 
	 */
	@RequestMapping(value = "/findByCityAndCategory", method = RequestMethod.GET)
	@ResponseBody
	public List<RestaurantRequestResponse> findByCityAndCategory(
			@RequestParam Long cityId, @RequestParam Long categoryId) throws RestaurantException{
		  
		List<RestaurantRequestResponse> restaurants = null;
		
		if(cityId != null && cityId > 0 && categoryId != null && categoryId > 0){
			
			restaurants = restaurantFactory.findByCityAndCategory(cityId, categoryId);
			
		} else {
			
			log.error("Params not valid in RestaurantController - findByCityAndCategory:" + cityId + ", " + categoryId);
			throw new RestaurantException(MessageConstants.RESTAURANT_SEARCH_ERROR);
			
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
			@RequestParam String name, 
			@RequestParam(value="latitude", required=false) Double latitude, 
			@RequestParam(value="longitude", required=false) Double longitude) throws PlateAndPicException {
		
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
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public RestaurantRequestResponse update(HttpServletRequest request, HttpServletResponse response,
			@RequestBody RestaurantRequestResponse restaurantRequest) throws PlateAndPicException{
		
		RestaurantRequestResponse savedRestaurant = null;
		
		savedRestaurant = restaurantFactory.validateAndSave(restaurantRequest);
			
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
	
	/**
	 * @param request
	 * @param response
	 * @return
	 * @throws PlateAndPicException 
	 * @throws RestaurantException 
	 */
	@RequestMapping(value = "/getUserRestaurant", method = RequestMethod.GET)
	@ResponseBody
	public RestaurantRequestResponse getUserRestaurant(HttpServletRequest request, HttpServletResponse response) throws PlateAndPicException {
		
		String token = null;
		RestaurantRequestResponse restaurant = null;
		
		try {
			
			token = request.getHeader(tokenHeader);
			
			restaurant = restaurantFactory.getUserRestaurant(token);
			
		} catch (PlateAndPicException e){
			log.error("Error in getUserRestaurant from RestaurantController: " + e.getStackTrace().toString());
			throw e;
		}
		
		return restaurant;
		 
	}
	
	@RequestMapping(value = "/savePicture", method = RequestMethod.POST, produces = MediaType.IMAGE_JPEG_VALUE)
	public String saveRestaurantPicture(@RequestBody MultipartFile image, HttpServletRequest request,
			HttpServletResponse response) throws PlateAndPicException {
	
		String token = null;
		String base64Img = null;
		
		try {
			
			token = request.getHeader(tokenHeader);
			
			base64Img = restaurantFactory.updatePicture(token, image);
			
		} catch(Exception e){
			log.error(e.getMessage());
			throw new PlateAndPicException(MessageConstants.RESTAURANT_PICTURE_NOT_SAVED);
		}
		
		return base64Img;
		
	}
}
