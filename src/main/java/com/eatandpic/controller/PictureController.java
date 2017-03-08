package com.eatandpic.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eatandpic.dao.PictureDao;
import com.eatandpic.dao.UserDao;
import com.eatandpic.models.Picture;
import com.eatandpic.models.User;
import com.eatandpic.security.JwtTokenUtil;

@RestController
@RequestMapping("/picture")
public class PictureController {
	
	@Autowired
	private PictureDao pictureDao;
	
	@Autowired
	protected UserDao userDao;
	
	@Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;
	
//	@RequestMapping(value = "/followersPictures", method = RequestMethod.GET)
//	public List<Picture> getFollowersPictures(@RequestParam long userId, HttpServletResponse response){
//		
//		List<Picture> userPictures = null, pictures = null;
//		Set<Long> followersIds = null;
//		User user = null;
//		
//		try{
//			
//			user = userDao.findOne(userId);
//			
////			if(user != null && !user.getFollowers().isEmpty()){
////				//Load followers
////			}
//			
//			response.setStatus(HttpServletResponse.SC_OK);
//			
//		} catch (Exception ex) {
//			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//			return null;
//		}
//		
//		return pictures;
//	}

    @RequestMapping(value = "/followersPictures", method = RequestMethod.GET)
    public User getAuthenticatedUser(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = userDao.findByUsername(username);
        return user;
    }


}
