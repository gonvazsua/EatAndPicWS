package com.plateandpic.factory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.plateandpic.constants.ConstantsProperties;
import com.plateandpic.constants.MessageConstants;
import com.plateandpic.dao.CategoryDao;
import com.plateandpic.dao.CityDao;
import com.plateandpic.dao.RestaurantDao;
import com.plateandpic.exceptions.CityException;
import com.plateandpic.exceptions.PlateAndPicException;
import com.plateandpic.exceptions.RestaurantException;
import com.plateandpic.exceptions.UserException;
import com.plateandpic.models.Category;
import com.plateandpic.models.City;
import com.plateandpic.models.Restaurant;
import com.plateandpic.models.User;
import com.plateandpic.response.RestaurantRequestResponse;
import com.plateandpic.utils.DateUtils;
import com.plateandpic.utils.ParserUtil;
import com.plateandpic.validator.RestaurantValidator;

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
	
	@Autowired
	private CategoryDao categoryDao;
	
	@Autowired
	private CityFactory cityFactory;
	
	@Autowired
	private UserFactory userFactory;
	
	@Autowired
	private Environment env;
	
	/**
	 * @param restaurantId
	 * @return
	 * @throws RestaurantException 
	 * 
	 * Find a Restaurant object by id
	 */
	public Restaurant findRestaurantById(Long restaurantId) throws RestaurantException{
		
		Restaurant restaurant = null;
		
		if(restaurantId != null){
			
			restaurant = restaurantDao.findOne(restaurantId);
			
		} else {
			
			throw new RestaurantException(MessageConstants.RESTAURANT_NOT_FOUND);
			
		}
		
		return restaurant;
		
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
		
		if(restaurant.getPicture() != null){
			response.setPicture(getbase64Picture(restaurant.getPicture()));
		}
		
		return response;
		
	}
	
	/**
	 * @param picture
	 * @return
	 */
	private String getbase64Picture(String picture){
		
		String returnedImg = null;
		
		try {
			
			returnedImg = FileFactory.getBase64FromProfilePictureName(env.getProperty(ConstantsProperties.RESTAURANT_PICTURE_PATH), 
					picture);
			
		} catch (Exception e){
			log.error(e.getMessage());
			returnedImg = null;
		}
		
		return returnedImg;
		
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
	private Restaurant buildRestaurantFromRequest(RestaurantRequestResponse restaurantToSave) throws RestaurantException {
		
		Restaurant restaurant = null;
		City city = null;
		
		try {
			
			restaurant = new Restaurant();
		
			city = cityFactory.getCityByNameOrLatitudeLongitude(restaurantToSave.getCityName(), restaurantToSave.getLatitude(),
					restaurantToSave.getLongitude());
			
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
			
		} catch (CityException e){
			log.error("City not found building restaurant with restaurant name: " + restaurant.getName() + "latLong: " + 
					restaurantToSave.getLatitude() + ", " + restaurantToSave.getLongitude());
			throw new RestaurantException(MessageConstants.RESTAURANT_NOT_FOUND);
		}
		
		return restaurant;
		
	}
	
	/**
	 * @param cityId
	 * @param categoryId
	 * @return
	 * @throws RestaurantException
	 * 
	 * Find Restaurants with the city and category passed as parameter and build a RestaurantRequestResponse
	 */
	public List<RestaurantRequestResponse> findByCityAndCategory(Long cityId, Long categoryId) throws RestaurantException{
		
		List<RestaurantRequestResponse> results = new ArrayList<RestaurantRequestResponse>();
		List<Restaurant> restaurants = null;
		
		City city = cityDao.findOne(cityId);
		Category category = categoryDao.findOne(categoryId);
		
		if(city == null || category == null){
			throw new RestaurantException(MessageConstants.RESTAURANT_NOT_FOUND);
		}
		
		restaurants = restaurantDao.findAllByCityAndCategories(city, category);
		
		if(restaurants != null && !restaurants.isEmpty()){
			results = buildListRestaurantRequestResponse(restaurants);
		}
		
		return results;
		
	}
	
	/**
	 * @param token
	 * @return
	 * @throws RestaurantException 
	 * @throws UserException 
	 * 
	 * Get the restaurant of the logged user, and parse to the response object
	 */
	public RestaurantRequestResponse getUserRestaurant(String token) throws RestaurantException, UserException{
		
		RestaurantRequestResponse response = null;
		
		Restaurant restaurant = getRestaurantFromUserToken(token);
		
		response = buildRestaurantRequestResponse(restaurant);
		
		return response;
		
	}
	
	
	/**
	 * @param restaurant
	 * @throws PlateAndPicException
	 * 
	 * Validate a Restaurant object
	 */
	private void validateRestaurant(Restaurant restaurant) throws PlateAndPicException {
		
		RestaurantValidator validator = new RestaurantValidator(restaurant);
		
		validator.validate();
		
	}
	
	/**
	 * @param request
	 * @return
	 * @throws PlateAndPicException
	 * 
	 * Validate and save a RestaurantRequestResponse object
	 */
	public RestaurantRequestResponse validateAndSave(RestaurantRequestResponse request) throws PlateAndPicException{
		
		RestaurantRequestResponse response = null;
		
		Restaurant restaurant = restaurantDao.findOne(request.getRestaurantId());
		
		if(restaurant == null){
			log.error("Restaurant not found with ID: " + request.getRestaurantId());
			throw new RestaurantException(MessageConstants.RESTAURANT_NOT_FOUND);
		}
		
		updateRestaurantFromRequest(restaurant, request);
		
		validateRestaurant(restaurant);
		
		restaurant = restaurantDao.save(restaurant);
		
		response = buildRestaurantRequestResponse(restaurant);
		
		return response;
		
	}
	
	/**
	 * @param restaurant
	 * @param request
	 * 
	 * Update allowed fields from request
	 */
	private void updateRestaurantFromRequest(Restaurant restaurant, RestaurantRequestResponse request) {
		
		restaurant.setAddress(request.getAddress());
		restaurant.setPhoneNumber(request.getPhoneNumber());
		restaurant.setPriceAverage(request.getPriceAverage());
		restaurant.setDescription(request.getDescription());
		
	}
	
	/**
	 * @param token
	 * @param image
	 * @return
	 * @throws IOException
	 * @throws PlateAndPicException
	 * 
	 * 1. Get the user and their restaurant
	 * 2. Upload file and set the name:  path+restaurntId+.jpg
	 * 3. Update the new file name to the restaurant
	 * 4. Convert to base64 and return
	 */
	public String updatePicture(String token, MultipartFile image) throws IOException, PlateAndPicException{
		
		String base64Img = null;
		String fileName = null;
		
		Restaurant restaurant = getRestaurantFromUserToken(token);
		
		fileName = FileFactory.uploadRestaurantPicture(image, restaurant.getRestaurantId());
		
		restaurant.setPicture(fileName);
		
		restaurantDao.save(restaurant);
		
		base64Img = FileFactory.getBase64FromProfilePictureName(env.getProperty(ConstantsProperties.RESTAURANT_PICTURE_PATH),
				fileName);
		
		return base64Img;
		
	}
	
	/**
	 * @param token
	 * @return
	 * @throws RestaurantException
	 * @throws UserException
	 */
	public Restaurant getRestaurantFromUserToken(String token) throws RestaurantException, UserException{
		
		User loggedUser = userFactory.getUserFromToken(token);
		
		Restaurant restaurant = loggedUser.getRestaurant();
		
		if(restaurant == null){
			log.error("Restaurant not found for userId: " + loggedUser.getId());
			throw new RestaurantException(MessageConstants.RESTAURANT_NOT_FOUND);
		}
		
		return restaurant;
		
	}

}
