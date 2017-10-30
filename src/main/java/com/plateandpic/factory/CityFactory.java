package com.plateandpic.factory;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.plateandpic.apiplace.dto.Api;
import com.plateandpic.apiplace.dto.ApiCityRequest;
import com.plateandpic.apiplace.dto.Result;
import com.plateandpic.constants.ConstantsProperties;
import com.plateandpic.dao.CityDao;
import com.plateandpic.dao.ProvinceDao;
import com.plateandpic.exceptions.CityException;
import com.plateandpic.exceptions.PlateAndPicException;
import com.plateandpic.exceptions.ProvinceException;
import com.plateandpic.models.City;
import com.plateandpic.models.Province;
import com.plateandpic.response.CityResponse;

/**
 * @author gonzalo
 *
 */
@Service
public class CityFactory {
	
	private static final Logger log = LoggerFactory.getLogger(CityFactory.class);

	@Autowired
	private CityDao cityDao;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private ProvinceFactory provinceFactory;

	/**
	 * @param name
	 * @return
	 * 
	 * Call to cityDao to find cities with name passed as parameter
	 */
	public List<CityResponse> findCitiesByName(String name){
		
		List<CityResponse> cityResponse = null;
		
		List<City> cities = cityDao.findByNameContainingIgnoreCaseOrderByNameAsc(name);
		
		if(cities != null && !cities.isEmpty()){
			
			cityResponse = buildCityResponseFromCityList(cities);
			
		} else {
			
			log.debug("Not found cities with name: " + name);
			cityResponse = new ArrayList<CityResponse>();
			
		}
		
		return cityResponse;
		
	}
	
	/**
	 * @param cities
	 * @return
	 * 
	 * Build CityResponse object list from City list
	 */
	private List<CityResponse> buildCityResponseFromCityList(List<City> cities){
		
		List<CityResponse> cityResponseList = new ArrayList<CityResponse>();
		CityResponse cityResponse = null;
		
		for(City city : cities){
			
			cityResponse = new CityResponse(city);
			cityResponseList.add(cityResponse);
			
		}
		
		return cityResponseList;
		
	}
	
	/**
	 * @param name
	 * @param latitude
	 * @param longitude
	 * @return
	 * 
	 * Find a City in BD with the name passed as parameter. It any result is returned,
	 * then call to City Api with the latitude and longitude and save it
	 * @throws CityException 
	 */
	public City getCityByNameOrLatitudeLongitude(String name, String latitude, String longitude) throws CityException {
		
		City city = null;
		
		try {
			
			city = cityDao.findByNameIgnoreCase(name);
			
			if(city == null){
				
				city = getCityFromApi(latitude, longitude);
				
				city = cityDao.save(city);
				
			}
			
		} catch(ProvinceException p){
			log.error(p.getMessage());
			throw new CityException(p.getMessage());
		} catch(CityException c){
			log.error(c.getMessage());
			throw c;
		} catch (PlateAndPicException e) {
			log.error(e.getMessage());
			throw new CityException(e.getMessage());
		}
		
		return city;
		
	}
	
	/**
	 * @param latitude
	 * @param longitude
	 * @return
	 * 
	 * Return a City object calling a API City with the latitude and longitude passed as parameter
	 * @throws PlateAndPicException 
	 */
	private City getCityFromApi(String latitude, String longitude) throws PlateAndPicException{
		
		Api apiCityRequest = buildApiCity(latitude, longitude);
		
		List<Result> results = apiCityRequest.executeGetMethod();
		
		City city = getCityFromResults(results);
		
		return city;
		
	}
	
	/**
	 * @param latitude
	 * @param longitude
	 * @return
	 * 
	 * Build an ApiCityRequest object
	 */
	private Api buildApiCity(String latitude, String longitude) {
		
		Api apiCityRequest = null;
		
		String endpoint = env.getProperty(ConstantsProperties.API_CITY_ENDPOINT);
		
		String apiKey = env.getProperty(ConstantsProperties.API_KEY);
		
		apiCityRequest = new ApiCityRequest(endpoint, latitude, longitude, apiKey);
		
		return apiCityRequest;
		
	}
	
	/**
	 * @param results
	 * @return
	 * 
	 * Build a city object from the first element of a Result list
	 * @throws CityException 
	 * @throws ProvinceException 
	 */
	private City getCityFromResults(List<Result> results) throws CityException, ProvinceException{
		
		City city = null;
		Result result = null;
		String cityName = null;
		String provinceName = null;
		
		if(results != null && results.size() > 0) {
			
			result = results.get(0);
			cityName = result.getCityFromAddressComponent();
			provinceName = result.getProvinceFromAddresComponent();
			
			city = buildCityByProvinceAndName(provinceName, cityName);
			
		}
		
		return city;
	}
	
	/**
	 * @param provinceName
	 * @param cityName
	 * @return
	 * 
	 * Build a city object with the province and name passed as parameter.
	 * Call to the province Factory to find the province object, and throw a
	 * ProvinceException if it does not exist.
	 */
	private City buildCityByProvinceAndName(String provinceName, String cityName){
		
		City city = null;
		Province province = null;
		
		try{
			
			city = new City();
			
			province = provinceFactory.getProvinceByName(provinceName);
			
			city.setProvince(province);
			city.setName(cityName);
			
		} catch (ProvinceException e){
			
			log.error(e.getMessage());
			
		}
		
		return city;
		
	}

}
