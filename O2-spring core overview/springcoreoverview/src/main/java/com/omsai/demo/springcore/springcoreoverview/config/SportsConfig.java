package com.omsai.demo.springcore.springcoreoverview.config;

import com.omsai.demo.springcore.util.Coach;
import com.omsai.demo.springcore.util.SwimCoach;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SportsConfig {

    @Bean
    public Coach swimCoach(){
        return new SwimCoach();
    }
}
