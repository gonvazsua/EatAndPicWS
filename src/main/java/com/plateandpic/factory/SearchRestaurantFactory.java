package com.plateandpic.factory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plateandpic.apiplace.dto.Api;
import com.plateandpic.apiplace.dto.ApiPlaceRequest;
import com.plateandpic.apiplace.dto.ApiPlaceResponse;
import com.plateandpic.apiplace.dto.Result;
import com.plateandpic.constants.ApiPlacesConstants;
import com.plateandpic.constants.ConstantsProperties;
import com.plateandpic.constants.MessageConstants;
import com.plateandpic.dao.RestaurantDao;
import com.plateandpic.exceptions.PlateAndPicException;
import com.plateandpic.models.City;
import com.plateandpic.models.Restaurant;
import com.plateandpic.utils.ParserUtil;

/**
 * @author gonzalo
 *
 */
@Service
public class SearchRestaurantFactory {
	
	private static final Logger log = LoggerFactory.getLogger(SearchRestaurantFactory.class);
	
	@Autowired
	private RestaurantDao restaurantDao;
	
	@Autowired
	private Environment env;
	
	/**
	 * @param name
	 * @param latitude
	 * @param longitude
	 * @return
	 * @throws PlateAndPicException 
	 * 
	 * 1. Execute a search of restaurant by name
	 * 2. If someone exists, return it
	 * 3. If not exists, call to API Places with the name, latitude and longitude parameters
	 * 4. Save the results
	 * 5. Convert to a Restaurant object and return
	 * 
	 * NEW: Execute BD Search and complete the search with API Response
	 */
	public List<Restaurant> executeUpdatedSearch(String name, Double latitude, Double longitude) throws PlateAndPicException {
		
		List<Restaurant> results = new ArrayList<Restaurant>();
		
		results.addAll(restaurantDao.findByNameContainingIgnoreCaseOrderByNameAsc(name));
		
		//Old method
//		if(results == null || results.isEmpty()){
//			
//			//results = getFromAPIPlaceAndSaveResults(name, latitude, longitude);
//			
//		}
		
		//New: Complete search with api results
		results.addAll(getFromAPIPlaceAndSaveResults(name, latitude, longitude));
		
		clearEquals(results);
		
		return results;
		
	}
	
	
	/**
	 * @param name
	 * @param latitude
	 * @param longitude
	 * @return
	 * 
	 * 1. Build the ApiPlateRequest and call to API
	 * 2. Map the results in ApiPlaceResponseObject
	 * 3. Convert to Restaurant object
	 * 4. Save restaurants
	 * 5. Return
	 * @throws PlateAndPicException 
	 * 
	 */
	private List<Restaurant> getFromAPIPlaceAndSaveResults(String name, Double latitude, Double longitude) throws PlateAndPicException {
		
		Api apiPlaces = getApiPlateRequest(name, latitude, longitude);
			
		List<Result> apiPlaceresponseList = apiPlaces.executeGetMethod();
			
		List<Restaurant> restaurants = convertToRestaurantsObjects(apiPlaceresponseList);
			
		//saveRestaurants(restaurants);
		
		return restaurants;
		
	}
	
	private Api getApiPlateRequest(String name, Double latitude, Double longitude){
		
		
		Api apiPlaceRequest = null;
		
		String endpoint = env.getProperty(ConstantsProperties.API_ENDPOINT);
		
		Integer radius = new Integer(env.getProperty(ConstantsProperties.API_RADIUS));
		
		String types = env.getProperty(ConstantsProperties.API_TYPES);
		
		String apiKey = env.getProperty(ConstantsProperties.API_KEY);
		
		apiPlaceRequest = new ApiPlaceRequest(endpoint, name, types, apiKey, radius, latitude, longitude);
		
		return apiPlaceRequest;
		
	}
	
	/**
	 * @param apiPlaceResponseList
	 * @return
	 * 
	 * Create a Restaurant list from ApiPlaceResponse list
	 */
	private List<Restaurant> convertToRestaurantsObjects(List<Result> apiPlaceResponseList){
		
		List<Restaurant> restaurants = new ArrayList<Restaurant>();
		
		for(Result result : apiPlaceResponseList){
			
			buildRestaurantFromResultAndAppendToList(result, restaurants);
			
		}
		
		return restaurants;
		
	}
	
	/**
	 * @param result
	 * @return
	 * 
	 * If some result cannot be parsed,  go to the next one
	 */
	private void buildRestaurantFromResultAndAppendToList(Result result, List<Restaurant> restaurants){
		
		Restaurant restaurant = null;
		City city = null;
		
		try {
			
			restaurant = new Restaurant();
			city = new City();
			
			restaurant.setActive(true);
			restaurant.setApiPlaceId(result.getPlace_id());
			restaurant.setLatitude(result.getGeometry().getLocation().getLat().toString());
			restaurant.setLongitude(result.getGeometry().getLocation().getLng().toString());
			restaurant.setName(result.getName());
			restaurant.setRating(result.getRating());
			
			if(result.getFormatted_address() != null){
				
				restaurant.setAddress(ParserUtil.getAddressFromFormattedAddress(result.getFormatted_address()));
				city.setName(ParserUtil.getCityNameFromFormattedAPIAddress(result.getFormatted_address()));
				restaurant.setCity(city);
				
			}
			
			restaurants.add(restaurant);
			
		} catch (Exception e){
			log.error(e.getStackTrace().toString());
		}
		
	}
	
	/**
	 * @param results
	 * 
	 * Delete equals Restaurants from list:
	 * 	Loop over list two times if remove one with no ID and call to the same function (recursion)
	 */
	private void clearEquals(List<Restaurant> results){
		
		Restaurant r1 = null;
		Restaurant r2 = null;
		
		for(int i = 0; i < results.size(); i++){
			
			r1 = (Restaurant) results.get(i);
			
			for(int j = i + 1; j < results.size(); j++){
				
				r2 = (Restaurant) results.get(j);
				
				if(r1.equals(r2)){
					
					if(r1.getRestaurantId() != r2.getRestaurantId()
							&& r1.getRestaurantId() > 0){
						
						results.remove(j);
						clearEquals(results);
						break;
						
					} else if(r1.getRestaurantId() != r2.getRestaurantId()
							&& r2.getRestaurantId() > 0){
						
						results.remove(i);
						clearEquals(results);
						break;
						
					}
					
				}
				
			}
			
			break;
			
		}
		
	}
}
