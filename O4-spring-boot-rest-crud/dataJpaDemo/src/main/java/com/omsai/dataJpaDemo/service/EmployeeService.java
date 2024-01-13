package com.omsai.dataJpaDemo.service;

import com.omsai.dataJpaDemo.entity.Employee;

import java.util.List;

public interface EmployeeService {

    public List<Employee> findAll();

    Employee findByID(int id);

    Employee save(Employee employee);

    void deleteById(int id);
}
