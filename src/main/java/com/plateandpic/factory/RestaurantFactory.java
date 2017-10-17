package com.plateandpic.factory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plateandpic.constants.MessageConstants;
import com.plateandpic.dao.CityDao;
import com.plateandpic.dao.RestaurantDao;
import com.plateandpic.exceptions.PlateAndPicException;
import com.plateandpic.exceptions.RestaurantException;
import com.plateandpic.models.City;
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
	
	private static final Logger log = LoggerFactory.getLogger(RestaurantFactory.class);
	
	@Autowired
	private RestaurantDao restaurantDao;
	
	@Autowired 
	private SearchRestaurantFactory searchRestaurantFactory;
	
	@Autowired
	private CityDao cityDao;
	
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
		response.setLatitude(restaurant.getLatitude());
		response.setLongitude(restaurant.getLongitude());
		response.setApiPlaceId(restaurant.getApiPlaceId());
		
		if(restaurant.getRating() != null){
			response.setRating(restaurant.getRating().toString());
		}
		
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
	 * @throws PlateAndPicException 
	 */
	public List<RestaurantRequestResponse> searchRestaurantsByName(String name, Double latitude, Double longitude) throws PlateAndPicException{
		
		List<RestaurantRequestResponse> response = new ArrayList<RestaurantRequestResponse>();
		
		List<Restaurant> restaurants = searchRestaurantFactory.executeUpdatedSearch(name, latitude, longitude);
		
		if(restaurants != null && !restaurants.isEmpty()){
			response = buildListRestaurantRequestResponse(restaurants);
		}
		
		return response;
		
	}
	
	/**
	 * @param restaurantToSave
	 * @return
	 * 
	 * Save the restaurant passed as parameter if not exists by name and city name
	 * @throws RestaurantException 
	 */
	public RestaurantRequestResponse saveIfNotExists(RestaurantRequestResponse restaurantToSave) throws RestaurantException{
		
		Restaurant savedRestaurant = findByNameAndCity(restaurantToSave.getName().trim(), restaurantToSave.getCityName().trim());
		
		if(savedRestaurant == null)
			savedRestaurant = buildAndSave(restaurantToSave);
		
		return buildRestaurantRequestResponse(savedRestaurant);
		
	}
	
	/**
	 * @param name
	 * @param cityName
	 * @return
	 * 
	 * Find a restaurant list by Name and City name
	 */
	private Restaurant findByNameAndCity(String name, String cityName){
		
		Restaurant restaurant = null;
		List<Restaurant> restaurants = restaurantDao.findByNameContainingIgnoreCaseAndCity_name(name, cityName);
		
		if(restaurants != null && !restaurants.isEmpty()){
			restaurant = restaurants.get(0);
		}
		
		return restaurant;
		
	}
	
	/**
	 * @param restaurantToSave
	 * @return
	 * @throws RestaurantException
	 * 
	 * Build a Restaurant object from RestaurantRequestResponse one and save it
	 */
	private Restaurant buildAndSave(RestaurantRequestResponse restaurantToSave) throws RestaurantException {
		
		Restaurant restaurant = buildRestaurantFromRequest(restaurantToSave);
		
		restaurant = restaurantDao.save(restaurant);
		
		return restaurant;
		
	}
	
	/**
	 * @param restaurantToSave
	 * @return
	 * @throws RestaurantException
	 * 
	 * Build a Restaurant object from a RestaurantRequestResponse one
	 */
	private Restaurant buildRestaurantFromRequest(RestaurantRequestResponse restaurantToSave) throws RestaurantException{
		
		Restaurant restaurant = new Restaurant();
		
		City city = cityDao.findByNameIgnoreCase(restaurantToSave.getCityName().trim());
		
		if(city == null){
			log.error("Not found city with name: " + restaurantToSave.getCityName());
			throw new RestaurantException(MessageConstants.RESTAURANT_SEARCH_ERROR);
		}
		
		restaurant.setCity(city);
		restaurant.setName(restaurantToSave.getName());
		restaurant.setAddress(restaurantToSave.getAddress());
		restaurant.setRegisteredOn(new Date());
		restaurant.setActive(true);
		restaurant.setLatitude(restaurantToSave.getLatitude());
		restaurant.setLongitude(restaurantToSave.getLongitude());
		restaurant.setApiPlaceId(restaurantToSave.getApiPlaceId());
		restaurant.setPriceAverage(restaurantToSave.getPriceAverage());
		
		if(restaurantToSave.getRating() != null){
			restaurant.setRating(new Double(restaurantToSave.getRating()));
		}
		
		return restaurant;
		
	}

}
