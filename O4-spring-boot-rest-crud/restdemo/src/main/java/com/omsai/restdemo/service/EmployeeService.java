package com.omsai.restdemo.service;

import com.omsai.restdemo.entity.Employee;

import java.util.List;

public interface EmployeeService {

    public List<Employee> findAll();

    Employee findByID(int id);

    Employee save(Employee employee);

    void deleteById(Employee employee);
}
