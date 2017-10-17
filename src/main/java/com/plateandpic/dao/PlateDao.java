package com.plateandpic.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.plateandpic.models.Plate;
import com.plateandpic.models.Restaurant;

/**
 * @author gonzalo
 *
 */
@Transactional
public interface PlateDao extends CrudRepository<Plate, Long> {
	
	/**
	 * @param restaurant
	 * @param name
	 * @return
	 */
	public List<Plate> findByRestaurantAndName(Restaurant restaurant, String name);

	/**
	 * @param restaurantId
	 * @return
	 */
	public List<Plate> findByRestaurant_restaurantId(Long restaurantId);
	
	/**
	 * @param name
	 * @param pageable
	 * @return
	 */
	public List<Plate> findByNameContainingIgnoreCaseOrderByNameAsc(String name, Pageable pageable);
	
}
