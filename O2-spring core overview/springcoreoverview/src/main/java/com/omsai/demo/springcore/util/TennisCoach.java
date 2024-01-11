package com.omsai.demo.springcore.util;

import org.springframework.stereotype.Component;

@Component
public class TennisCoach implements Coach {

    public TennisCoach() {
        System.out.println("Spring has created tennisCoach bean.");
    }

    @Override
    public String getDailyWorkout() {
        return "Practice your backhand volley";
    }
}
