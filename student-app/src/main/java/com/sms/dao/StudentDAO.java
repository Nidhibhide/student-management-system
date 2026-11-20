
package com.sms.dao;

import com.sms.model.Student;
import java.sql.SQLException;
import java.util.List;

public interface StudentDAO {

    // Adds a new student record
    boolean addStudent(Student student) throws SQLException;

    // Retrieves all student records
    List<Student> getAllStudents() throws SQLException;

    // Searches for a student using ID
    // Returns null if the student does not exist
    Student searchStudentById(int id) throws SQLException;

    // Updates the details of an existing student
    boolean updateStudent(Student student) throws SQLException;
}
