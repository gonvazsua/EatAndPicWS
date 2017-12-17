package com.plateandpic.factory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.plateandpic.constants.ConstantsProperties;
import com.plateandpic.constants.MessageConstants;
import com.plateandpic.dao.UserDao;
import com.plateandpic.exceptions.PasswordException;
import com.plateandpic.exceptions.PlateAndPicException;
import com.plateandpic.exceptions.RestaurantException;
import com.plateandpic.exceptions.UserException;
import com.plateandpic.models.Restaurant;
import com.plateandpic.models.Status;
import com.plateandpic.models.User;
import com.plateandpic.response.FollowersResponse;
import com.plateandpic.response.RestaurantRequestResponse;
import com.plateandpic.response.UserResponse;
import com.plateandpic.security.JwtTokenUtil;
import com.plateandpic.utils.UpdatePasswordRequest;

/**
 * @author gonzalo
 *
 */
@Service
public class UserFactory {
	
	private static final Integer ROW_LIMIT = 30;
	private static final String QUERY_SORT = "name";
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private RestaurantFactory restaurantFactory;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	/**
	 * @param userFrom
	 * @param userTo
	 */
	public void copyFieldsFromPersonalDataChange(User userFrom, User userTo){
		
		userTo.setUsername(userFrom.getUsername());
		userTo.setFirstname(userFrom.getFirstname());
		userTo.setLastname(userFrom.getLastname());
		userTo.setTarget(userFrom.getTarget());
		
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
	 * @throws PlateAndPicException 
	 * @throws IOException
	 */
	public UserResponse getUserResponse(Long userId) throws PlateAndPicException {
		
		UserResponse userResponse = null;
		User user = null;
		
		if(userId == null || userId == 0){
			throw new UsernameNotFoundException(MessageConstants.USER_USER_NOT_FOUND);
		}
		
		user = userDao.findOne(userId);
		
		if(user == null){
			throw new UsernameNotFoundException(MessageConstants.USER_USER_NOT_FOUND);
		}
		
		userResponse = buildUserResponse(user);
		
		return userResponse;
		
	}
	
	/**
	 * @param user
	 * @return
	 * @throws IOException
	 * @throws PlateAndPicException 
	 */
	public UserResponse buildUserResponse(User user) throws PlateAndPicException{
		
		UserResponse userResponse = null;
		String base64UserPicture = "";
		
		userResponse = new UserResponse(user);
		
		if(user.getPicture() != null && !"".equals(user.getPicture())){
			base64UserPicture = convertBase64UserProfilePicture(user.getPicture());
			userResponse.setPicture(base64UserPicture);
		}
		
		return userResponse;
		
	}
	
	/**
	 * @param picture
	 * @return
	 * @throws PlateAndPicException
	 * 
	 * Convert picture passed as parameter to base64
	 */
	public String convertBase64UserProfilePicture(String picture) throws PlateAndPicException{
		
		String base64 = "";
		
		if(picture != null && !picture.isEmpty()){
			
			base64 = FileFactory.getBase64FromProfilePictureName(getProfilePicturePath(), picture);
			
		}
		
		return base64;
		
	}
	
	/**
	 * @param user
	 * @return
	 * @throws IOException
	 * @throws PlateAndPicException 
	 * 
	 * Build a List of UserResponse from User list
	 */
	public List<UserResponse> buildUserResponseList(List<User> users) throws PlateAndPicException{
		
		UserResponse userResponse = null;
		List<UserResponse> response = new ArrayList<UserResponse>();
		
		if(users == null || users.isEmpty()){
			return response;
		}
		
		for(User u : users){
			
			userResponse = buildUserResponse(u);
			response.add(userResponse);
			
		}
		
		return response;
		
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
	 * @throws UserException
	 */
	public User getUserFromToken(String token) throws UserException {
		
		Long userId = jwtTokenUtil.getUserIdFromToken(token);
		
		User user = userDao.findOne(userId);
		
		if(user == null){
			throw new UserException(MessageConstants.USER_USER_NOT_FOUND);
		}
		
		return user;
		
	}
	
	/**
	 * @param token
	 * @return
	 */
	public Long getUserIdFromToken(String token){
		
		return jwtTokenUtil.getUserIdFromToken(token);
		
	}
	
	/**
	 * @param username
	 * @return
	 * @throws UserException
	 */
	public User getUserByUsername(String username) throws UserException{
		
		User user = userDao.findByUsername(username);
		
		if(user == null){
			throw new UserException(MessageConstants.USER_USER_NOT_FOUND);
		}
		
		return user;
		
	}
	
	/**
	 * @param email
	 * @return
	 * @throws UserException
	 */
	public User getUserByEmail(String email) throws UserException{
		
		User user = userDao.findByEmail(email);
		
		if(user == null){
			throw new UserException(MessageConstants.USER_USER_NOT_FOUND);
		}
		
		return user;
		
	}
	
	/**
	 * @param oldUsername, newUsername
	 * @return
	 * @throws UserException
	 * 
	 * Check if username has changed and is unique in DB
	 */
	public void checkUsername(String oldUsername, String newUsername) throws UserException{
		
		if(!(oldUsername.equals(newUsername)) && (getUserByUsername(newUsername) != null)){
			
			throw new UserException(MessageConstants.USER_USERNAME_ALREADY_USED);
			
		}
		
	}
	
	/**
	 * @param oldEmail, newEmail
	 * @return
	 * @throws UserException
	 * 
	 * Check if email has changed and is unique in DB
	 */
	public void checkEmail(String oldEmail, String newEmail) throws UserException{
		
		if(!(oldEmail.equals(newEmail)) && (getUserByEmail(newEmail) != null)){
			
			throw new UserException(MessageConstants.USER_USERNAME_ALREADY_USED);
			
		}
		
	}
	
	/**
	 * @param user
	 * 
	 * Save user in DB
	 */
	public void saveUser(User user){
		
		userDao.save(user);
		
	}
	
	/**
	 * @param passwordRequest
	 * @param user
	 * @throws PasswordException
	 */
	public void validateLastPasswordAndUpdate(UpdatePasswordRequest passwordRequest, User user) throws PasswordException{
		
		validateLastPassword(passwordRequest, user);
		
		updateNewPassword(passwordRequest, user);
		
		saveUser(user);
		
	}
	
	/**
	 * @param passwordRequest
	 * @param user
	 * @throws PasswordException
	 */
	private void validateLastPassword(UpdatePasswordRequest passwordRequest, User user) throws PasswordException{
		
		Authentication authentication = null;
		
		try {
			
			authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), 
					passwordRequest.getLastPassword()));
			
		} catch(AuthenticationException e) {
			throw new PasswordException(MessageConstants.PASSWORD_NOT_CORRECT);
		}
		
	}
	
	/**
	 * @param passwordRequest
	 * @param user
	 */
	private void updateNewPassword(UpdatePasswordRequest passwordRequest, User user){
		
		String newPassword = passwordEncoder.encode(passwordRequest.getNewPassword1());
		
		user.setPassword(newPassword);
		
	}
	
	/**
	 * @param keySearch
	 * @param page
	 * @return
	 * 
	 * Find users with name, lastname or username like the keysearch
	 * @throws PlateAndPicException 
	 */
	public List<UserResponse> findUsersByKeySearch(String keySearch, Integer page) throws PlateAndPicException{
		
		List<User> userList = null;
		List<UserResponse> userResponseList = null;
		Pageable pageable = null;
		
		//pageable = new PageRequest(page, ROW_LIMIT, Sort.Direction.DESC, QUERY_SORT);
		userList = userDao.findUserByKey(keySearch.trim());
		
		userResponseList = buildUserResponseList(userList);
		
		return userResponseList;
		
	}
	
	
	/**
	 * @param loggedUserId
	 * @param userId
	 * @return
	 * @throws PlateAndPicException
	 * 
	 * Load the Followers data from the userLogged passed as parameter.
	 * If the userId is null, it means that the infomation we are looking for is the logged user,
	 * so we have to set their value to the same that loggedUserId
	 */
	public FollowersResponse getFollowersData(Long loggedUserId, Long userId) throws PlateAndPicException{
		
		FollowersResponse response = null;
		
		if(userId == null || userId == 0){
			userId = loggedUserId;
		}
		
		response = userDao.getFollowersData(loggedUserId, userId);
		
		return response;
		
	}
	
	/**
	 * @param userId
	 * @param token
	 * @throws UserException
	 * 
	 * Get the logged user and add the user passed as parameter to their followers,
	 * if it does not exist
	 */
	public void followToUser(Long userId, String token) throws UserException{
		
		User user = getUserFromToken(token);
		
		User userToFollow = userDao.findOne(userId);
		
		if(!user.getFollowers().contains(userToFollow)){
			
			user.getFollowers().add(userToFollow);
			userDao.save(user);
			
		}
		
	}
	
	/**
	 * @param userId
	 * @param token
	 * @throws UserException
	 * 
	 * Get the logged user and remove from to their followers, the user passed as parameter,
	 * if it exists
	 */
	public void unfollowToUser(Long userId, String token) throws UserException{
		
		User user = getUserFromToken(token);
		
		User userToUnfollow = userDao.findOne(userId);
		
		if(user.getFollowers().contains(userToUnfollow)){
			
			user.getFollowers().remove(userToUnfollow);
			userDao.save(user);
			
		}
		
	}
	
	/**
	 * @param token
	 * @param restaurant
	 * @throws UserException 
	 * @throws RestaurantException 
	 * 
	 * Get the logged user and the restaurant passed as parameter, and assign it
	 * to the user
	 */
	public void saveUserRestaurant(String token, RestaurantRequestResponse restaurant) throws UserException, RestaurantException{
		
		User user = getUserFromToken(token);
		
		Restaurant restToSave = restaurantFactory.findRestaurantById(restaurant.getRestaurantId());

		User userWithRestaurant = userDao.findByRestaurant(restToSave);
		
		if(userWithRestaurant != null){
			throw new UserException(MessageConstants.USER_RESTAURANT_ALREADY_MANAGED);
		}
		
		user.setRestaurant(restToSave);
		user.setStatus(Status.RESTAURANT_PENDING.getCode());
		
		userDao.save(user);
		
	}

}
