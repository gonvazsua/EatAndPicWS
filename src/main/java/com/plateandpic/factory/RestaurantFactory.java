package com.plateandpic.factory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plateandpic.constants.MessageConstants;
import com.plateandpic.dao.RestaurantDao;
import com.plateandpic.exceptions.RestaurantException;
import com.plateandpic.models.Restaurant;
import com.plateandpic.response.RestaurantRequestResponse;
import com.plateandpic.utils.DateUtils;
import com.plateandpic.utils.ParserUtil;
import com.plateandpic.utils.StringUtils;

/**
 * @author gonzalo
 *
 */
@Service
public class RestaurantFactory {
	
	@Autowired
	private RestaurantDao restaurantDao;
	
	/**
	 * @param restaurant
	 * @return
	 */
	public Restaurant buildAndSave(Restaurant restaurant){
		
		restaurant.setRegisteredOn(new Date());
		
		restaurant = restaurantDao.save(restaurant);
		
		return restaurant;
		
	}
	
	
	/**
	 * @param restaurant
	 * @return
	 */
	public Restaurant findByApiPlaceId(Restaurant restaurant){
		
		Restaurant savedRestaurant = null;
		List<Restaurant> restaurants = restaurantDao.findByApiPlaceId(restaurant.getApiPlaceId());
		
		if(restaurants != null && restaurants.size() > 0){
			
			savedRestaurant = restaurants.get(0);
			
		}
		
		return savedRestaurant;
		
	}
	
	/**
	 * @param id
	 * @return
	 * @throws RestaurantException
	 * 
	 * Load a RestaurantRequestResponse from RestaurantId
	 */
	public RestaurantRequestResponse getById(Long id) throws RestaurantException{
		
		RestaurantRequestResponse response = null;
		
		Restaurant restaurant = restaurantDao.findOne(id);
		
		if(restaurant == null){
			throw new RestaurantException(MessageConstants.RESTAURANT_NOT_FOUND);
		}
		
		response = buildRestaurantRequestResponse(restaurant);
		
		return response;
		
	}
	
	
	/**
	 * @param restaurants
	 * @return
	 * 
	 * Build a List of RestaurantRequestResponse from a given Restaurants list
	 */
	private List<RestaurantRequestResponse> buildListRestaurantRequestResponse(List<Restaurant> restaurants){
		
		List<RestaurantRequestResponse> response = new ArrayList<RestaurantRequestResponse>();
		RestaurantRequestResponse rr = null;
		
		for(Restaurant restaurant : restaurants){
			
			rr = buildRestaurantRequestResponse(restaurant);
			response.add(rr);
			
		}
		
		return response;
		
	}
	
	/**
	 * @param restaurant
	 * @return
	 * 
	 * Build new RestaurantRequestResponse from a Restaurant object
	 */
	private RestaurantRequestResponse buildRestaurantRequestResponse(Restaurant restaurant){
		
		RestaurantRequestResponse response = new RestaurantRequestResponse();
		
		response.setRestaurantId(restaurant.getRestaurantId());
		response.setName(restaurant.getName());
		response.setAddress(restaurant.getAddress());
		response.setPhoneNumber(restaurant.getPhoneNumber());
		response.setRegisteredOn(DateUtils.getDateDDMMYYY(restaurant.getRegisteredOn()));
		response.setCityId(restaurant.getCity().getCityId());
		response.setCityName(restaurant.getCity().getName());
		response.setPriceAverage(restaurant.getPriceAverage());
		response.setPicture(restaurant.getPicture());
		response.setDescription(restaurant.getDescription());
		
		if(restaurant.getCategories() != null && !restaurant.getCategories().isEmpty()){
			response.setCategories(ParserUtil.getCategoriesAsString(restaurant.getCategories()));
		}
		
		return response;
		
	}
	
	
	/**
	 * @param name
	 * @return
	 * 
	 * Search restaurants by name and build a RestaurantRequestResponse objects list
	 */
	public List<RestaurantRequestResponse> searchRestaurantsByName(String name){
		
		List<RestaurantRequestResponse> response = new ArrayList<RestaurantRequestResponse>();
		
		List<Restaurant> restaurants = restaurantDao.findByNameContainingIgnoreCaseOrderByNameAsc(name);
		
		if(restaurants != null && !restaurants.isEmpty()){
			response = buildListRestaurantRequestResponse(restaurants);
		}
		
		return response;
		
	}

}
