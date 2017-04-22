package com.plateandpic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication
public class PlateAndPicApplication extends SpringBootServletInitializer {
	
	private static final Logger log = LoggerFactory.getLogger(PlateAndPicApplication.class);
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PlateAndPicApplication.class);
    }
	
	public static void main(String[] args) {
		SpringApplication.run(PlateAndPicApplication.class, args);
		
		log.info("Starting app...");
	}
}


