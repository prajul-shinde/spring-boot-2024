package com.omsai.thymeleafDemo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CourseCodeValidator implements ConstraintValidator<CourseCode, String> {

    private String courseCodePrefix;

    @Override
    public void initialize(CourseCode courseCode) {
        this.courseCodePrefix = courseCode.value();
    }

    @Override
    public boolean isValid(String code, ConstraintValidatorContext constraintValidatorContext) {

        return code.startsWith("LUV");
    }
}
