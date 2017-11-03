package com.plateandpic.factory;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.plateandpic.constants.MessageConstants;
import com.plateandpic.dao.PlateDao;
import com.plateandpic.dao.RestaurantDao;
import com.plateandpic.exceptions.PlateAndPicException;
import com.plateandpic.exceptions.PlateException;
import com.plateandpic.models.Plate;
import com.plateandpic.models.Restaurant;
import com.plateandpic.response.PlateResponse;
import com.plateandpic.validator.PlateValidator;

/**
 * @author gonzalo
 *
 */
@Component
public class PlateFactory {
	
	private static final Logger log = LoggerFactory.getLogger(PlateFactory.class);
	
	private static final Integer ROW_LIMIT = 30;
	private static final String QUERY_SORT = "name";
	
	@Autowired
	private PlateDao plateDao;
	
	@Autowired
	private RestaurantDao restaurantDao;
	
	/**
	 * @param plateId
	 * @return
	 * @throws PlateException
	 * 
	 * Find the plate with the id passed as parameter
	 */
	public Plate findById(Long plateId) throws PlateException{
		
		Plate plate = plateDao.findOne(plateId);
		
		if(plate == null){
			log.error("Plate not found with ID: " + plateId);
			throw new PlateException(MessageConstants.PLATE_NOT_FOUND);
		}
		
		return plate;
	}
	
	/**
	 * @param restaurantId
	 * @return
	 * 
	 * Load a plateResponse from the Restaurant passed as parameter
	 */
	public List<PlateResponse> getPlateResponseByRestaurantId(Long restaurantId){
		
		List<PlateResponse> response = null;
		
		List<Plate> plates = plateDao.findByRestaurant_restaurantId(restaurantId);
		
		if(plates != null){
		
			response = buildPlateResponse(plates);
		
		}
		
		return response;
		
	}
	
	/**
	 * @param name
	 * @param page
	 * @return
	 * 
	 * Load a plateResponse from the plate name passed as parameter
	 */
	public List<PlateResponse> findPlatesByName(String name, Integer page){
		
		List<PlateResponse> response = null;
		Pageable pageable = null;
		
		pageable = new PageRequest(page, ROW_LIMIT, Sort.Direction.DESC, QUERY_SORT);
		List<Plate> plates = plateDao.findByNameContainingIgnoreCaseOrderByNameAsc(name, pageable);
		
		if(plates != null){
		
			response = buildPlateResponse(plates);
		
		}
		
		return response;
		
	}
	
	/**
	 * @param plates
	 * @return
	 * 
	 * Build a list of PlateResponse from the Plate list passed as parameter
	 */
	private List<PlateResponse> buildPlateResponse(List<Plate> plates){
		
		List<PlateResponse> response = new ArrayList<PlateResponse>();
		PlateResponse plateResponse = null;
		
		for(Plate plate : plates){
			
			plateResponse = new PlateResponse(plate);
			response.add(plateResponse);
			
		}
		
		return response;
		
	}
	
	/**
	 * @param request
	 * @throws PlateAndPicException
	 * 
	 * Validate a PlateResponse object with a PlateValidator
	 */
	private void validatePlateResponse(PlateResponse request) throws PlateAndPicException{
		
		PlateValidator plateValidator = new PlateValidator(request);
		
		plateValidator.validate();
		
	}
	
	/**
	 * @param request
	 * @return
	 * @throws PlateAndPicException
	 * 
	 * Validate, save and return a PlateResponse object
	 */
	public PlateResponse validateAndSave(PlateResponse request) throws PlateAndPicException {
		
		Plate plate = null;
		PlateResponse response = null;
		
		validatePlateResponse(request);
		
		plate = buildPlateFromPlateResponse(request);
		
		plate = plateDao.save(plate);
		
		response = new PlateResponse(plate);
		
		return response;
		
	}
	
	/**
	 * @param request
	 * @return
	 * @throws PlateException
	 * 
	 * Generate a Plate object from PlateResponse one
	 */
	private Plate buildPlateFromPlateResponse(PlateResponse request) throws PlateException {
		
		Plate plate = new Plate();
		
		Restaurant restaurant = restaurantDao.findOne(request.getRestaurantId());
		
		if(restaurant == null){
			log.error("Restaurant not found while building Plate: " + request.getRestaurantId());
			throw new PlateException(MessageConstants.PLATE_RESTAURANT_NOT_NULL);
		}
		
		plate.setName(request.getPlateName());
		plate.setRestaurant(restaurant);
		//TODO: Set platetype
		plate.setActive(true);
		
		return plate;
		
	}
	
}
