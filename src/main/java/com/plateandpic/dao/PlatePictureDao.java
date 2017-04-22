package com.plateandpic.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.plateandpic.models.PlatePicture;
import com.plateandpic.models.Restaurant;
import com.plateandpic.models.User;

@Transactional
public interface PlatePictureDao extends CrudRepository<PlatePicture, Long> {
	
	public List<PlatePicture> findByUser(User user);

}
