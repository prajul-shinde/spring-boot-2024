package com.omsai.demo.springcore.springcoreoverview.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class SetterInjectionDemoService {

    public SetterInjectionDemoService() {
        System.out.println("Spring has created SetterInjectionDemoService bean");
    }

    public String getData() {
        return "hello";
    }
}
