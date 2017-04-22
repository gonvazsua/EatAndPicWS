package com.plateandpic.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.plateandpic.models.City;
import com.plateandpic.models.Restaurant;

@Transactional
public interface RestaurantDao extends CrudRepository<Restaurant, Long> {
	
	public List<Restaurant> findByNameAndCity(String name, City city);
	
}
