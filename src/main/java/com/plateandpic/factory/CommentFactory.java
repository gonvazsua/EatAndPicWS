package com.plateandpic.factory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.plateandpic.constants.MessageConstants;
import com.plateandpic.dao.CommentDao;
import com.plateandpic.exceptions.CommentException;
import com.plateandpic.exceptions.PlateAndPicException;
import com.plateandpic.exceptions.PlatePictureException;
import com.plateandpic.exceptions.UserException;
import com.plateandpic.models.Comment;
import com.plateandpic.models.User;
import com.plateandpic.response.CommentRequestResponse;
import com.plateandpic.utils.Messages;
import com.plateandpic.validator.CommentValidator;

/**
 * @author gonzalo
 *
 */
@Service
public class CommentFactory {
	
	private static final Logger log = LoggerFactory.getLogger(CommentFactory.class);
	
	private static final Integer ROW_LIMIT = 20;
	private static final String QUERY_SORT = "registeredOn";
	
	@Autowired
	private CommentDao commentDao;
	
	@Autowired
	private UserFactory userFactory;
	
	@Autowired
	private PlatePictureFactory platePictureFactory;
	
	/**
	 * @param platePictureId
	 * @param page
	 * @return
	 * @throws IOException
	 * @throws PlateAndPicException 
	 */
	public List<CommentRequestResponse> getCommentsByPlatePictureId(Long platePictureId, Integer page) throws CommentException{
		
		List<CommentRequestResponse> commentsResponse = null;
		List<Comment> comments = null;
		Pageable pageable = null;
		
		try {
			
			pageable = new PageRequest(page, ROW_LIMIT, Sort.Direction.DESC, QUERY_SORT);
			comments = commentDao.findByPlatePicture_PlatePictureId(platePictureId, pageable);
			
			if(comments == null){
				throw new CommentException(MessageConstants.GENERAL_ERROR);
			}
			
			commentsResponse = buildCommentsResponse(comments);
			
		} catch(PlateAndPicException e) {
			
			log.error(e.getMessage());
			throw new CommentException(MessageConstants.GENERAL_ERROR);
			
		} catch(IOException e) {
			
			log.error(e.getMessage());
			throw new CommentException(MessageConstants.GENERAL_ERROR);
			
		}
		
		return commentsResponse;
		
	}
	
	/**
	 * @param comments
	 * @return
	 * @throws IOException
	 * @throws PlateAndPicException 
	 */
	private List<CommentRequestResponse> buildCommentsResponse(List<Comment> comments) throws IOException, PlateAndPicException{
		
		List<CommentRequestResponse> commentsResponse = new ArrayList<CommentRequestResponse>();
		CommentRequestResponse commentResponse = null;
		String userImage = "";
		
		for(Comment comment : comments){
			
			commentResponse = new CommentRequestResponse(comment);
			
			userImage = userFactory.convertBase64UserProfilePicture(commentResponse.getUserImage());
			commentResponse.setUserImage(userImage);
			
			commentsResponse.add(commentResponse);
			
		}
		
		return commentsResponse;
		
	}
	
	/**
	 * @param token
	 * @param comment
	 * @return
	 * @throws PlateAndPicException
	 * 
	 * Validate the commentRequest, build new Comment and save it
	 * 
	 */
	public CommentRequestResponse validateAndSave(String token, CommentRequestResponse comment) throws PlateAndPicException {	
		
		validateComment(comment);
		
		Comment newComment = buildComment(comment, token);
		
		newComment = commentDao.save(newComment);
		
		if(newComment == null){
			throw new CommentException(MessageConstants.COMMENT_NOT_SAVED);
		}
		
		comment = new CommentRequestResponse(newComment);
		
		comment.setUserImage(userFactory.convertBase64UserProfilePicture(comment.getUserImage()));
		
		return comment;
		
	}
	
	/**
	 * @param token
	 * @param comment
	 * @return
	 * @throws PlateAndPicException
	 * 
	 * Validate the commentRequest and remove it
	 * 
	 */
	public CommentRequestResponse validateAndRemove(String token, CommentRequestResponse comment) throws PlateAndPicException {	
		
		validateRemoveComment(comment, token);
		
		commentDao.delete(comment.getCommentId());
		
		return comment;
		
	}
	
	/**
	 * @param comment
	 * @throws PlateAndPicException
	 * 
	 * Validate the CommentRequestResponse
	 * 
	 */
	private void validateComment(CommentRequestResponse comment) throws PlateAndPicException{
		
		CommentValidator commentValidator = new CommentValidator(comment);
		
		commentValidator.validate();
		
	}
	
	/**
	 * @param comment
	 * @throws PlateAndPicException
	 * 
	 * Validate user logged is the same that the user comment
	 * 
	 */
	private void validateRemoveComment(CommentRequestResponse comment, String token) throws PlateAndPicException{
		
		User loggedUser = userFactory.getUserFromToken(token);
		
		User userComment = userFactory.getUserByUsername(comment.getUsername());
		
		if(!loggedUser.getId().equals(userComment.getId())){
			
			log.error("The userId: " + loggedUser.getId() + " has tried to remove a comment from the userId: " + userComment.getId());
			throw new PlateAndPicException(MessageConstants.COMMENT_NOT_REMOVED);
			
		}
		
	}
	
	/**
	 * @param commentRequestResponse
	 * @param userToken
	 * @return
	 * @throws CommentException
	 * 
	 * Build new Commment from the CommentRequestResponse passed as parameter
	 * 
	 */
	private Comment buildComment(CommentRequestResponse commentRequestResponse, String userToken) throws CommentException {
		
		Comment comment = null;
		
		try {
			
			comment = new Comment();
			comment.setComment(commentRequestResponse.getComment());
			comment.setRegisteredOn(new Date());
			comment.setUser(userFactory.getUserFromToken(userToken));		
			comment.setPlatePicture(platePictureFactory.getPlatePictureById(commentRequestResponse.getPlatePictureId()));
			
		} catch (UserException e){
			
			log.error("User not found with token: " + userToken);
			throw new CommentException(MessageConstants.COMMENT_NOT_SAVED);			
			
		} catch (PlatePictureException e){
			
			log.error("PlatePicture not found with: " + commentRequestResponse.getPlatePictureId());
			throw new CommentException(MessageConstants.COMMENT_NOT_SAVED);
			
		}
		
		return comment;
		
	}
	
}
