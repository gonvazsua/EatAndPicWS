package com.plateandpic.factory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.plateandpic.dao.CommentDao;
import com.plateandpic.exceptions.CommentException;
import com.plateandpic.exceptions.UserException;
import com.plateandpic.models.Comment;
import com.plateandpic.models.User;
import com.plateandpic.response.CommentResponse;
import com.plateandpic.validator.CommentValidator;

/**
 * @author gonzalo
 *
 */
@Service
public class CommentFactory {
	
	private static final Integer ROW_LIMIT = 50;
	private static final String QUERY_SORT = "registeredOn";
	
	@Autowired
	private CommentDao commentDao;
	
	@Autowired
	private UserFactory userFactory;
	
	/**
	 * @param platePictureId
	 * @param page
	 * @return
	 * @throws CommentException
	 * @throws IOException
	 */
	public List<CommentResponse> getCommentsByPlatePictureId(Long platePictureId, Integer page) throws CommentException, IOException{
		
		List<CommentResponse> commentsResponse = null;
		List<Comment> comments = null;
		Pageable pageable = null;
		
		pageable = new PageRequest(page, ROW_LIMIT, Sort.Direction.DESC, QUERY_SORT);
		comments = commentDao.findByPlatePicture_PlatePictureId(platePictureId, pageable);
		
		if(comments == null){
			throw new CommentException("Comments not found with platePictureId = " + platePictureId);
		}
		
		commentsResponse = buildCommentsResponse(comments);
		
		return commentsResponse;
		
	}
	
	/**
	 * @param comments
	 * @return
	 * @throws IOException
	 */
	private List<CommentResponse> buildCommentsResponse(List<Comment> comments) throws IOException{
		
		List<CommentResponse> commentsResponse = new ArrayList<CommentResponse>();
		CommentResponse commentResponse = null;
		String userImage = "";
		
		for(Comment comment : comments){
			
			commentResponse = new CommentResponse(comment);
			
			userImage = FileFactory.getBase64FromProfilePictureName(userFactory.getProfilePicturePath(), commentResponse.getUserImage());
			commentResponse.setUserImage(userImage);
			
			commentsResponse.add(commentResponse);
			
		}
		
		return commentsResponse;
		
	}
	
	/**
	 * @param token
	 * @param comment
	 * @return
	 * @throws UserException
	 * @throws CommentException
	 */
	public CommentResponse validateAndSave(String token, Comment comment) throws UserException, CommentException{
		
		User user = null;
		CommentResponse commentResponse = null;
		
		user = userFactory.getUserFromToken(token);
		
		comment.setUser(user);
		
		comment = CommentValidator.validate(comment);
		
		comment = commentDao.save(comment);
		
		if(comment == null){
			throw new CommentException("Comment not saved!");
		}
		
		commentResponse = new CommentResponse(comment);
		
		return commentResponse;
		
	}
	
}
