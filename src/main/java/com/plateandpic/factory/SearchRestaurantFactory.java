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
	 * 1. Create the URL of the GET request
	 * 2. Map the results in ApiPlaceResponseObject
	 * 3. Convert to Restaurant object
	 * 4. Save restaurants
	 * 5. Return
	 * @throws PlateAndPicException 
	 * 
	 */
	private List<Restaurant> getFromAPIPlaceAndSaveResults(String name, Double latitude, Double longitude) throws PlateAndPicException {
		
		List<Restaurant> restaurants = null;
		
		try {
		
			String url = getUrlApiRequest(name, latitude, longitude);
			
			List<Result> apiPlaceresponseList = executeHttpGetRequest(url);
			
			restaurants = convertToRestaurantsObjects(apiPlaceresponseList);
			
			//saveRestaurants(restaurants);
			
		} catch(IOException e) {
			log.error(e.getStackTrace().toString());
			throw new PlateAndPicException(MessageConstants.RESTAURANT_SEARCH_ERROR);
		} catch (URISyntaxException e) {
			log.error(e.getStackTrace().toString());
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
	 * @throws UnsupportedEncodingException 
	 * @throws PlateAndPicException 
	 */
	private String getUrlApiRequest(String name, Double latitude, Double longitude) throws PlateAndPicException{
		
		String url = "";
		ApiPlaceRequest apiPlaceRequest = null;
		
		//Get common params
		String endpoint = env.getProperty(ConstantsProperties.API_ENDPOINT);
		Integer radius = new Integer(env.getProperty(ConstantsProperties.API_RADIUS));
		String types = env.getProperty(ConstantsProperties.API_TYPES);
		String apiKey = env.getProperty(ConstantsProperties.API_KEY);
		apiPlaceRequest = new ApiPlaceRequest(endpoint, name, types, apiKey, radius, latitude, longitude);
		
		url = apiPlaceRequest.buildURL();
		
		log.debug("Calling to url: " + url);
		
		return url;
		
	}
	
	/**
	 * @param url
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException 
	 * 
	 * Execute GET Request and map the result as Result list 
	 * First, the url is cleaned with URI object to scape illegal characters
	 */
	private List<Result> executeHttpGetRequest(String url) throws IOException, URISyntaxException {
		
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
		
		String jsonResponse = response.toString(); System.out.println(jsonResponse);
		
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
