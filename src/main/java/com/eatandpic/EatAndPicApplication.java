package com.eatandpic;

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
public class EatAndPicApplication extends SpringBootServletInitializer {
	
	private static final Logger log = LoggerFactory.getLogger(EatAndPicApplication.class);
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(EatAndPicApplication.class);
    }
	
	public static void main(String[] args) {
		SpringApplication.run(EatAndPicApplication.class, args);
		
		log.info("Starting app...");
	}
}


