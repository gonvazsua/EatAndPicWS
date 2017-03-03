package com.eatandpic.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eatandpic.dao.PictureDao;
import com.eatandpic.dao.UserDao;
import com.eatandpic.models.Picture;
import com.eatandpic.models.User;

@RestController
@RequestMapping("/picture")
public class PictureController {
	
	@Autowired
	private PictureDao pictureDao;
	
	@Autowired
	protected UserDao userDao;
	
	@RequestMapping(value = "/followersPictures", method = RequestMethod.GET)
	public List<Picture> getFollowersPictures(@RequestParam long userId, HttpServletResponse response){
		
		List<Picture> userPictures = null, pictures = null;
		Set<Long> followersIds = null;
		User user = null;
		
		try{
			
			user = userDao.findOne(userId);
			
//			if(user != null && !user.getFollowers().isEmpty()){
//				//Load followers
//			}
			
			response.setStatus(HttpServletResponse.SC_OK);
			
		} catch (Exception ex) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
		
		return pictures;
	}

}
