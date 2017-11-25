package com.plateandpic.factory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plateandpic.dao.CategoryDao;
import com.plateandpic.exceptions.RestaurantException;
import com.plateandpic.exceptions.UserException;
import com.plateandpic.models.Category;
import com.plateandpic.models.Restaurant;

/**
 * @author gonzalo
 *
 */
@Service
public class CategoryFactory {
	
	private static final Logger log = LoggerFactory.getLogger(CategoryFactory.class);
	
	@Autowired
	private CategoryDao categoryDao;
	
	@Autowired
	private RestaurantFactory restaurantFactory;
	
	/**
	 * @param name
	 * @return
	 * 
	 * Return a list of categories that contains the name passed as parameter
	 */
	public List<Category> findCategoriesByName(String name){
		
		List<Category> categories = null;
		
		categories = categoryDao.findByCategoryNameContainingIgnoreCaseOrderByCategoryNameAsc(name);
		
		if(categories == null){
			
			log.debug("Not found categories with name: " + name);
			categories = new ArrayList<Category>();
			
		}
		
		return categories;
		
	}
	
	
	/**
	 * @return
	 * 
	 * Get all categories from user restaurant
	 * @throws UserException 
	 * @throws RestaurantException 
	 */
	public List<Category> getUserRestaurantCategories(String token) throws RestaurantException, UserException {
		
		List<Category> categories = new ArrayList<Category>();
		
		Restaurant restaurant = restaurantFactory.getRestaurantFromUserToken(token);
		
		categories.addAll(restaurant.getCategories());
		
		return categories;
		
	}
	
	/**
	 * @return
	 * 
	 * Get the list of all categories
	 */
	public List<Category> getAll() {
		
		List<Category> categories = new ArrayList<Category>();
		
		Iterable<Category> itbCat = categoryDao.findAll();
		Iterator<Category> itCategory = itbCat.iterator();
		
		while(itCategory.hasNext()){
			categories.add(itCategory.next());
		}
		
		return categories;
		
	}
	
	/**
	 * @return
	 * 
	 * Get all categories from user restaurant
	 * @throws UserException 
	 * @throws RestaurantException 
	 */
	public List<Category> getNoUserRestaurantCategories(String token) throws RestaurantException, UserException {
		
		List<Category> categories = new ArrayList<Category>();
		Set<Category> restaurantCategories = null;
		
		categories = getAll();
		
		Restaurant restaurant = restaurantFactory.getRestaurantFromUserToken(token);
		
		restaurantCategories = (Set<Category>) restaurant.getCategories();
		
		categories.removeAll(restaurant.getCategories());
		
		return categories;
		
	}
	
	/**
	 * @param token
	 * @param category
	 * 
	 * Find the category id and add to user restaurant
	 * @throws UserException 
	 * @throws RestaurantException 
	 */
	public Category addCategoryToUserRestaurant(String token, Category category) throws RestaurantException, UserException {
		
		Restaurant restaurant = restaurantFactory.getRestaurantFromUserToken(token);
		
		restaurant.getCategories().add(category);
		
		restaurantFactory.saveOne(restaurant);
		
		return category;
		
	}
	
	/**
	 * @param token
	 * @param category
	 * 
	 * Find the category id and remove of user restaurant
	 * @throws UserException 
	 * @throws RestaurantException 
	 */
	public Category removeCategoryFromUserRestaurant(String token, Category category) throws RestaurantException, UserException {
		
		Restaurant restaurant = restaurantFactory.getRestaurantFromUserToken(token);
		
		category = categoryDao.findOne(category.getCategoryId());
		
		restaurant.getCategories().remove(category);
		
		restaurantFactory.saveOne(restaurant);
		
		return category;
		
	}

}
