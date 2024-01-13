package com.omsai.springDataRestDemo.dao;

import com.omsai.springDataRestDemo.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(path = "members")
public interface EMployeeRepository extends JpaRepository<Employee, Integer> {
}
