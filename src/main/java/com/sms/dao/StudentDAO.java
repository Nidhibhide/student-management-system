
package com.sms.dao;

import com.sms.model.Student;
import java.sql.SQLException;
import java.util.List;

public interface StudentDAO {

    // Adds a new student record 
    boolean addStudent(Student student) throws SQLException;

    // Retrieves all student records 
    List<Student> getAllStudents() throws SQLException;

    // Searches for one student using their ID
    // Returns null if the student does not exist
    Student searchStudentById(int id) throws SQLException;
}
