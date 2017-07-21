package com.plateandpic.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.plateandpic.models.Comment;

/**
 * @author gonzalo
 *
 */
@Transactional
public interface CommentDao extends CrudRepository<Comment, Long> {
	
	/**
	 * @param pictureId
	 * @param pageable
	 * @return
	 */
	public List<Comment> findByPlatePicture_PlatePictureId(Long pictureId, Pageable pageable);
	
}
