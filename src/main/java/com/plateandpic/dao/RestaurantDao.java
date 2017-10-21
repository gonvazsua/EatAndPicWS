package com.plateandpic.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.plateandpic.models.Category;
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
	
	
	/**
	 * @param name
	 * @param cityName
	 * @return
	 */
	public List<Restaurant> findByNameContainingIgnoreCaseAndCity_name(String name, String cityName);
	
	
	/**
	 * @param cityId
	 * @param categoryId
	 * @return
	 * 
	 * Search restaurants filtered by city and category
	 */
//	@Query(value = "SELECT r FROM Restaurant r WHERE r.city = :city AND r.categories IN (:category)")
//	public List<Restaurant> findByCityAndCategory(@Param("city") City city, @Param("category") Category category);
	
	public List<Restaurant> findAllByCityAndCategories(City city, Category category);
	
}
