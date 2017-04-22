package com.plateandpic.dao;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.plateandpic.models.City;

@Transactional
public interface CityDao extends CrudRepository<City, Long>{
	
	public City findByName(String name);
	
}
