package com.omsai.demo.springcore.util;

import org.springframework.stereotype.Component;

public class SwimCoach implements Coach {

    public SwimCoach() {
        System.out.println("Spring has created swimCaoch bean.");
    }

    @Override
    public String getDailyWorkout() {
        return "Swim a 1000 meters as a warmup!";
    }
}
