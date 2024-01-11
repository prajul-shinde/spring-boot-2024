package com.omsai.demo.springcore.springcoreoverview.rest;

import com.omsai.demo.springcore.springcoreoverview.service.SetterInjectionDemoService;
import com.omsai.demo.springcore.util.Coach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    private final Coach coach;

    private Coach anotherCoach;

    private Coach swimCoach;

    private SetterInjectionDemoService service;

    /**
     * @param coach Constructor injection/Qualifier demo.
     *              autowired is optional if only one constructor is there.
     *              we have multiple implementation of coach. so we have to use qualifier or primary on one of implementation.
     *              if no qualifier provided then check trackcoach class it marked as primary.
     *              qualifier overrides primary.
     */
    @Autowired
    public DemoController(
            @Qualifier("cricketCoach") final Coach coach,
            @Qualifier("cricketCoach") final Coach anotherCoach,
            @Qualifier("swimCoach") final Coach swimCoach
            ) {
        System.out.println("Spring has created DemoController bean");
        this.coach = coach;
        this.anotherCoach = anotherCoach;
        this.swimCoach = swimCoach;
    }

    /**
     * Setter Injection.
     *
     * @param service
     */
    @Autowired
    public void setSetterInjectionDemoService(final SetterInjectionDemoService service) {
        this.service = service;
    }

    @GetMapping("/dailyworkout")
    public String getDailyWorkout() {
        return coach.getDailyWorkout();
    }

    @GetMapping("/setterinjection")
    public String getData() {
        return service.getData();
    }

    /**
     * Endpoint to check bean scope
     * in case of prototype it will return false
     * in case of singleton returns true
     */
    @GetMapping("/checkscope")
    public String checkScope() {
        return "comparing beans: mycoach == anothercoach " + (coach == anotherCoach);
    }

    @GetMapping("/javabeanconfig")
    public String testJavaBeanConfig() {
        return swimCoach.getDailyWorkout();
    }
}
