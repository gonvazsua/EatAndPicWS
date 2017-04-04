package com.eatandpic.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.eatandpic.models.City;
import com.eatandpic.models.Restaurant;

@Transactional
public interface RestaurantDao extends CrudRepository<Restaurant, Long> {
	
	public List<Restaurant> findByNameAndCity(String name, City city);
	
}
