package com.plateandpic.security.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestConnectionController {
	
	@RequestMapping(value = "/testConnection", method = RequestMethod.GET)
	public String testConnection(){
		return "Test is OK";
	}

}
