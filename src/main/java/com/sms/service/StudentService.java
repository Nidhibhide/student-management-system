
package com.sms.service;

import com.sms.dao.StudentDAO;
import com.sms.dao.StudentDAOImpl;
import com.sms.model.Student;

import java.sql.SQLException;
import java.util.List;

// Service layer contains validation and business logic.
public class StudentService {

    private final StudentDAO studentDAO = new StudentDAOImpl();

    // Add a student after validating the details
    public boolean addStudent(Student student) throws SQLException {

        validateStudent(student);

        return studentDAO.addStudent(student);
    }

    // Get all students
    public List<Student> getAllStudents() throws SQLException {

        return studentDAO.getAllStudents();
    }

    // Search student by ID
    public Student searchStudentById(int id) throws SQLException {

        if (id <= 0) {
            throw new IllegalArgumentException("Student ID must be greater than zero.");
        }

        return studentDAO.searchStudentById(id);
    }

    // Update student details
    public boolean updateStudent(Student student) throws SQLException {

        if (student.getId() <= 0) {
            throw new IllegalArgumentException("Invalid student ID.");
        }

        validateStudent(student);

        return studentDAO.updateStudent(student);
    }

    // Delete student by ID
    public boolean deleteStudent(int id) throws SQLException {

        if (id <= 0) {
            throw new IllegalArgumentException("Student ID must be greater than zero.");
        }

        return studentDAO.deleteStudent(id);
    }

    // Common validation method used by Add and Update
    private void validateStudent(Student student) {

        if (student == null) {
            throw new IllegalArgumentException("Student details cannot be empty.");
        }

        if (student.getFirstName() == null ||
                student.getFirstName().isBlank()) {

            throw new IllegalArgumentException("First name is required.");
        }

        if (student.getEmail() == null ||
                !student.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

            throw new IllegalArgumentException("Please enter a valid email address.");
        }

        if (student.getCourse() == null ||
                student.getCourse().isBlank()) {

            throw new IllegalArgumentException("Course is required.");
        }
    }
}