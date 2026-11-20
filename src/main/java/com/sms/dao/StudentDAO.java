
package com.sms.dao;

import com.sms.model.Student;
import java.sql.SQLException;
import java.util.List;

// DAO interface defines the database operations for Student.
public interface StudentDAO {

    // Add a new student
    boolean addStudent(Student student) throws SQLException;

    // Retrieve all students
    List<Student> getAllStudents() throws SQLException;

    // Search for a student using ID
    Student searchStudentById(int id) throws SQLException;

    // Update an existing student
    boolean updateStudent(Student student) throws SQLException;

    // Delete a student using ID
    boolean deleteStudent(int id) throws SQLException;
}