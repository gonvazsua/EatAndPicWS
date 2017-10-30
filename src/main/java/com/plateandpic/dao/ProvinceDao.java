package com.plateandpic.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.plateandpic.models.Province;

@Transactional
public interface ProvinceDao extends CrudRepository<Province, Long> {
	
	
	/**
	 * @param provinceName
	 * @return
	 * 
	 * Get a list of provinces filter by name
	 */
	public List<Province> findByNameContainingIgnoreCaseOrderByNameAsc(String provinceName);

}
