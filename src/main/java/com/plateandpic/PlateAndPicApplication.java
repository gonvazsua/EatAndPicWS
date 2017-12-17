package com.plateandpic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;

/**
 * @author gonzalo
 *
 */
@SpringBootApplication
//@EnableDiscoveryClient
public class PlateAndPicApplication extends SpringBootServletInitializer {
	
	private static final Logger log = LoggerFactory.getLogger(PlateAndPicApplication.class);
	
	private static final Integer ENVIRONMENT = 1;
	
	/* (non-Javadoc)
	 * @see org.springframework.boot.web.servlet.support.SpringBootServletInitializer#configure(org.springframework.boot.builder.SpringApplicationBuilder)
	 */
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PlateAndPicApplication.class);
    }
	
	/**
	 * @param args
	 * 
	 * Param environment: dev, pre, prod
	 */
	public static void main(String[] args) {
		
		SpringApplication app = new SpringApplication(PlateAndPicApplication.class);
		ConfigurableEnvironment environment = new StandardEnvironment();
		
		String env = args[ENVIRONMENT];
		
		//Set environment
		environment.setDefaultProfiles(env);
		
		app.setEnvironment(environment);
		
		//SpringApplication.run(PlateAndPicApplication.class, args);
		app.run(args);
		
		log.error("Started app in "+env+" !!");
	}
}


