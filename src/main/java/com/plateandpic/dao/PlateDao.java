package com.plateandpic.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.plateandpic.models.Plate;
import com.plateandpic.models.Restaurant;

@Transactional
public interface PlateDao extends CrudRepository<Plate, Long> {
	
	public List<Plate> findByRestaurant(Restaurant restaurant);
	
	public List<Plate> findByRestaurantAndName(Restaurant restaurant, String name);

}
