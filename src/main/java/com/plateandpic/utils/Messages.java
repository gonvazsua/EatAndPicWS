package com.plateandpic.utils;

import java.util.Locale;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

@Component
public class Messages {
	
	@Autowired
    private MessageSource messageSource;

    private MessageSourceAccessor accessor;

    @PostConstruct
    private void init() {
    	
    	Locale spanish = new Locale("es");
        accessor = new MessageSourceAccessor(messageSource, spanish);
        
    }

    public String get(String code) {
    	
        return accessor.getMessage(code,new Locale("es"));
        
    }
	
}
