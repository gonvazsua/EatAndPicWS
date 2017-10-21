package com.plateandpic.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.plateandpic.models.Category;

/**
 * @author gonzalo
 *
 */
@Transactional
public interface CategoryDao extends CrudRepository<Category,Long> {
	
	/**
	 * @param name
	 * @return
	 * 
	 * Query: get Categories by name, ignoring capital letters order by name ascedent
	 */
	public List<Category> findByCategoryNameContainingIgnoreCaseOrderByCategoryNameAsc(String name);

}
