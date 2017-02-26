package com.eatandpic.dao;

import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.eatandpic.models.User;

@Transactional
public interface UserDao extends CrudRepository<User, Long> {
	
	/**
	   * This method will find an User instance in the database by its email.
	   * Note that this method is not implemented and its working code will be
	   * automagically generated from its signature by Spring Data JPA.
	   */
	  public User findByEmail(String email);
	  
	  public User findByUsername(String username);
	  
	  //public Set<User> findFollowers(User user);
	  
	  //public Set<Long> findFollowersIds(User user);

}
