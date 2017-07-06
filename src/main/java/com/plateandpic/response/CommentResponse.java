package com.plateandpic.response;

import com.plateandpic.models.Comment;
import com.plateandpic.utils.DateUtils;

public class CommentResponse {
	
	private Long commentId;
	private String comment;
	private Long userId;
	private String username;
	private String userImage;
	private String registeredOn;
	
	public CommentResponse(Comment comment){
		
		this.commentId = comment.getCommentId();
		this.comment = comment.getComment();
		this.userId = comment.getUser().getId();
		this.username = comment.getUser().getUsername();
		this.userImage = comment.getUser().getPicture();
		this.registeredOn = DateUtils.getDateDDMMYYY(comment.getRegisteredOn());
		
	}
	
	public Long getCommentId() {
		return commentId;
	}
	
	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}
	
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getRegisteredOn() {
		return registeredOn;
	}
	
	public void setRegisteredOn(String registeredOn) {
		this.registeredOn = registeredOn;
	}

	public String getUserImage() {
		return userImage;
	}

	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}

}
