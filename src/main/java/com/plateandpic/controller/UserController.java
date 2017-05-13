package com.plateandpic.controller;

import java.io.IOException;
import java.util.Date;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.plateandpic.dao.UserDao;
import com.plateandpic.factory.FileFactory;
import com.plateandpic.factory.UserFactory;
import com.plateandpic.models.User;
import com.plateandpic.response.UserResponse;
import com.plateandpic.security.JwtTokenUtil;
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
	  private UserDetailsService userDetailsService;
	  
	  @Autowired
	  private PasswordEncoder passwordEncoder;
	  
	  @Autowired
	  private Environment env;
	  
	
  
	  /**
	   * POST /updatePersonalData  --> Update the passed data User
	   */
	  @RequestMapping(value = "/updatePersonalData", method = RequestMethod.POST)
	  @ResponseBody
	  public User updateUser(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) {
		  
		  String token = "";
	      Long userId = null;
		  User userBBDD = null, userCheckUsername = null;
	      UserValidator userValidator = null;
		  
		  try {
			  
			  userValidator = new UserValidator(user);
			  token = request.getHeader(tokenHeader);
			  userId = jwtTokenUtil.getUserIdFromToken(token);
			  
			  userBBDD = userDao.findOne(userId);
			  
			  //Check if exists another user with same Username
			  if(!userBBDD.getUsername().equals(user.getUsername())){
				  
				  userCheckUsername = userDao.findByUsername(user.getUsername());
				  
			  }
			  
			  if(userCheckUsername == null && userBBDD != null && userId != null){
				  
				  userValidator.validateUserForPersonalDataChange();
				  userFactory.copyFieldsFromPersonalDataChange(user, userBBDD);

				  userDao.save(userBBDD);
				  
			  }
			  else{
				  throw new Exception("Username already exists");
			  }
			  
		  }
		  catch (Exception ex) {
			  log.error(ex.getMessage());
			  response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			  return null;
		  }
		  
		  return user;
	  }
	  
	  
	  /**
	   * POST /updateEmail  --> Update the user email
	   * email.
	   */
	  @RequestMapping(value = "/updateEmail", method = RequestMethod.POST)
	  @ResponseBody
	  public User updateUserEmail(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) {
		  
		  String token = "";
	      Long userId = null;
		  User userBBDD = null, userCheckEmail = null;
	      UserValidator userValidator = null;
		  
		  try {
			  
			  userValidator = new UserValidator(user);
			  token = request.getHeader(tokenHeader);
			  userId = jwtTokenUtil.getUserIdFromToken(token);
			  
			  userBBDD = userDao.findOne(userId);
			  
			  //Check if exists another user with same Email
			  if(!userBBDD.getEmail().equals(user.getEmail())){
				  
				  userCheckEmail = userDao.findByEmail(user.getEmail());
				  
			  }
			  
			  if(userCheckEmail == null && userBBDD != null && userId != null){
				  
				  userValidator.validateEmail();
				  userFactory.copyFieldsFromEmailChange(user, userBBDD);

				  userDao.save(userBBDD);
				  
			  }
			  else{
				  throw new Exception("Email already exists");
			  }
			  
		  }
		  catch (Exception ex) {
			  log.error(ex.getMessage());
			  response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			  return null;
		  }
		  
		  return user;
	  }
	  
	  
	  
	  /**
	   * POST /updatePassword  --> Update the user password
	   */
	  @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
	  @ResponseBody
	  public User updatePassword(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) {
		  
		  String token = "";
	      Long userId = null;
		  User userBBDD = null;
		  
		  try {
			  
			  token = request.getHeader(tokenHeader);
			  userId = jwtTokenUtil.getUserIdFromToken(token);
			  
			  userBBDD = userDao.findOne(userId);
			  
			  if(userBBDD != null && userId != null){
				  
				  userBBDD.setPassword(passwordEncoder.encode(user.getPassword()));
				  userBBDD.setLastPasswordResetDate(new Date());

				  userDao.save(userBBDD);
				  
			  }
			  else{
				  throw new Exception("Email already exists");
			  }
			  
		  }
		  catch (Exception ex) {
			  log.error(ex.getMessage());
			  response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			  return null;
		  }
		  
		  return user;
	  }
	  
	  
	  /**
	   * GET /getAuthenticatedUser
	   */
	  @RequestMapping(value = "/getAuthenticatedUser", method = RequestMethod.GET)
	  public UserResponse getAuthenticatedUser(HttpServletRequest request, HttpServletResponse response) {
		  
		  UserResponse userResponse = null;
		  String token = "";
		  Long userId = null;
		  
		  try{
			  
			  token = request.getHeader(tokenHeader);
			  userId = jwtTokenUtil.getUserIdFromToken(token);
			  
			  userResponse = userFactory.getUserResponse(userId);
			  
		  } catch(UsernameNotFoundException e){
			  
			  log.error(e.getMessage());
			  response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			  return null;
			  
		  } catch (IOException e) {
			
			  log.error(e.getMessage());
			  response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			  return null;
			  
		  }
		  
		  return userResponse;
	  }
	  
	  
	  /**
	   * POST /updateProfilePicture  --> Update user profile picture
	   */
	  @RequestMapping(value = "/updateProfilePicture", method = RequestMethod.POST, produces = MediaType.IMAGE_JPEG_VALUE)
	  public String updateProfilePicture(@RequestBody MultipartFile image, HttpServletRequest request, HttpServletResponse response){
		  
		  String token = "";
	      Long userId = null;
		  User userBBDD = null;
		  String newFileName = null;
		  String base64Img = null;
		  
		  try {
			  
			  token = request.getHeader(tokenHeader);
			  userId = jwtTokenUtil.getUserIdFromToken(token);
			  
			  userBBDD = userDao.findOne(userId);
			  
			  if(userBBDD != null && image != null){
				  
				  newFileName = FileFactory.uploadProfilePicture(image, userId);
				  
				  userBBDD.setPicture(newFileName);
				  
				  userDao.save(userBBDD);
				  
				  base64Img = FileFactory.getBase64FromProfilePictureName(env.getProperty("userProfilePicturesPath"), newFileName);
				  
			  }
			  
		  }
		  catch (Exception ex) {
			  log.error(ex.getMessage());
			  response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			  base64Img = null;
		  }
		  
		  return base64Img;
	  }

}
