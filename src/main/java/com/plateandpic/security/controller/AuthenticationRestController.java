package com.plateandpic.security.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.plateandpic.constants.MessageConstants;
import com.plateandpic.dao.UserDao;
import com.plateandpic.exceptions.PlateAndPicException;
import com.plateandpic.exceptions.UserException;
import com.plateandpic.security.JwtAuthenticationFactory;
import com.plateandpic.security.JwtAuthenticationRequest;
import com.plateandpic.security.JwtAuthenticationResponse;
import com.plateandpic.security.JwtSignUpRequest;
import com.plateandpic.security.JwtTokenUtil;
import com.plateandpic.security.JwtUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class AuthenticationRestController {
	
	private static final Logger log = LoggerFactory.getLogger(AuthenticationRestController.class);

	@Value("${jwt.header}")
	private String tokenHeader;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private JwtAuthenticationFactory authFactory;

	@RequestMapping(value = "${jwt.route.authentication.path}", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest)
			throws AuthenticationException {
		
		JwtAuthenticationResponse response = null;
		
		if(authenticationRequest == null
				|| authenticationRequest.getUsername() == null || authenticationRequest.getUsername().isEmpty()
				|| authenticationRequest.getPassword() == null || authenticationRequest.getPassword().isEmpty()){
			
			log.error("Parameters empty or null in createAuthenticationToken");
			throw new AuthenticationCredentialsNotFoundException("Credentials not found");
		}

		response = authFactory.login(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		// Return the token
		return ResponseEntity.ok(response);

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
	 * POST /create --> Create a new user and save it in the database.
	 * @throws UserException 
	 */
	@CrossOrigin
	@RequestMapping(value = "${jwt.route.authentication.register}", method = RequestMethod.POST)
	public JwtAuthenticationResponse create(@RequestBody JwtSignUpRequest request, HttpServletResponse response) throws PlateAndPicException {
		
		JwtAuthenticationResponse jwtResponse = null;
		
		if(request == null){
			log.error("Parameter request not found in create method of AuthenticationRestController");
			throw new PlateAndPicException(MessageConstants.GENERAL_ERROR);
		}
		
		jwtResponse = authFactory.validateAndCreateUser(request);
		
		response.setStatus(HttpServletResponse.SC_OK);
		
		return jwtResponse;
		
	}

}
