package com.eatandpic.dao;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.eatandpic.models.User;

@Transactional
public interface UserDao extends CrudRepository<User, Long> {
	
	
	  public User findByEmail(String email);
	  
	  public User findByUsername(String username);
	  
	  //public Set<User> findFollowers(User user);
	  
	  //public Set<Long> findFollowersIds(User user);

}
