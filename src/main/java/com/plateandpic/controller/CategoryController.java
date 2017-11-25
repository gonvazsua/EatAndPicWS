package com.plateandpic.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.plateandpic.exceptions.PlateAndPicException;
import com.plateandpic.exceptions.RestaurantException;
import com.plateandpic.exceptions.UserException;
import com.plateandpic.factory.CategoryFactory;
import com.plateandpic.models.Category;

@RestController
@RequestMapping("/category")
public class CategoryController {
	
	private static final Logger log = LoggerFactory.getLogger(CategoryController.class);
	
	@Autowired
	private CategoryFactory categoryFactory;
	
	@Value("${jwt.header}")
	private String tokenHeader;
	
	/**
	 * @param request
	 * @param response
	 * @param name
	 * @return
	 * 
	 * Return list of Categories filtered by name
	 */
	@RequestMapping(value = "/findByName", method = RequestMethod.GET)
	@ResponseBody
	public List<Category> findByName(HttpServletRequest request, HttpServletResponse response,
			@RequestParam String name){
		
		List<Category> categories = new ArrayList<Category>();
		
		if(name != null && !name.isEmpty()){
			
			categories = categoryFactory.findCategoriesByName(name);
			
		} else {
			
			log.error("Empty name or null");
			
		}
		
		return categories;
		
	}
	
	/**
	 * @param request
	 * @param response
	 * @return
	 * @throws PlateAndPicException 
	 */
	@RequestMapping(value = "/getByUserRestaurant", method = RequestMethod.GET)
	@ResponseBody
	public List<Category> getByUserRestaurant(HttpServletRequest request, HttpServletResponse response) throws PlateAndPicException {
		
		String token = null;
		List<Category> categories = null;
		
		try {
			
			categories = new ArrayList<Category>();
			
			token = request.getHeader(tokenHeader);
			
			categories = categoryFactory.getUserRestaurantCategories(token);
			
		} catch (UserException e) {
			log.error("User not found!");
			throw new PlateAndPicException(e.getMessage());
		} catch (RestaurantException e) {
			log.error("Restaurant not found!");
			throw new PlateAndPicException(e.getMessage());
		}
		
		return categories;
		
	}
	
	/**
	 * @param request
	 * @param response
	 * @return
	 * @throws PlateAndPicException 
	 */
	@RequestMapping(value = "/getNoUserRestaurant", method = RequestMethod.GET)
	@ResponseBody
	public List<Category> getNoUserRestaurant(HttpServletRequest request, HttpServletResponse response) throws PlateAndPicException {
		
		String token = null;
		List<Category> categories = null;
		
		try {
			
			categories = new ArrayList<Category>();
			
			token = request.getHeader(tokenHeader);
			
			categories = categoryFactory.getNoUserRestaurantCategories(token);
			
		} catch (UserException e) {
			log.error("User not found!");
			throw new PlateAndPicException(e.getMessage());
		} catch (RestaurantException e) {
			log.error("Restaurant not found!");
			throw new PlateAndPicException(e.getMessage());
		}
		
		return categories;
		
	}
	
	/**
	 * @param request
	 * @param response
	 * @return
	 * @throws PlateAndPicException 
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Category addCategoryToUserRestaurant(@RequestBody Category category, 
			HttpServletRequest request, HttpServletResponse response) throws PlateAndPicException {
		
		String token = null;
		List<Category> categories = null;
		
		try {
			
			categories = new ArrayList<Category>();
			
			token = request.getHeader(tokenHeader);
			
			categoryFactory.addCategoryToUserRestaurant(token, category);
			
		} catch (UserException e) {
			log.error("User not found!");
			throw new PlateAndPicException(e.getMessage());
		} catch (RestaurantException e) {
			log.error("Restaurant not found!");
			throw new PlateAndPicException(e.getMessage());
		}
		
		return category;
		
	}
	
	/**
	 * @param request
	 * @param response
	 * @return
	 * @throws PlateAndPicException 
	 */
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	@ResponseBody
	public Category removeCategoryFromUserRestaurant(@RequestBody Category category, 
			HttpServletRequest request, HttpServletResponse response) throws PlateAndPicException {
		
		String token = null;
		List<Category> categories = null;
		
		try {
			
			categories = new ArrayList<Category>();
			
			token = request.getHeader(tokenHeader);
			
			category = categoryFactory.removeCategoryFromUserRestaurant(token, category);
			
		} catch (UserException e) {
			log.error("User not found!");
			throw new PlateAndPicException(e.getMessage());
		} catch (RestaurantException e) {
			log.error("Restaurant not found!");
			throw new PlateAndPicException(e.getMessage());
		}
		
		return category;
		
	}
	
	

}
