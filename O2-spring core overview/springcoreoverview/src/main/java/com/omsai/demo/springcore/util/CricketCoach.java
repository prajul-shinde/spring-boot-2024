package com.omsai.demo.springcore.util;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CricketCoach implements Coach {

    public CricketCoach() {
        System.out.println("Spring has created cricketCoach bean.");
    }

    @Override
    public String getDailyWorkout() {
        return "Practice fast bowling for 15 minutes";
    }
    @PostConstruct
    public void init() {
        System.out.println("In postconstruct init method of bean lifecycle");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("in predestroy destroy method of bean lifecycle");
    }
}
