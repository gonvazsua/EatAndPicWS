package com.eatandpic.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
 
	  @Override
	  protected void configure(HttpSecurity http) throws Exception {
	    
		  //Funciona
//		  http.authorizeRequests()
//		  	.anyRequest().permitAll()
//		  	.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//		  	.and().httpBasic()
//		  	.and().csrf().disable();
		  
		  http.authorizeRequests()
		  	.antMatchers(HttpMethod.OPTIONS, "/*/**").permitAll()
		  	.antMatchers("/user/login").permitAll()
		  	.antMatchers("/user/register").permitAll()
		  	.antMatchers("/user/logout").permitAll()
		  	.anyRequest().fullyAuthenticated()
		  	.and().csrf().disable();
	  }
  
}
