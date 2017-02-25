package com.eatandpic.controller;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.eatandpic.dao.UserDao;
import com.eatandpic.models.User;

@RestController
@RequestMapping("/user")
public class UserController {

	  // Private fields

	  @Autowired
	  private UserDao userDao;
	  
	
	  /**
	   * POST /create  --> Create a new user and save it in the database.
	   */
	  @CrossOrigin
	  @RequestMapping(value = "/register", method = RequestMethod.POST)
	  public User create(@RequestBody User user, HttpServletResponse response) {
	    
		  String userId = "";
		  try {
			  
			  if(user != null && userDao.findByEmail(user.getEmail()) == null && userDao.findByUsername(user.getUsername()) == null){
				  
				  user.prepareForRegister();
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
	   * POST /create  --> Create a new user and save it in the database.
	   */
	  @CrossOrigin
	  @RequestMapping(value = "/login", method = RequestMethod.POST)
	  public User login(@RequestBody User user, HttpServletResponse response) {
	    
		  String userId = "";
		  String passwordLogin = "";
		  User existingUser = null;
		  try {
			  
			  if(user != null){
				  
				  passwordLogin = user.getPassword();
				  existingUser = userDao.findByUsername(user.getUsername());
				  
				  if(existingUser != null && existingUser.checkPassword(user.getPassword())){
					  existingUser.setLastLogin(new Date());
					  userDao.save(existingUser);
					  response.setStatus(HttpServletResponse.SC_ACCEPTED);
				  }
				  else{
					  response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				  }
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
		      User user = new User(id);
		      userDao.delete(user);
		  }
		  catch (Exception ex) {
			  return "Error deleting the user:" + ex.toString();
		  }
		  return "User succesfully deleted!";
	  }
  
	  /**
	   * GET /get-by-email  --> Return the id for the user having the passed
	   * email.
	   */
	  @RequestMapping("/get-by-email")
	  @ResponseBody
	  public String getByEmail(String email) {
		  String userId = "";
		  try {
			  	User user = userDao.findByEmail(email);
			  	userId = String.valueOf(user.getUserId());
		  }
		  catch (Exception ex) {
			  return "User not found";
		  }
		  return "The user id is: " + userId;
	  }
  
	  /**
	   * GET /update  --> Update the email and the name for the user in the 
	   * database having the passed id.
	   */
	  @RequestMapping("/update")
	  @ResponseBody
	  public String updateUser(long id, String email, String name) {
		  try {
			  User user = userDao.findOne(id);
			  user.setEmail(email);
			  user.setName(name);
			  userDao.save(user);
		  }
		  catch (Exception ex) {
			  return "Error updating the user: " + ex.toString();
		  }
		  return "User succesfully updated!";
	  }

}
