package com.plateandpic.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.plateandpic.constants.MessageConstants;
import com.plateandpic.exceptions.PlateAndPicException;
import com.plateandpic.exceptions.UserException;
import com.plateandpic.utils.Messages;

@ControllerAdvice
public class ExceptionController {
	
	private static final Logger log = LoggerFactory.getLogger(ExceptionController.class);
	
	@Autowired
	private Messages messages;
	
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<String> badCredentialsExceptionHandler(AuthenticationException ex) {
		
		log.error(ex.getMessage());
		
		String message = messages.get(MessageConstants.LOGIN_NOT_VALID);
		
		return new ResponseEntity<String>(message, HttpStatus.BAD_REQUEST);
	
	}
	
	@ExceptionHandler(PlateAndPicException.class)
	public ResponseEntity<String> plateAndPicExceptionHandler(PlateAndPicException ex){
		
		log.error(ex.getMessage());
		
		String message = messages.get(ex.getMessage());
		
		return new ResponseEntity<String>(message, HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> exceptionHandler(Exception ex) {
		
		log.error(ex.getMessage());
		
		String message = messages.get(MessageConstants.GENERAL_ERROR);
		
		return new ResponseEntity<String>(message, HttpStatus.BAD_REQUEST);
	
	}
	
	
}
