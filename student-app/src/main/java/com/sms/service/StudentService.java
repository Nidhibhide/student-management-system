
package com.sms.service;

import com.sms.dao.StudentDAO;
import com.sms.dao.StudentDAOImpl;
import com.sms.model.Student;

import java.sql.SQLException;
import java.util.List;

public class StudentService {

    // DAO object connects the service layer to the database layer
    private final StudentDAO studentDAO;

    public StudentService() {
        this.studentDAO = new StudentDAOImpl();
    }

    // ADD STUDENT
    public boolean addStudent(Student student) throws SQLException {

        validateStudent(student);

        return studentDAO.addStudent(student);
    }

    // VIEW ALL STUDENTS
    public List<Student> getAllStudents() throws SQLException {

        return studentDAO.getAllStudents();
    }

    // SEARCH STUDENT BY ID
    public Student searchStudentById(int id) throws SQLException {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Student ID must be greater than zero."
            );
        }

        return studentDAO.searchStudentById(id);
    }

    // UPDATE STUDENT
    public boolean updateStudent(Student student) throws SQLException {

        // Validate the ID before updating
        if (student.getId() <= 0) {
            throw new IllegalArgumentException(
                    "Invalid student ID."
            );
        }

        // Validate the updated student details
        validateStudent(student);

        // Send the updated information to the DAO
        return studentDAO.updateStudent(student);
    }

    // COMMON VALIDATION METHOD
    // Reuse the same validation rules for Add and Update
    private void validateStudent(Student student) {

        if (student.getFirstName() == null ||
                student.getFirstName().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "First name is required."
            );
        }

        if (student.getEmail() == null ||
                !student.getEmail().matches(
                        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

            throw new IllegalArgumentException(
                    "Please enter a valid email."
            );
        }

        if (student.getCourse() == null ||
                student.getCourse().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Course is required."
            );
        }
    }
}
