package com.plateandpic.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.plateandpic.models.City;

/**
 * @author gonzalo
 *
 */
@Transactional
public interface CityDao extends CrudRepository<City, Long>{
	
	/**
	 * @param name
	 * @return
	 */
	public City findByNameIgnoreCase(String name);
	
	/**
	 * @param name
	 * @return
	 */
	public List<City> findByNameContainingIgnoreCaseOrderByNameAsc(String name);
	
}
