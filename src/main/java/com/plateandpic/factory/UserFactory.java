package com.plateandpic.factory;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.plateandpic.constants.ConstantsProperties;
import com.plateandpic.dao.UserDao;
import com.plateandpic.exceptions.UserNotValidException;
import com.plateandpic.models.User;
import com.plateandpic.response.UserResponse;
import com.plateandpic.security.JwtTokenUtil;

/**
 * @author gonzalo
 *
 */
@Service
public class UserFactory {
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	/**
	 * @param userFrom
	 * @param userTo
	 */
	public void copyFieldsFromPersonalDataChange(User userFrom, User userTo){
		
		userTo.setUsername(userFrom.getUsername());
		userTo.setFirstname(userFrom.getFirstname());
		userTo.setLastname(userFrom.getLastname());
		
	}
	
	/**
	 * @param userFrom
	 * @param userTo
	 */
	public void copyFieldsFromEmailChange(User userFrom, User userTo){
		
		userTo.setEmail(userFrom.getEmail());
		
	}
	
	/**
	 * @param userId
	 * @return
	 * @throws IOException
	 */
	public UserResponse getUserResponse(Long userId) throws IOException{
		
		UserResponse userResponse = null;
		User user = null;
		
		user = userDao.findOne(userId);
		
		if(user == null){
			throw new UsernameNotFoundException("User not found with id: " + userId);
		}
		
		userResponse = buildUserResponse(user);
		
		return userResponse;
		
	}
	
	/**
	 * @param user
	 * @return
	 * @throws IOException
	 */
	private UserResponse buildUserResponse(User user) throws IOException{
		
		UserResponse userResponse = null;
		String base64UserPicture = "";
		
		userResponse = new UserResponse(user);
		
		if(user.getPicture() != null && !"".equals(user.getPicture())){
			base64UserPicture = FileFactory.getBase64FromProfilePictureName(getProfilePicturePath(), user.getPicture());
			userResponse.setPicture(base64UserPicture);
		}
		
		return userResponse;
		
	}
	
	/**
	 * @return
	 */
	public String getProfilePicturePath(){
		
		return env.getProperty(ConstantsProperties.USER_PROFILE_PICTURE_PATH);
		
	}
	
	/**
	 * @param token
	 * @return
	 * @throws UserNotValidException
	 */
	public User getUserFromToken(String token) throws UserNotValidException{
		
		Long userId = jwtTokenUtil.getUserIdFromToken(token);
		
		User user = userDao.findOne(userId);
		
		if(user == null){
			throw new UserNotValidException("User not found with token: " + token);
		}
		
		return user;
		
	}
	
	/**
	 * @param username
	 * @return
	 * @throws UserNotValidException
	 */
	public User getUserByUsername(String username) throws UserNotValidException{
		
		User user = userDao.findByUsername(username);
		
		if(user == null){
			throw new UserNotValidException("User not found with username: " + username);
		}
		
		return user;
		
	}

}
