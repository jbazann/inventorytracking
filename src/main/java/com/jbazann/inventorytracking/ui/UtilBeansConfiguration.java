package com.jbazann.inventorytracking.ui;

import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilBeansConfiguration {

    @Bean
    public JacksonJsonParser jacksonJsonParser() {
        return new JacksonJsonParser();
    }
    
}
