package com.plateandpic.controller;

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

import com.plateandpic.dao.PlatePictureDao;
import com.plateandpic.dao.UserDao;
import com.plateandpic.models.Plate;
import com.plateandpic.models.PlatePicture;
import com.plateandpic.models.Restaurant;
import com.plateandpic.models.User;
import com.plateandpic.security.JwtTokenUtil;

@RestController
@RequestMapping("/picture")
public class PictureController {
	
	@Autowired
	private PlatePictureDao pictureDao;
	
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
	public PlatePicture save(HttpServletRequest request, HttpServletResponse response,
			@RequestBody PlatePicture picture){
		  
		try{
			
			pictureDao.save(picture);
			
			response.setStatus(HttpServletResponse.SC_OK);
			  
		} catch(Exception e){
			picture = null;
		}
		  
		return picture;
		 
	}


}
