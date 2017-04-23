package com.plateandpic.factory;

import java.util.Date;

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

}
