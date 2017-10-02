package com.plateandpic.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.plateandpic.constants.MessageConstants;
import com.plateandpic.exceptions.CommentException;
import com.plateandpic.exceptions.PlateAndPicException;
import com.plateandpic.exceptions.UserException;
import com.plateandpic.factory.CommentFactory;
import com.plateandpic.models.Comment;
import com.plateandpic.response.CommentResponse;

/**
 * @author gonzalo
 *
 */
@RestController
@RequestMapping("/comment")
public class CommentController {
	
	private static final Logger log = LoggerFactory.getLogger(CommentController.class);
	
	@Autowired
	private CommentFactory commentFactory;
	
	@Value("${jwt.header}")
	private String tokenHeader;
	
	/**
	 * @param request
	 * @param response
	 * @param platePictureId
	 * @param page
	 * @return
	 * @throws PlateAndPicException 
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getByPlatePicture", method = RequestMethod.GET)
	@ResponseBody
	public List<CommentResponse> getByPlatePicture(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam Long platePictureId, @RequestParam Integer page) throws PlateAndPicException, IOException{
		
		List<CommentResponse> comments = null;
			
		if(platePictureId != null && platePictureId > 0
				&& page != null && page >= 0){
			
			comments = commentFactory.getCommentsByPlatePictureId(platePictureId, page);
			response.setStatus(HttpServletResponse.SC_OK);	
		
		}
		else{
			log.error("Params not valid -> platePictureId: " + platePictureId
					+ ", page: " + page + ". In CommentController.getByPlatePicture()");
			throw new CommentException(MessageConstants.GENERAL_ERROR);
		}
			
		return comments;
		
	}
	

	/**
	 * @param request
	 * @param response
	 * @param comment
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public CommentResponse save(HttpServletRequest request, HttpServletResponse response, 
			@RequestBody Comment comment){
		
		CommentResponse savedComment;
		String token = "";
		
		try{
			
			token = request.getHeader(tokenHeader);
			
			savedComment = commentFactory.validateAndSave(token, comment);
			
		} catch (CommentException e) {
			
			savedComment = null;
			log.error("Error save:" + e.getMessage());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			
		} catch (UserException e) {
			
			savedComment = null;
			log.error("Error save:" + e.getMessage());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		
		return savedComment;
		
	}

}
