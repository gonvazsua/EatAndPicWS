package com.plateandpic.response;

import com.plateandpic.models.Comment;
import com.plateandpic.utils.DateUtils;

/**
 * @author gonzalo
 *
 */
public class CommentResponse {
	
	private Long commentId;
	private String comment;
	private Long userId;
	private String username;
	private String userImage;
	private String registeredOn;
	
	/**
	 * @param comment
	 */
	public CommentResponse(Comment comment){
		
		this.commentId = comment.getCommentId();
		this.comment = comment.getComment();
		this.userId = comment.getUser().getId();
		this.username = comment.getUser().getUsername();
		this.userImage = comment.getUser().getPicture();
		this.registeredOn = DateUtils.getDateDDMMYYY(comment.getRegisteredOn());
		
	}
	
	/**
	 * @return
	 */
	public Long getCommentId() {
		return commentId;
	}
	
	/**
	 * @param commentId
	 */
	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}
	
	/**
	 * @return
	 */
	public String getComment() {
		return comment;
	}
	
	/**
	 * @param comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	/**
	 * @return
	 */
	public Long getUserId() {
		return userId;
	}
	
	/**
	 * @param userId
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	/**
	 * @return
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * @return
	 */
	public String getRegisteredOn() {
		return registeredOn;
	}
	
	/**
	 * @param registeredOn
	 */
	public void setRegisteredOn(String registeredOn) {
		this.registeredOn = registeredOn;
	}

	/**
	 * @return
	 */
	public String getUserImage() {
		return userImage;
	}

	/**
	 * @param userImage
	 */
	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}

}
