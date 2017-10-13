package com.plateandpic.dao;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.plateandpic.constants.PlatePictureConstants;
import com.plateandpic.constants.PlatePictureQueryType;
import com.plateandpic.response.PlatePictureResponse;
import com.plateandpic.utils.DateUtils;
import com.plateandpic.utils.ParserUtil;

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
	 * Get last platePictures loaded by username
	 */
	@Override
	public List<PlatePictureResponse> getLastPlatePicturesByUsername(String username, Integer from, Integer to) {

		List<PlatePictureResponse> response = null;
		StringBuilder SQLQuery = null;
		List<Object[]> queryResult = null;
		Query query = null;
		
		try {
			
			SQLQuery = buildPlatePictureQuery(username, PlatePictureQueryType.TYPE_PROFILE, from, to);
			
			query = entityManager.createNativeQuery(SQLQuery.toString());
			
			queryResult = query.getResultList();
			
			response = buildListOfPlatePictureResponse(queryResult);
			
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
		return response;
	}
	
	/* (non-Javadoc)
	 * @see com.plateandpic.dao.PlatePictureDaoCustom#getLastPlatePictures()
	 *	
	 * Get last platePicture from followed users
	 */
	@Override
	public List<PlatePictureResponse> getLastFollowersPlatePicturesByUserId(Long userId, Integer from, Integer to){
		
		List<PlatePictureResponse> response = null;
		StringBuilder sqlQuery = null;
		List<Object[]> queryResult = null;
		Query query = null;
		
		try {
			
			sqlQuery = buildPlatePictureQuery(userId.toString(), PlatePictureQueryType.TYPE_FOLLOWERS, from, to);
			
			query = entityManager.createNativeQuery(sqlQuery.toString());
			
			queryResult = query.getResultList();
			
			response = buildListOfPlatePictureResponse(queryResult);
			
		} catch(Exception e){
			
		}
		
		return response;
		
	}
	
	/**
	 * @return
	 * 
	 * Build platePicture query and build the where clause depending of the type of query:
	 * 	1. FOLLOWERS: Return last platePictures of the followed users by the logged user
	 * 	2. PROFILE: Return last platePictures of the logged user
	 */
	private StringBuilder buildPlatePictureQuery(String data, PlatePictureQueryType queryType, Integer from, Integer to){
		
		StringBuilder query = new StringBuilder(100);
		
		query.append("select u.id as userId, u.username as userName, u.picture as userImage,");
		query.append("	pp.plate_picture_id as platePictureId, pp.title as title, pp.picture as picture, pp.registered_on as registeredOn,");
		query.append("	r.restaurant_id as restaurantId, r.name as restaurantName,");
		query.append("	c.city_id as cityId, c.name as cityName,");
		query.append("	pl.plate_id as plateId, pl.name as plateName, pl.active as plateActive,");
		query.append("	likes.likesNumber, comments.commentsNumber, likeToUser.likeUserId");
		query.append(" from user u");
		query.append("		LEFT JOIN plate_picture pp ON pp.user_id = u.id");
		query.append("		LEFT JOIN plate pl ON pp.plate_plate_id = pl.plate_id");
		query.append("		LEFT JOIN restaurant r ON pl.restaurant_restaurant_id = r.restaurant_id");
		query.append("		LEFT JOIN city c ON c.city_id = r.city_city_id");
		query.append("		LEFT JOIN ");
		query.append("		(");
		query.append("			select ul.plate_picture_plate_picture_id as likesPpId, count(ul.likes_id) as likesNumber");
		query.append("			from plate_picture_likes ul");
		query.append("			group by ul.plate_picture_plate_picture_id");
		query.append("		) as likes");
		query.append("		ON likes.likesPpId = pp.plate_picture_id");
		query.append("		LEFT JOIN ");
		query.append("		(");
		query.append("			select cm.plate_picture_plate_picture_id as commentsPpId, count(cm.comment_id) as commentsNumber");
		query.append("			from comment cm");
		query.append("			group by cm.plate_picture_plate_picture_id");
		query.append("		) as comments");
		query.append("		ON comments.commentsPpId = pp.plate_picture_id");
		query.append("		LEFT JOIN");
		query.append("		(");
		query.append("			SELECT ppl.likes_id as likeUserId, ppl.plate_picture_plate_picture_id as likePpId FROM plate_picture_likes ppl");
		query.append("		)");
		query.append("		as likeToUser ON (likeToUser.likeUserId = u.id AND likeToUser.likePpId = pp.plate_picture_id)");
		query.append( getWhereClauseByQueryType(data, queryType) );
		query.append(" order by pp.registered_on desc ");
		query.append(" LIMIT ").append(from).append(",").append(to);
		
		return query;
		
	}
	
	
	/**
	 * @param userId
	 * @param queryType
	 * @return
	 * 
	 * Return the correct where clause of the query: Profile or Followers type
	 */
	private String getWhereClauseByQueryType(String data, PlatePictureQueryType queryType){
		
		StringBuilder whereClause = new StringBuilder(100);
		
		if(queryType.equals(PlatePictureQueryType.TYPE_PROFILE)){
			
			whereClause.append("where u.username = '").append(data).append("'");
			
		} else if(queryType.equals(PlatePictureQueryType.TYPE_FOLLOWERS)){
			
			whereClause.append("where u.id in (");
			whereClause.append("	select followers_id from user_followers where user_id = ").append(data);
			whereClause.append(")");
			
		}
		
		return whereClause.toString();
		
	}
	
	
	
	/**
	 * @param register
	 * @return
	 * 
	 * Build a PlatePictureResponse from a row of database result
	 */
	private PlatePictureResponse buildPlatePictureResponse(Object[] register){
		
		PlatePictureResponse response = new PlatePictureResponse();
		
		response.setUserId(ParserUtil.bigIntegerToLong((BigInteger) register[PlatePictureConstants.USER_ID]));
		response.setUsername((String) register[PlatePictureConstants.USERNAME]);
		response.setUserImage((String) register[PlatePictureConstants.USER_PICTURE]);
		response.setPlatePictureId(ParserUtil.bigIntegerToLong((BigInteger) register[PlatePictureConstants.PLATE_PICTURE_ID]));
		response.setTitle((String) register[PlatePictureConstants.PLATE_PICTURE_TITLE]);
		response.setPicture((String) register[PlatePictureConstants.PLATE_PICTURE_PICTURE]);
		response.setRegisteredOn(DateUtils.timeStampToString((Timestamp) register[PlatePictureConstants.PLATE_PICTURE_REGIST_ON]));
		response.setRestaurantId(ParserUtil.bigIntegerToLong((BigInteger) register[PlatePictureConstants.RESTAURANT_ID]));
		response.setRestaurantName((String) register[PlatePictureConstants.RESTAURANT_NAME]);
		response.setCityId(ParserUtil.bigIntegerToLong((BigInteger) register[PlatePictureConstants.CITY_ID]));
		response.setCityName((String) register[PlatePictureConstants.CITY_NAME]);
		response.setPlateId(ParserUtil.bigIntegerToLong((BigInteger) register[PlatePictureConstants.PLATE_ID]));
		response.setPlateName((String) register[PlatePictureConstants.PLATE_NAME]);
		response.setPlateActive((Boolean) register[PlatePictureConstants.PLATE_ACTIVE]);
		response.setLikesNumber(ParserUtil.bigIntegerToInteger((BigInteger) register[PlatePictureConstants.LIKES_NUMBER]));
		response.setCommentsNumber(ParserUtil.bigIntegerToInteger((BigInteger) register[PlatePictureConstants.COMMENTS_NUMBER]));
		response.setLikeToUser(ParserUtil.convertNotNullToBoolean(register[PlatePictureConstants.LIKE_TO_USER]));
		
		return response;
		
	}
	
	private List<PlatePictureResponse> buildListOfPlatePictureResponse(List<Object[]> results){
		
		List<PlatePictureResponse> response = new ArrayList<PlatePictureResponse>();
		PlatePictureResponse ppr = null;
		
		if(results == null || results.isEmpty()){
			return response;
		}
		
		Iterator<Object[]> objectIterator = results.iterator();
		
		while(objectIterator.hasNext()){
			
			ppr = buildPlatePictureResponse(objectIterator.next());
			response.add(ppr);
			
		}
		
		return response;
		
	}
}
