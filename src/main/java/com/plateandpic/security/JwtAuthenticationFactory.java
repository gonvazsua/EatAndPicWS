package com.plateandpic.security;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.plateandpic.constants.MessageConstants;
import com.plateandpic.dao.UserDao;
import com.plateandpic.exceptions.PlateAndPicException;
import com.plateandpic.exceptions.UserException;
import com.plateandpic.models.Authority;
import com.plateandpic.models.AuthorityName;
import com.plateandpic.models.Rol;
import com.plateandpic.models.Status;
import com.plateandpic.models.User;
import com.plateandpic.validator.JwtSignUpRequestValidator;

@Component
public class JwtAuthenticationFactory {

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
	
	
	/**
	 * @param authenticationRequest
	 * @return
	 * 
	 * Create authentication and return the Jwt response with the token and the restaurantId, if is a restaurant user
	 */
	public JwtAuthenticationResponse login(String username, String password){
		
		JwtAuthenticationResponse response = null;
		
		performAuthentication(username, password);

		// Reload password post-security so we can generate token
		final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

		// Load User for getting userId and save it into the token
		final User user = userDao.findByUsername(username);

		//Generate token with username and user id
		final String token = jwtTokenUtil.generateToken(userDetails, user.getId());

		//Get the correspondant JwtResponse
		response = getJwtResponseByUser(user, token);
		
		return response;
		
	}
	
	/**
	 * @param username
	 * @param password
	 * 
	 * Perform the authentication calling to SecurityContextHolder of Spring Security
	 */
	private void performAuthentication(String username, String password){
		
		final Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
	}
	
	/**
	 * @param user
	 * @param token
	 * @return
	 * 
	 * Get the correct JwtAuthenticationResponse:
	 * If the user has a restaurant, build the JwtAuthenticationRestaurant and return it,
	 * else, the JwtAuthenticationResponse with only the token is returned.
	 */
	private JwtAuthenticationResponse getJwtResponseByUser(User user, String token) {

		JwtAuthenticationResponse response = null;

		if (user.getRestaurant() == null) {

			response = new JwtAuthenticationResponse(token);

		} else {

			response = new JwtAuthenticationRestaurantResponse(token, user.getRestaurant().getRestaurantId());

		}

		return response;

	}
	
	/**
	 * @param request
	 * @return
	 * @throws PlateAndPicException
	 * 
	 * 1. Validate sign up request
	 * 2. Create user from request
	 * 3. Check if username or email is available
	 * 4. Set sign up parameters
	 * 5. Save and login user
	 */
	public JwtAuthenticationResponse validateAndCreateUser(JwtSignUpRequest request) throws PlateAndPicException {
		
		JwtAuthenticationResponse response = null;
		
		validateJwtSignUpRequest(request);
		
		User user = parseUserFromSignUpRequest(request);
		
		checkExistsByEmailAndUser(user.getUsername(), user.getEmail());
		
		prepareForSigningUp(user);
		
		user = userDao.save(user);
		
		response = login(user.getUsername(), request.getPassword());
		
		return response;
		
	}
	
	/**
	 * @param request
	 * @throws PlateAndPicException
	 * 
	 * Validate request parameters
	 */
	private void validateJwtSignUpRequest(JwtSignUpRequest request) throws PlateAndPicException{
		
		JwtSignUpRequestValidator validator = new JwtSignUpRequestValidator(request);
		
		validator.validate();
		
	}
	
	/**
	 * @param request
	 * @return
	 * 
	 * Build user from sign up request setting the status of restaurant user
	 */
	private User parseUserFromSignUpRequest(JwtSignUpRequest request){
		
		User user = new User();
		
		user.setUsername(request.getUsername());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setStatus(Status.ACTIVE.getCode());
		
		return user;
		
	}
	
	/**
	 * @param username
	 * @param email
	 * @throws UserException
	 * 
	 * Check if exists one user with the username or the email passed as parameter
	 */
	private void checkExistsByEmailAndUser(String username, String email) throws UserException {
		
		User user = userDao.findByUsernameOrEmail(username, email);
		
		if(user != null){
			
			if(user.getUsername().equals(username))
				throw new UserException(MessageConstants.USER_USERNAME_ALREADY_USED);
			
			if(user.getEmail().equals(email))
				throw new UserException(MessageConstants.USER_EMAIL_ALREADY_USED);
			
		}
		
	}
	
	/**
	 * @param user
	 */
	private void prepareForSigningUp(User user){
		
		Date today = new Date();
		Authority authority = new Authority();
		authority.setName(AuthorityName.ROLE_USER);
		List<Authority> authorities = new ArrayList<Authority>();
		authorities.add(authority);
		Set<User> followers = new HashSet<User>();
		followers.add(user);
		
		user.setRegisterDate(today);
		user.setLastLogin(today);
		user.setRolId(Rol.USUARIO);
		user.setAuthorities(authorities);
		user.setEnabled(true);
		user.setLastPasswordResetDate(today);
    	user.setFollowers(followers);
    	user.setRestaurant(null);
		
	}
	
}
