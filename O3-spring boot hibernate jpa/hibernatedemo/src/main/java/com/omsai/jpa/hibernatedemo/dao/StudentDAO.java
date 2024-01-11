package com.omsai.jpa.hibernatedemo.dao;

import com.omsai.jpa.hibernatedemo.entity.Student;

import java.util.List;

public interface StudentDAO {

    void save(final Student student);

    Student findById(final Integer id);

    List<Student> findAll();

    List<Student> findByLastName(final String theLastName);

    void update(Student student);

    void delete(Integer id);

    int deleteAll();
}
