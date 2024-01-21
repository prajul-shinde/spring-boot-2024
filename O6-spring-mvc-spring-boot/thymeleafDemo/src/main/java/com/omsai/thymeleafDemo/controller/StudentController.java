package com.omsai.thymeleafDemo.controller;

import com.omsai.thymeleafDemo.model.Student;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class StudentController {

    @Value("${languages}")
    private List<String> languages;
    @Value("${operatingSystems}")
    private List<String> operatingSystems;

    @Value("${countries}")
    private List<String> countries;

    @GetMapping("/showStudentForm")
    public String showForm(Model model) {

        model.addAttribute("student", new Student());
        model.addAttribute("languages", languages);
        model.addAttribute("countries", countries);
        model.addAttribute("operatingSystems", operatingSystems);
        return "student-form";
    }

    @PostMapping("/processStudentForm")
    public String processForm(@ModelAttribute("student") Student student) {

        System.out.println(student.toString());
        return "confirm-submission";
    }
}
