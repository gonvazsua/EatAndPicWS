package com.plateandpic.validator;

import java.util.Date;

import com.plateandpic.models.Comment;
import com.plateandpic.utils.StringUtils;

/**
 * @author gonzalo
 *
 */
public class CommentValidator {
	
	private static final Integer LENGTH_COMMENT = 50;
	
	/**
	 * @param comment
	 * @return
	 */
	public static Comment validate(Comment comment){
		
		comment.setComment(StringUtils.validateLength(comment.getComment(), LENGTH_COMMENT));
		comment.setRegisteredOn(new Date());
		
		return comment;
		
	}
}
