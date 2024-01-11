package com.omsai.demo.springbootoverview.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Value("${user.name}")
    private String username;

    @GetMapping("/welcome")
    public String welcome() {
        return "Hello! " + username;
    }

    @GetMapping("/welcomeAgain")
    public String welcomeAgain() {
        return "Hello Again!";
    }
}
