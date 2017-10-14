package com.plateandpic.factory;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.plateandpic.dao.PlateDao;
import com.plateandpic.dao.RestaurantDao;
import com.plateandpic.exceptions.PlateException;
import com.plateandpic.models.Plate;
import com.plateandpic.models.Restaurant;
import com.plateandpic.response.PlateResponse;

/**
 * @author gonzalo
 *
 */
@Component
public class PlateFactory {
	
	private static final Logger log = LoggerFactory.getLogger(PlateFactory.class);
	
	@Autowired
	private PlateDao plateDao;
	
	@Autowired
	private RestaurantDao restaurantDao;
	
	/**
	 * @param plate
	 * @return
	 * @throws PlateException
	 */
	public Plate savePlateIfNotExists(Plate plate) throws PlateException{
		
		Restaurant restaurant = null;
		Plate savedPlate = null;
		List<Plate> plates = null;
		
		restaurant = restaurantDao.findOne(plate.getRestaurant().getRestaurantId());
		
		plate.setRestaurant(restaurant);
		
		plates = plateDao.findByRestaurantAndName(restaurant, plate.getName());
		
		if(plates != null && plates.size() > 0){
			
			savedPlate = plates.get(0);
		
		} 
		else{
			
			savedPlate = plateDao.save(plate);
			
		}
		
		if(savedPlate == null){
			throw new PlateException("Plate not saved: " + plate.toString());
		}
		
		return savedPlate;
	}
	
	/**
	 * @param restaurantId
	 * @return
	 * 
	 * Load a plateResponse from the Restaurant passed as parameter
	 */
	public List<PlateResponse> getPlateResponseByRestaurantId(Long restaurantId){
		
		List<PlateResponse> response = null;
		
		List<Plate> plates = plateDao.findByRestaurant_restaurantId(restaurantId);
		
		if(plates != null){
		
			response = buildPlateResponse(plates);
		
		}
		
		return response;
		
	}
	
	/**
	 * @param plates
	 * @return
	 * 
	 * Build a list of PlateResponse from the Plate list passed as parameter
	 */
	private List<PlateResponse> buildPlateResponse(List<Plate> plates){
		
		List<PlateResponse> response = new ArrayList<PlateResponse>();
		PlateResponse plateResponse = null;
		
		for(Plate plate : plates){
			
			plateResponse = new PlateResponse(plate);
			response.add(plateResponse);
			
		}
		
		return response;
		
	}
	
}
