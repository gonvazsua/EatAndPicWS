package com.plateandpic.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.plateandpic.models.City;
import com.plateandpic.models.Restaurant;

/**
 * @author gonzalo
 *
 */
@Transactional
public interface RestaurantDao extends CrudRepository<Restaurant, Long> {
	
	//public List<Restaurant> findByNameAndCity(String name, City city);
	
	/**
	 * @param name
	 * @param city
	 * @return
	 */
	@Query(value = "Select * from restaurant where name like %:name% and city_city_id = :city", nativeQuery = true)
	public List<Restaurant> findByNameAndCityId(
			@Param("name") String name, @Param("city") Long city);
	
	/**
	 * @param apliPlaceId
	 * @return
	 */
	public List<Restaurant> findByApiPlaceId(String apliPlaceId);
	
	/**
	 * @param apliPlaceId
	 * @return
	 * 
	 * Find a restaurant by name with LIKE and IgnoreCase operator order by Name ASC
	 */
	public List<Restaurant> findByNameContainingIgnoreCaseOrderByNameAsc(String name);
	
}
