package com.plateandpic.validator;

import com.plateandpic.constants.MessageConstants;
import com.plateandpic.exceptions.CommentException;
import com.plateandpic.exceptions.PlateAndPicException;
import com.plateandpic.response.CommentRequestResponse;
import com.plateandpic.utils.ValidationUtils;

/**
 * @author gonzalo
 *
 */
public class CommentValidator {
	
	private static final Integer LENGTH_COMMENT = 150;
	
	private CommentRequestResponse comment;
	
	/**
	 * @param comment
	 * 
	 * Constructor
	 */
	public CommentValidator(CommentRequestResponse comment){
		
		this.comment = comment;
		
	}
	
	/**
	 * @throws PlateAndPicException 
	 * 
	 * Validate only the length of the comment
	 */
	public void validate() throws PlateAndPicException{
		
		ValidationUtils.validateMandatoryStringLength(comment.getComment(), LENGTH_COMMENT, 
				getCommentException(MessageConstants.COMMENT_NOT_SAVED));
		
	}
	
	/**
	 * @param message
	 * @return
	 */
	private CommentException getCommentException(String message){
		
		return new CommentException(message);
		
	}
}
