package com.eatandpic.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.eatandpic.models.PlatePicture;
import com.eatandpic.models.Restaurant;
import com.eatandpic.models.User;

@Transactional
public interface PlatePictureDao extends CrudRepository<PlatePicture, Long> {
	
	public List<PlatePicture> findByUser(User user);

}
