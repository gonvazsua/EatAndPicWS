package com.eatandpic.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import com.eatandpic.dao.UserDao;
import com.eatandpic.manager.UserManager;
import com.eatandpic.models.User;
import com.eatandpic.security.JwtTokenUtil;
import com.eatandpic.validator.UserValidator;

@RestController
@RequestMapping("/user")
public class UserController {

	  // Private fields

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
	
	  /**
	   * POST /create  --> Create a new user and save it in the database.
	   */
	  @CrossOrigin
	  @RequestMapping(value = "/register", method = RequestMethod.POST)
	  public User create(@RequestBody User user, HttpServletResponse response) {
	    
		  String userId = "";
		  try {
			  
			  if(user != null && userDao.findByEmail(user.getEmail()) == null && userDao.findByUsername(user.getUsername()) == null){
				  
				  userDao.save(user);
				  
				  response.setStatus(HttpServletResponse.SC_ACCEPTED);
			  }
			  else{
				  response.setStatus(HttpServletResponse.SC_BAD_REQUEST); 
				  user = null;
			  }
				  
		  }
		  catch (Exception ex) {
			  response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			  return null;
		  }
	    
		  return user;
	  }
	  
	  /**
	   * POST /login  --> Login new User
	   */
	  @CrossOrigin
	  @RequestMapping(value = "/login", method = RequestMethod.POST)
	  public User login(@RequestBody User user, HttpServletResponse response) {
	    

		  User existingUser = null;
		  try {
			  
			  if(user != null){

				  existingUser = userDao.findByUsername(user.getUsername());
			  }
			  else{
				  response.setStatus(HttpServletResponse.SC_BAD_REQUEST); 
				  existingUser = null;
			  }
				  
		  }
		  catch (Exception ex) {
			  response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			  return null;
		  }
	    
		  return existingUser;
	  }
  
	  /**
	   * GET /delete  --> Delete the user having the passed id.
	   */
	  @RequestMapping("/delete")
	  @ResponseBody
	  public String delete(long id) {
		  try {
		      //User user = new User(id);
		      //userDao.delete(user);
		  }
		  catch (Exception ex) {
			  return "Error deleting the user:" + ex.toString();
		  }
		  return "User succesfully deleted!";
	  }
  
	  /**
	   * POST /updateUser  --> Update the passed User
	   * email.
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
				  UserManager.copyFieldsFromPersonalDataChange(user, userBBDD);

				  userDao.save(userBBDD);
				  
			  }
			  else{
				  throw new Exception("Username already exists");
			  }
			  
		  }
		  catch (Exception ex) {
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
				  UserManager.copyFieldsFromEmailChange(user, userBBDD);

				  userDao.save(userBBDD);
				  
			  }
			  else{
				  throw new Exception("Email already exists");
			  }
			  
		  }
		  catch (Exception ex) {
			  response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			  return null;
		  }
		  
		  return user;
	  }
	  
	  /**
	   * POST /updateEmail  --> Update the user email
	   * email.
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
			  response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			  return null;
		  }
		  
		  return user;
	  }
	  
	  @RequestMapping(value = "/getAuthenticatedUser", method = RequestMethod.GET)
	  public User getAuthenticatedUser(HttpServletRequest request) {
		  String token = request.getHeader(tokenHeader);
		  Long userId = jwtTokenUtil.getUserIdFromToken(token);
		  User user = userDao.findOne(userId);
		  return user;
	  }
	  
	  @RequestMapping(value = "/updateProfilePicture", method = RequestMethod.POST)
	  public String updateProfilePicture(@RequestParam MultipartFile image, HttpServletRequest request, HttpServletResponse response){
		  return "";
	  }

}
