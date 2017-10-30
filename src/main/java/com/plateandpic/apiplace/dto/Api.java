package com.plateandpic.apiplace.dto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plateandpic.constants.ApiPlacesConstants;
import com.plateandpic.constants.MessageConstants;
import com.plateandpic.exceptions.PlateAndPicException;

/**
 * @author gonzalo
 * 
 * This class is used for make a HTTP GET call to a correspondent API
 * There is an abstract method for building the URL of the API to call
 * Return the results as a list of Result object
 * 
 */
public abstract class Api {
	
	private static final Logger log = LoggerFactory.getLogger(Api.class);
	
	private String endpoint;
	private String apiKey;
	
	/**
	 * @param endpoint
	 * @param apiKey
	 */
	public Api(String endpoint, String apiKey){
		
		this.endpoint = endpoint;
		this.apiKey = apiKey;
		
	}

	/**
	 * @return the endpoint
	 */
	public String getEndpoint() {
		return endpoint;
	}

	/**
	 * @param endpoint the endpoint to set
	 */
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	/**
	 * @return the apiKey
	 */
	public String getApiKey() {
		return apiKey;
	}

	/**
	 * @param apiKey the apiKey to set
	 */
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	
	/**
	 * @return
	 * 
	 * Abstract method for building URL:
	 * 1- Api places
	 * 2. Api cities
	 */
	public abstract String buildUrl() throws PlateAndPicException;
	
	/**
	 * @return
	 * @throws PlateAndPicException
	 * 
	 * Execute the GET call to the URL and parse the response to a Result object list
	 */
	public List<Result> executeGetMethod() throws PlateAndPicException{
		
		List<Result> apiPlaceResponseList = null;
		
		try{
			
			String url = this.buildUrl();
			
			URL obj = new URL(url);
			
			log.debug("Calling to api : " + url);
			
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			
			con.setRequestMethod(ApiPlacesConstants.GET);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			
			StringBuffer response = getResponseAsStringBuffer(in);
			
			apiPlaceResponseList = mapResponseToApiPlaceResponse(response);
			
		} catch (MalformedURLException e) {
			log.error(e.getMessage());
			throw new PlateAndPicException(MessageConstants.RESTAURANT_SEARCH_ERROR);
		} catch (IOException e) {
			log.error(e.getMessage());
			throw new PlateAndPicException(MessageConstants.RESTAURANT_SEARCH_ERROR);
		}
		
		
		return apiPlaceResponseList;
		
	}
	
	/**
	 * @param br
	 * @return
	 * @throws IOException
	 * 
	 * Convert the BufferReader response in StringBuffer
	 */
	private StringBuffer getResponseAsStringBuffer(BufferedReader br) throws IOException {
		
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

}
