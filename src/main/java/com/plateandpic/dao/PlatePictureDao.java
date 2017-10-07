package com.plateandpic.dao;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.plateandpic.models.PlatePicture;
import com.plateandpic.models.Restaurant;
import com.plateandpic.models.User;

/**
 * @author gonzalo
 *
 */
@Transactional
public interface PlatePictureDao extends CrudRepository<PlatePicture, Long>, PlatePictureDaoCustom {
	
	/**
	 * @param user
	 * @param pageable
	 * @return
	 */
	public List<PlatePicture> findByUser(User user, Pageable pageable);
	
	/**
	 * @param users
	 * @param pageable
	 * @return
	 */
	public List<PlatePicture> findByUserIn(Set<User> users, Pageable pageable);

}
