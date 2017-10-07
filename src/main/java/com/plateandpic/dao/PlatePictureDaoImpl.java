package com.plateandpic.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.plateandpic.response.PlatePictureResponse;

/**
 * @author gonzalo
 * 
 * Auxiliar class for creating a custom method to execute the complex sql query with the 
 * last plate pictures
 *
 */
@Repository
@Transactional(readOnly = true)
public class PlatePictureDaoImpl implements PlatePictureDaoCustom {
	
	private static final Logger log = LoggerFactory.getLogger(PlatePictureDaoImpl.class);
	
	@PersistenceContext
	private EntityManager entityManager;

	/* (non-Javadoc)
	 * @see com.plateandpic.dao.PlatePictureDaoCustom#getLastPlatePictures()
	 * 
	 * TODO: Complex SQL Query
	 */
	@Override
	public List<PlatePictureResponse> getLastPlatePictures() {

		List<PlatePictureResponse> response = null;
		StringBuilder query = null;
		
		try {
			
			query = buildPlatePictureQuery();
			
			
		} catch(Exception e){
			
		}
		
		return response;
	}
	
	/**
	 * @return
	 */
	private StringBuilder buildPlatePictureQuery(){
		
		StringBuilder query = new StringBuilder(100);
		
		return query;
		
	}

}
