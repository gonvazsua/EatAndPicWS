package com.eatandpic.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.eatandpic.models.Plate;
import com.eatandpic.models.Restaurant;

@Transactional
public interface PlateDao extends CrudRepository<Plate, Long> {
	
	public List<Plate> findByRestaurant(Restaurant restaurant);

}
