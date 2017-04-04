package com.eatandpic.dao;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.eatandpic.models.City;

@Transactional
public interface CityDao extends CrudRepository<City, Long>{
	
	public City findByName(String name);
	
}
