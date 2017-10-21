package com.plateandpic.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.plateandpic.factory.CategoryFactory;
import com.plateandpic.models.Category;

@RestController
@RequestMapping("/category")
public class CategoryController {
	
	private static final Logger log = LoggerFactory.getLogger(CategoryController.class);
	
	@Autowired
	private CategoryFactory categoryFactory;
	
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

}
