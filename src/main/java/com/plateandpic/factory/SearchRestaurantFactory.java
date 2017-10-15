package com.plateandpic.factory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plateandpic.apiplace.dto.ApiPlaceRequest;
import com.plateandpic.apiplace.dto.ApiPlaceResponse;
import com.plateandpic.apiplace.dto.Result;
import com.plateandpic.constants.ApiPlacesConstants;
import com.plateandpic.constants.ConstantsProperties;
import com.plateandpic.constants.MessageConstants;
import com.plateandpic.dao.RestaurantDao;
import com.plateandpic.exceptions.PlateAndPicException;
import com.plateandpic.models.Restaurant;

/**
 * @author gonzalo
 *
 */
@Service
public class SearchRestaurantFactory {
	
	@Autowired
	private RestaurantDao restaurantDao;
	
	@Autowired
	private Environment env;
	
	/**
	 * @param name
	 * @param latitude
	 * @param longitude
	 * @return
	 * 
	 * 1. Execute a search of restaurant by name
	 * 2. If someone exists, return it
	 * 3. If not exists, call to API Places with the name, latitude and longitude parameters
	 * 4. Save the results
	 * 5. Convert to a Restaurant object and return
	 * @throws PlateAndPicException 
	 */
	public List<Restaurant> executeUpdatedSearch(String name, Double latitude, Double longitude) throws PlateAndPicException {
		
		List<Restaurant> results = null;
		
		results = restaurantDao.findByNameContainingIgnoreCaseOrderByNameAsc(name);
		
		if(results == null || results.isEmpty()){
			
			results = getFromAPIPlaceAndSaveResults(name, latitude, longitude);
			
		}
		
		return results;
		
	}
	
	
	/**
	 * @param name
	 * @param latitude
	 * @param longitude
	 * @return
	 * 
	 * 1. Create the URL of the GET request
	 * 2. Map the results in ApiPlaceResponseObject
	 * 3. Convert to Restaurant object
	 * 4. Save restaurants
	 * 5. Return
	 * @throws PlateAndPicException 
	 * 
	 */
	private List<Restaurant> getFromAPIPlaceAndSaveResults(String name, Double latitude, Double longitude) throws PlateAndPicException{
		
		List<Restaurant> restaurants = null;
		
		try {
		
			String url = getUrlApiRequest(name, latitude, longitude);
			
			List<Result> apiPlaceresponseList = executeHttpGetRequest(url);
			
			restaurants = convertToRestaurantsObjects(apiPlaceresponseList);
			
			//saveRestaurants(restaurants);
			
		} catch(IOException e) {
			throw new PlateAndPicException(MessageConstants.RESTAURANT_SEARCH_ERROR);
		}
		
		return restaurants;
		
	}
	
	
	/**
	 * @param name
	 * @param latitude
	 * @param longitude
	 * @return
	 * 
	 * Create an ApiPlaceRequest object and get the API Endpoint
	 */
	private String getUrlApiRequest(String name, Double latitude, Double longitude){
		
		String endpoint = env.getProperty(ConstantsProperties.API_ENDPOINT);
		Integer radius = new Integer(env.getProperty(ConstantsProperties.API_RADIUS));
		String types = env.getProperty(ConstantsProperties.API_TYPES);
		String key = env.getProperty(ConstantsProperties.API_KEY);
		
		ApiPlaceRequest apiPlaceRequest = new ApiPlaceRequest(endpoint, radius, types, latitude, longitude, name, key);
		
		return apiPlaceRequest.buildURL();
		
	}
	
	/**
	 * @param url
	 * @return
	 * @throws IOException
	 * 
	 * Execute GET Request and map the result as Result list 
	 */
	private List<Result> executeHttpGetRequest(String url) throws IOException {
		
		List<Result> apiPlaceResponseList = null;
		
		URL obj = new URL(url);
		
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
		con.setRequestMethod(ApiPlacesConstants.GET);
		
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		
		StringBuffer response = getResponseAsStringBuffer(in);
		
		apiPlaceResponseList = mapResponseToApiPlaceResponse(response);
		
		return apiPlaceResponseList;
		
	}
	
	/**
	 * @param br
	 * @return
	 * @throws IOException
	 * 
	 * Convert the BufferReader response in StringBuffer
	 */
	private StringBuffer getResponseAsStringBuffer(BufferedReader br) throws IOException{
		
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = br.readLine()) != null) {
			response.append(inputLine);
		}
		
		br.close();
		
		return response;
		
	}
	
	/**
	 * @param response
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * 
	 * Convert the StringBuffer response in a ApiPlaceResponse object
	 */
	private List<Result> mapResponseToApiPlaceResponse(StringBuffer response) throws JsonParseException, JsonMappingException, IOException{
		
		String jsonResponse = response.toString();
		
		ObjectMapper mapper = new ObjectMapper();
		
		ApiPlaceResponse apiResponse = mapper.readValue(jsonResponse, ApiPlaceResponse.class);
		
		return apiResponse.getResults();
		
	}
	
	/**
	 * @param apiPlaceResponseList
	 * @return
	 * 
	 * Create a Restaurant list from ApiPlaceResponse list
	 */
	private List<Restaurant> convertToRestaurantsObjects(List<Result> apiPlaceResponseList){
		
		List<Restaurant> restaurants = new ArrayList<Restaurant>();
		Restaurant restaurant = null;
		
		for(Result result : apiPlaceResponseList){
			
			restaurant = buildRestaurantFromResult(result);
			restaurants.add(restaurant);
			
		}
		
		return restaurants;
		
	}
	
	/**
	 * @param result
	 * @return
	 */
	private Restaurant buildRestaurantFromResult(Result result){
		
		Restaurant restaurant = new Restaurant();
		
		
		return restaurant;
		
	}
}
