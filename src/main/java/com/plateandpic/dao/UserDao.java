package com.plateandpic.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.plateandpic.models.User;

/**
 * @author gonzalo
 *
 */
@Transactional
public interface UserDao extends CrudRepository<User, Long>, UserDaoCustom {
	
	
	/**
	 * @param username
	 * @param email
	 * @return
	 */
	public User findByUsernameOrEmail(String username, String email);
	
	  /**
	 * @param email
	 * @return
	 */
	public User findByEmail(String email);
	  
	  /**
	 * @param username
	 * @return
	 */
	public User findByUsername(String username);
	
	
	/**
	 * @param key
	 * @param page
	 * @return
	 * 
	 * Search user having name, lastname, username or email like the key passed as parameter
	 */
	@Query(value = "Select * from user where lower(firstname) like %:key% or lower(lastname) like %:key% "
			+ " or lower(email) like %:key% or lower(username) like %:key% order by username asc", nativeQuery = true)
	public List<User> findUserByKey(@Param("key") String key);

}
