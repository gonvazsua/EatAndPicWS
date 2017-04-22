package com.plateandpic.security.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.plateandpic.dao.UserDao;
import com.plateandpic.models.User;
import com.plateandpic.security.JwtAuthenticationRequest;
import com.plateandpic.security.JwtAuthenticationResponse;
import com.plateandpic.security.JwtTokenUtil;
import com.plateandpic.security.JwtUser;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class AuthenticationRestController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(value = "${jwt.route.authentication.path}", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {

        // Perform the security
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-security so we can generate token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        
        //Load User for getting userId and save it into the token
        final User user = userDao.findByUsername(userDetails.getUsername());
        
        final String token = jwtTokenUtil.generateToken(userDetails, user.getId());

        // Return the token
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }

    @RequestMapping(value = "${jwt.route.authentication.refresh}", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
      /**
	   * POST /create  --> Create a new user and save it in the database.
	   */
	  @CrossOrigin
	  @RequestMapping(value = "${jwt.route.authentication.register}", method = RequestMethod.POST)
	  public ResponseEntity<?> create(@RequestBody User user, HttpServletResponse response) {
	    
		  String userId = "";
		  UserDetails userDetails = null;
		  JwtAuthenticationRequest authenticationRequest;
		  String lastPassword = "";
		  
		  try {
			  
			  if(user != null && userDao.findByEmail(user.getEmail()) == null && userDao.findByUsername(user.getUsername()) == null){
				  
				  user.prepareForRegisterRoleUser();
				  
				  //Crypt password
				  lastPassword = user.getPassword();
				  user.setPassword(passwordEncoder.encode(user.getPassword()));
				  
				  userDao.save(user);
				  
				  authenticationRequest = new JwtAuthenticationRequest(user.getUsername(), lastPassword);
			  
				  return this.createAuthenticationToken(authenticationRequest);
			  }
			  else{
				  response.setStatus(HttpServletResponse.SC_BAD_REQUEST); 
				  user = null;
			  }
				  
		  }
		  catch (Exception ex) {
			  response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			  return null;
		  }
	    
		  return null;
	  }

}
