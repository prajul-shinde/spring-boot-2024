package com.omsai.jpa.hibernatedemo;

import com.omsai.jpa.hibernatedemo.dao.StudentDAO;
import com.omsai.jpa.hibernatedemo.entity.Student;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class HibernatedemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(HibernatedemoApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(final StudentDAO studentDAO) {
        return (runner) -> {
//            createStudent(studentDAO);
            createMultipleStudent(studentDAO);
//            readStudent(studentDAO);
//            queryForStudents(studentDAO);
//            queryForStudentsByLastName(studentDAO);
//            updateStudent(studentDAO);
//            deleteStudent(studentDAO);
//            deleteAllStudents(studentDAO);
        };
    }

    private void deleteAllStudents(StudentDAO studentDAO) {
        System.out.println("Deleting all students...");
        int numRowsDeleted = studentDAO.deleteAll();
        System.out.println("Deleted row count: " + numRowsDeleted);
    }

    private void deleteStudent(StudentDAO studentDAO) {

        System.out.println("Deleting student with id 3");
        studentDAO.delete(3);
    }

    private void updateStudent(StudentDAO studentDAO) {

        // retrieve student by id
        Student student = studentDAO.findById(1);

        //change first name
        student.setFirstName("John");

        //update student
        studentDAO.update(student);

        // display updated student
        System.out.println("updated student: " + student);
    }

    private void queryForStudentsByLastName(StudentDAO studentDAO) {
        studentDAO.findByLastName("Doe").forEach(System.out::println);
    }

    private void queryForStudents(final StudentDAO studentDAO) {
        studentDAO.findAll().forEach(System.out::println);
    }

    private void readStudent(final StudentDAO studentDAO) {

        //create student
        System.out.println("Creating student object...");
        final Student tempStudent = new Student("Daffy", "Duck", "daffy@luv2code.com");

        //save the student
        System.out.println("Saving the student...");
        studentDAO.save(tempStudent);
        final int id = tempStudent.getId();
        System.out.println("Saved student. Generated id: " + id);

        // retrieve student
        System.out.println("Retrieving a student with id: " + id);
        Student myStudent = studentDAO.findById(id);

        //display student
        System.out.println("Found the student: " + myStudent);
    }

    private void createMultipleStudent(final StudentDAO studentDAO) {
        System.out.println("Creating 3 student objects");
        final List<Student> students = List.of(
                new Student("John", "Doe", "john@luv2code.com"),
                new Student("Mary", "Public", "mary@luv2code.com"),
                new Student("Bonita", "Applebum", "bonita@luv2code.com"));
        System.out.println("Saving the students");
        students.forEach(studentDAO::save);
    }

    private void createStudent(final StudentDAO studentDAO) {
        System.out.println("Creating student object...");
        final Student tempStudent = new Student("Paul", "Doe", "paul@luv2code.com");

        System.out.println("Saving the student...");
        studentDAO.save(tempStudent);

        System.out.println("Saved student. Generated id: " + tempStudent.getId());
    }
}
