package com.plateandpic.factory;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plateandpic.dao.CityDao;
import com.plateandpic.models.City;
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

}
