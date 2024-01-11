package com.omsai.demo.springcore.springcoreoverview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Component scanning. util is out of base package so we have to enable scanning
 */
@SpringBootApplication(
        scanBasePackages = {"com.omsai.demo.springcore.springcoreoverview", "com.omsai.demo.springcore.util"}
)
public class SpringcoreoverviewApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringcoreoverviewApplication.class, args);
    }

}
