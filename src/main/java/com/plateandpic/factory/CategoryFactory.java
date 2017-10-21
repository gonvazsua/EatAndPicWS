package com.plateandpic.factory;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plateandpic.dao.CategoryDao;
import com.plateandpic.models.Category;

/**
 * @author gonzalo
 *
 */
@Service
public class CategoryFactory {
	
	private static final Logger log = LoggerFactory.getLogger(CategoryFactory.class);
	
	@Autowired
	private CategoryDao categoryDao;
	
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

}
