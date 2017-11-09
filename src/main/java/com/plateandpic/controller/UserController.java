package com.plateandpic.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.plateandpic.constants.MessageConstants;
import com.plateandpic.dao.UserDao;
import com.plateandpic.exceptions.PasswordException;
import com.plateandpic.exceptions.PlateAndPicException;
import com.plateandpic.exceptions.UserException;
import com.plateandpic.factory.FileFactory;
import com.plateandpic.factory.UserFactory;
import com.plateandpic.models.User;
import com.plateandpic.response.FollowersResponse;
import com.plateandpic.response.UserResponse;
import com.plateandpic.security.JwtTokenUtil;
import com.plateandpic.utils.UpdatePasswordRequest;
import com.plateandpic.validator.PasswordValidator;
import com.plateandpic.validator.UserValidator;

@RestController
@RequestMapping("/user")
public class UserController {

	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserFactory userFactory;

	@Autowired
	private UserDao userDao;

	@Value("${jwt.header}")
	private String tokenHeader;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private Environment env;

	/**
	 * POST /updatePersonalData --> Update the passed data User
	 * 
	 * @throws IOException
	 * @throws PlateAndPicException
	 */
	@RequestMapping(value = "/updatePersonalData", method = RequestMethod.POST)
	@ResponseBody
	public UserResponse updateUser(@RequestBody User user, HttpServletRequest request, HttpServletResponse response)
			throws IOException, PlateAndPicException {

		User userBBDD = null, userCheckUsername = null;
		UserValidator userValidator = null;
		UserResponse userResponse = null;

		userValidator = new UserValidator(user);

		userValidator.validateUserForPersonalDataChange();

		userBBDD = userFactory.getUserFromToken(request.getHeader(tokenHeader));

		userFactory.checkUsername(userBBDD.getUsername(), user.getUsername());

		userFactory.checkEmail(userBBDD.getEmail(), user.getEmail());

		userFactory.copyFieldsFromPersonalDataChange(user, userBBDD);

		userFactory.saveUser(userBBDD);

		userResponse = userFactory.buildUserResponse(userBBDD);

		return userResponse;

	}

	/**
	 * POST /updatePassword --> Update the user password
	 * 
	 * @throws PasswordException
	 * @throws UserException
	 */
	@RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
	@ResponseBody
	public User updatePassword(@RequestBody UpdatePasswordRequest passwordRequest, HttpServletRequest request,
			HttpServletResponse response) throws PasswordException, UserException {

		User userBBDD = userFactory.getUserFromToken(request.getHeader(tokenHeader));

		PasswordValidator passwordValidator = new PasswordValidator(passwordRequest);

		passwordValidator.validate();

		userFactory.validateLastPasswordAndUpdate(passwordRequest, userBBDD);

		return userBBDD;

	}

	/**
	 * GET /getAuthenticatedUser
	 * 
	 * @throws PlateAndPicException
	 */
	@RequestMapping(value = "/getAuthenticatedUser", method = RequestMethod.GET)
	public UserResponse getAuthenticatedUser(HttpServletRequest request, HttpServletResponse response)
			throws PlateAndPicException {

		UserResponse userResponse = null;
		String token = "";
		Long userId = null;

		token = request.getHeader(tokenHeader);
		userId = jwtTokenUtil.getUserIdFromToken(token);
		userResponse = userFactory.getUserResponse(userId);

		return userResponse;

	}

	/**
	 * POST /updateProfilePicture --> Update user profile picture
	 * 
	 * @throws PlateAndPicException
	 */
	@RequestMapping(value = "/updateProfilePicture", method = RequestMethod.POST, produces = MediaType.IMAGE_JPEG_VALUE)
	public String updateProfilePicture(@RequestBody MultipartFile image, HttpServletRequest request,
			HttpServletResponse response) throws PlateAndPicException {

		String token = "";
		Long userId = null;
		User userBBDD = null;
		String newFileName = null;
		String base64Img = null;

		try {

			token = request.getHeader(tokenHeader);
			userId = jwtTokenUtil.getUserIdFromToken(token);

			userBBDD = userDao.findOne(userId);

			if (userBBDD != null && image != null) {

				newFileName = FileFactory.uploadProfilePicture(image, userId);

				userBBDD.setPicture(newFileName);

				userDao.save(userBBDD);

				base64Img = FileFactory.getBase64FromProfilePictureName(env.getProperty("userProfilePicturesPath"),
						newFileName);

			}

		} catch (IOException ex) {
			log.error(ex.getMessage());
			throw new PlateAndPicException(MessageConstants.USER_PICTURE_NOT_SAVED);
		}

		return base64Img;
	}

	/**
	 * GET /getUserById --> Load User by the id
	 * 
	 * @throws UserException
	 * @throws IOException
	 * @throws PlateAndPicException
	 */
	@RequestMapping(value = "/getUserById", method = RequestMethod.GET)
	@ResponseBody
	public UserResponse getUserById(HttpServletRequest request, HttpServletResponse response, @RequestParam Long userId)
			throws IOException, PlateAndPicException {

		UserResponse userResponse = userFactory.getUserResponse(userId);

		return userResponse;

	}

	/**
	 * GET /findByKey --> Find users with name, lastname or username like the
	 * keysearch
	 * 
	 * @throws UserException
	 * @throws IOException
	 * @throws PlateAndPicException
	 */
	@RequestMapping(value = "/findByKey", method = RequestMethod.GET)
	@ResponseBody
	public List<UserResponse> findByKey(HttpServletRequest request, HttpServletResponse response,
			@RequestParam String keySearch, @RequestParam Integer page) throws PlateAndPicException {

		List<UserResponse> users = new ArrayList<UserResponse>();

		if (keySearch != null && !keySearch.isEmpty() && page != null && page >= 0) {

			users = userFactory.findUsersByKeySearch(keySearch, page);

		}

		return users;

	}

	/**
	 * GET /followersData --> Return the object FollowersResponse from the logged user
	 * 
	 * @throws PlateAndPicException
	 */
	@RequestMapping(value = "/followersData", method = RequestMethod.GET)
	@ResponseBody
	public FollowersResponse followersData(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="userId", required=false) Long userId) throws PlateAndPicException {

		FollowersResponse followersRes = null;
		Long loggedUserId = null;
		String token = null;
		
		token = request.getHeader(tokenHeader);
		
		loggedUserId = userFactory.getUserIdFromToken(token);
		
		if(loggedUserId == null){
			log.error("Parameters not valid!! loggedUserId:" + loggedUserId);
			throw new PlateAndPicException(MessageConstants.USER_USER_NOT_FOUND);
		}
		
		followersRes = userFactory.getFollowersData(loggedUserId, userId);

		return followersRes;

	}
	
	/**
	 * POST /followToUser --> Follow from the loggedUser to the user passed as parameter
	 * 
	 * @throws PlateAndPicException
	 */
	@RequestMapping(value = "/followToUser", method = RequestMethod.POST)
	@ResponseBody
	public void followToUser(HttpServletRequest request, HttpServletResponse response,
			@RequestBody FollowersResponse followResponse) throws PlateAndPicException {

		String token = null;
		
		if(followResponse == null || followResponse.getUserId() == null){
			log.error("Parameter null in followToUser. userId = " + followResponse.getUserId());
			throw new PlateAndPicException(MessageConstants.USER_USER_NOT_FOUND);
		}
		
		token = request.getHeader(tokenHeader);
		
		this.userFactory.followToUser(followResponse.getUserId(), token);

	}
	
	/**
	 * POST /unfollowToUser --> Unfollow the user passed as parameter from the logged user
	 * 
	 * @throws PlateAndPicException
	 */
	@RequestMapping(value = "/unfollowToUser", method = RequestMethod.POST)
	@ResponseBody
	public void unfollowToUser(HttpServletRequest request, HttpServletResponse response,
			@RequestBody FollowersResponse followResponse) throws PlateAndPicException {

		String token = null;
		
		if(followResponse == null || followResponse.getUserId() == null){
			log.error("Parameter null in unfollowToUser. userId = " + followResponse.getUserId());
			throw new PlateAndPicException(MessageConstants.USER_USER_NOT_FOUND);
		}
		
		token = request.getHeader(tokenHeader);
		
		this.userFactory.unfollowToUser(followResponse.getUserId(), token);

	}
	
	

}
