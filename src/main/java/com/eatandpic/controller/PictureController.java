package com.eatandpic.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.eatandpic.dao.PictureDao;
import com.eatandpic.dao.UserDao;
import com.eatandpic.models.Picture;
import com.eatandpic.models.Plate;
import com.eatandpic.models.Restaurant;
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

    @RequestMapping(value = "/followersPictures", method = RequestMethod.GET)
    public User getAuthenticatedUser(HttpServletRequest request) {
        
    	String token = request.getHeader(tokenHeader);
        
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        
        return null;
    }
    
    /**
     * POST /save  --> Save new Picture and return it
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public Picture save(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Picture picture){
		  
		try{
			
			pictureDao.save(picture);
			
			response.setStatus(HttpServletResponse.SC_OK);
			  
		} catch(Exception e){
			picture = null;
		}
		  
		return picture;
		 
	}


}
