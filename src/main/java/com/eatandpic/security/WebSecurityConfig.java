package com.eatandpic.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
 
	  @Override
	  protected void configure(HttpSecurity http) throws Exception {
	    
		  
		  // disable caching
	      http.headers().cacheControl();

		  //Funciona
//		  http.authorizeRequests()
//		  	.anyRequest().permitAll()
//		  	.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//		  	.and().httpBasic()
//		  	.and().csrf().disable();
		  
//		  http.authorizeRequests()
//		  	.antMatchers(HttpMethod.OPTIONS, "/*/**").permitAll()
//		  	.antMatchers("/user/login").permitAll()
//		  	.antMatchers("/user/register").permitAll()
//		  	.antMatchers("/user/logout").permitAll()
//		  	.anyRequest().fullyAuthenticated()
//		  	.and().csrf().disable();
		  
	      http.csrf().disable() // disable csrf for our requests.
	          .authorizeRequests()
	          //.antMatchers("/").permitAll()
	          .antMatchers(HttpMethod.OPTIONS, "/*/**").permitAll()
	          .antMatchers(HttpMethod.POST, "/user/login").permitAll()
	          .antMatchers(HttpMethod.POST, "/user/register").permitAll()
	          .antMatchers(HttpMethod.POST, "/user/logout").permitAll()
	          .anyRequest().authenticated()
	          .and()
	          // We filter the api/login requests
	          .addFilterBefore(new JWTLoginFilter("/user/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
	          // And filter other requests to check the presence of JWT in header
	          .addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
	  }
  
}
