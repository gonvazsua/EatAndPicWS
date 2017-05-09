package com.plateandpic.factory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plateandpic.dao.RestaurantDao;
import com.plateandpic.models.Restaurant;

@Service
public class RestaurantFactory {
	
	@Autowired
	private RestaurantDao restaurantDao;
	
	public Restaurant buildAndSave(Restaurant restaurant){
		
		restaurant.setRegisteredOn(new Date());
		
		restaurant = restaurantDao.save(restaurant);
		
		return restaurant;
		
	}
	
	
	public Restaurant findByApiPlaceId(Restaurant restaurant){
		
		Restaurant savedRestaurant = null;
		List<Restaurant> restaurants = restaurantDao.findByApiPlaceId(restaurant.getApiPlaceId());
		
		if(restaurants != null && restaurants.size() > 0){
			
			savedRestaurant = restaurants.get(0);
			
		}
		
		return savedRestaurant;
		
	}

}
