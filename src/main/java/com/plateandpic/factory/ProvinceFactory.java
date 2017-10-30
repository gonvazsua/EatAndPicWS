package com.plateandpic.factory;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plateandpic.constants.MessageConstants;
import com.plateandpic.dao.ProvinceDao;
import com.plateandpic.exceptions.ProvinceException;
import com.plateandpic.models.Province;

/**
 * @author gonzalo
 *
 */
@Service
public class ProvinceFactory {

	private static final Logger log = LoggerFactory.getLogger(ProvinceFactory.class);
	
	@Autowired
	private ProvinceDao provinceDao;
	
	/**
	 * @param name
	 * @return
	 * @throws ProvinceException
	 * 
	 * Get province by name
	 */
	public Province getProvinceByName(String name) throws ProvinceException{
		
		Province province = null;
		
		List<Province> provinces = provinceDao.findByNameContainingIgnoreCaseOrderByNameAsc(name);
		
		if(provinces != null && !provinces.isEmpty()){
			
			province = provinces.get(0);
			
		} else {
			
			log.error("Not found province with name:" + name);
			throw new ProvinceException(MessageConstants.PROVINCE_NOT_FOUND);
			
		}
		
		return province;
		
	}
	
}
