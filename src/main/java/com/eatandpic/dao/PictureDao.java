package com.eatandpic.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.eatandpic.models.Picture;
import com.eatandpic.models.Restaurant;
import com.eatandpic.models.User;

@Transactional
public interface PictureDao extends CrudRepository<Picture, Long> {
	
	public List<Picture> findByUser(User user);

}
