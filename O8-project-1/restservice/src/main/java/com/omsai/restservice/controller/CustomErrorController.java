package com.omsai.restservice.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class CustomErrorController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity handleBindErrors(MethodArgumentNotValidException exception) {

        List errorList = exception.getFieldErrors().stream().map(fieldError -> {
            Map<String, String> errormap = new HashMap<>();
            errormap.put(fieldError.getField(), fieldError.getDefaultMessage());
            return errormap;
        }).toList();
        return ResponseEntity.badRequest().body(errorList);

    }

    @ExceptionHandler
    ResponseEntity handleJPAViolations(TransactionSystemException exception) {

        ResponseEntity.BodyBuilder responseEntity = ResponseEntity.badRequest();

        if (exception.getCause().getCause() instanceof ConstraintViolationException) {
            ConstraintViolationException ve = (ConstraintViolationException) exception.getCause().getCause();
            List errors = ve.getConstraintViolations().stream().map(constraintViolation -> {
                Map<String, String> errormap = new HashMap<>();
                errormap.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
                return errormap;
            }).toList();
            return responseEntity.body(errors);
        }
        return responseEntity.build();
    }
}
