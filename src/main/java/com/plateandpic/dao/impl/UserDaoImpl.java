package com.plateandpic.dao.impl;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.plateandpic.constants.MessageConstants;
import com.plateandpic.constants.UserConstants;
import com.plateandpic.dao.UserDaoCustom;
import com.plateandpic.exceptions.PlateAndPicException;
import com.plateandpic.response.FollowersResponse;
import com.plateandpic.utils.ParserUtil;

/**
 * @author gonzalo
 *
 */
@Repository
@Transactional(readOnly = true)
public class UserDaoImpl implements UserDaoCustom {
	
	private static final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);
	
	@PersistenceContext
	private EntityManager entityManager;

	/* (non-Javadoc)
	 * @see com.plateandpic.dao.UserDaoCustom#getFollowersData(java.lang.Long, java.lang.Long)
	 */
	@Override
	public FollowersResponse getFollowersData(Long loggedUserId, Long userId) throws PlateAndPicException {
		
		FollowersResponse followerData = null;
		StringBuilder sql = null;
		Query query = null;
		List<Object[]> queryResult = null;
		
		try {
			
			sql = new StringBuilder();
			
			buildFollowersDataNativeQuery(sql, loggedUserId, userId);
			
			query = entityManager.createNativeQuery(sql.toString());
			
			queryResult = query.getResultList();
			
			followerData = buildFollowerFromQueryResult(queryResult, loggedUserId, userId);
			
			
		} catch (Exception e){
			log.error("Error getting Followers Data for logged user: " + loggedUserId + ", and the user" + userId);
			throw new PlateAndPicException(MessageConstants.GENERAL_ERROR);
		}
		
		
		return followerData;
		
	}
	
	/**
	 * @param sql
	 * @param loggedUserId
	 * @param userId
	 * 
	 * Build the native query for getting the information about the follower
	 */
	/**
	 * @param sql
	 * @param loggedUserId
	 * @param userId
	 */
	private void buildFollowersDataNativeQuery(StringBuilder sql, Long loggedUserId, Long userId){
		
		sql.append("select u.id as userId, followers.fNumber as followersNumber,");
		sql.append("	platePictures.ppCount as ppNumber, isFollowing.isF");
		sql.append(" from ");
		sql.append("	user u");
		sql.append("    left join (");
		sql.append("		select uf.followers_id as uId, count(uf.user_id) as fNumber");
		sql.append("        from user_followers uf");
		sql.append("		where uf.user_id <> uf.followers_id");
		sql.append("        group by uf.followers_id");
		sql.append("    ) as followers");
		sql.append("    on u.id = followers.uId");
		sql.append("    left join (");
		sql.append("		select pp.user_id as ppUserId, count(pp.plate_picture_id) as ppCount");
		sql.append("        from plate_picture pp");
		sql.append("        group by pp.user_id");
		sql.append("    ) as platePictures");
		sql.append("    on u.id = platePictures.ppUserId");
		sql.append("    left join (");
		sql.append("		select followers_id as isF ");
		sql.append("        from user_followers uf");
		sql.append("        where uf.user_id = ").append(loggedUserId).append(" and followers_id = ").append(userId);
		sql.append("    ) as isFollowing");
		sql.append("    on u.id = isFollowing.isF");
		sql.append(" where u.id = ").append(userId);
		sql.append(" 	AND (isF is null OR isF is not null and isF = u.id)");
		
		log.debug("Executing query: " + sql.toString());
		
	}
	
	/**
	 * @param queryResult
	 * @param loggedUserId
	 * @param userId
	 * @return
	 */
	private FollowersResponse buildFollowerFromQueryResult(List<Object[]> queryResult, Long loggedUserId, Long userId){
		
		FollowersResponse fr = new FollowersResponse();
		
		if(queryResult != null && queryResult.size() > 0){
			
			fr = buildFollowerFromObjectResult(queryResult.get(0), loggedUserId, userId);
			
		}
		
		return fr;
		
	}
	
	/**
	 * @param result
	 * @param loggedUserId
	 * @param userId
	 * @return
	 */
	private FollowersResponse buildFollowerFromObjectResult(Object[] result, Long loggedUserId, Long userId){
		
		FollowersResponse response = new FollowersResponse();
		
		response.setUserId(ParserUtil.bigIntegerToLong((BigInteger) result[UserConstants.USER_ID]));
		response.setFollowersNumber(ParserUtil.bigIntegerToInteger((BigInteger) result[UserConstants.FOLLOWERS_NUMBER]));
		response.setPlatePicturesNumber(ParserUtil.bigIntegerToInteger((BigInteger) result[UserConstants.PLATEPICTURES_NUMBER]));
		response.setIsFollowing(ParserUtil.convertNotNullToBoolean(result[UserConstants.IS_FOLLOWING]));
		response.setIsLoggedUser(loggedUserId.equals(userId));
		
		return response;
		
	}

}
