package com.eatandpic.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.eatandpic.dao.UserDao;
import com.eatandpic.models.User;
import com.eatandpic.security.JwtTokenUtil;
import com.eatandpic.security.JwtUser;
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
	  @RequestMapping(value = "/update", method = RequestMethod.POST)
	  @ResponseBody
	  public User updateUser(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) {
		  
		  String token = "";
	      Long userId = null;
		  User userBBDD = null;
	      
		  try {
			  
			  token = request.getHeader(tokenHeader);
			  userId = jwtTokenUtil.getUserIdFromToken(token);
			  
			  userBBDD = userDao.findOne(userId);
			  
			  if(userBBDD != null && UserValidator.validateUser(user) && userId != null){
				  
				  user.setId(userId);
				  userDao.save(user);
				  
			  }
			  else{
				  throw new Exception("User not valid");
			  }
			  
		  }
		  catch (Exception ex) {
			  response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			  return null;
		  }
		  
		  return user;
	  }
	  
	  @RequestMapping(value = "/getAuthenticatedUser", method = RequestMethod.GET)
	    public JwtUser getAuthenticatedUser(HttpServletRequest request) {
	        String token = request.getHeader(tokenHeader);
	        String username = jwtTokenUtil.getUsernameFromToken(token);
	        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
	        return user;
	    }

}
