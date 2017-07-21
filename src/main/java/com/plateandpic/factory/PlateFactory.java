package com.plateandpic.factory;

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
	
}
