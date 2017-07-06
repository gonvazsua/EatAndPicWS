package com.plateandpic.validator;

import java.util.Date;

import com.plateandpic.models.Comment;
import com.plateandpic.utils.StringUtils;

public class CommentValidator {
	
	private static final Integer LENGTH_COMMENT = 50;
	
	public static Comment validate(Comment comment){
		
		comment.setComment(StringUtils.validateLength(comment.getComment(), LENGTH_COMMENT));
		comment.setRegisteredOn(new Date());
		
		return comment;
		
	}
}
