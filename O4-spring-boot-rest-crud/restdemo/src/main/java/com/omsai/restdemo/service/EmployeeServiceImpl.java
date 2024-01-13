package com.omsai.restdemo.service;

import com.omsai.restdemo.dao.EmployeeDAO;
import com.omsai.restdemo.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeDAO employeeDAO;

    @Autowired
    public EmployeeServiceImpl(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Override
    public List<Employee> findAll() {
        return employeeDAO.findAll();
    }


    @Override
    public Employee findByID(int id) {
        Employee employee = employeeDAO.findByID(id);
        if (employee == null) {
            throw new RuntimeException("Employee not found for id: " + id);
        }
        return employee;
    }

    @Transactional
    @Override
    public Employee save(Employee employee) {
        return employeeDAO.save(employee);
    }

    @Transactional
    @Override
    public void deleteById(Employee employee) {
        employeeDAO.deleteById(employee);
    }
}
