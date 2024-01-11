package com.omsai.demo.springcore.util;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class BaseballCoach implements Coach {

    /**
     * to check how spring initializes bean.
     */
    public BaseballCoach() {
        System.out.println("Spring has created baseballCoach bean.");
    }

    @Override
    public String getDailyWorkout() {
        return "Spend 30 minutes in batting practice.";
    }
}
