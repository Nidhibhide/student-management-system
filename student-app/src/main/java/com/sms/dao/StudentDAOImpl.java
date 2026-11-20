
package com.sms.dao;

import com.sms.model.Student;
import com.sms.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAOImpl implements StudentDAO {

    // ADD STUDENT
    @Override
    public boolean addStudent(Student student) throws SQLException {

        String sql = "INSERT INTO students " +
                "(first_name, last_name, email, mobile, gender, dob, address, course, admission_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            // Set the values for each ? placeholder
            ps.setString(1, student.getFirstName());
            ps.setString(2, student.getLastName());
            ps.setString(3, student.getEmail());
            ps.setString(4, student.getMobile());
            ps.setString(5, student.getGender());
            ps.setDate(6, student.getDob());
            ps.setString(7, student.getAddress());
            ps.setString(8, student.getCourse());
            ps.setDate(9, student.getAdmissionDate());

            int rowsInserted = ps.executeUpdate();

            // executeUpdate() returns the number of affected rows
            return rowsInserted > 0;
        }
    }

    // VIEW ALL STUDENTS
    @Override
    public List<Student> getAllStudents() throws SQLException {

        List<Student> students = new ArrayList<>();

        String sql = "SELECT * FROM students";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // next() moves the ResultSet cursor to the next row
            while (rs.next()) {

                Student student = new Student();

                // Read database columns and store them in the Student object
                student.setId(rs.getInt("id"));
                student.setFirstName(rs.getString("first_name"));
                student.setLastName(rs.getString("last_name"));
                student.setEmail(rs.getString("email"));
                student.setMobile(rs.getString("mobile"));
                student.setGender(rs.getString("gender"));
                student.setDob(rs.getDate("dob"));
                student.setAddress(rs.getString("address"));
                student.setCourse(rs.getString("course"));
                student.setAdmissionDate(rs.getDate("admission_date"));

                students.add(student);
            }
        }

        return students;
    }

    // SEARCH STUDENT BY ID
    @Override
    public Student searchStudentById(int id) throws SQLException {

        // Only the student whose ID matches the given ID will be selected
        String sql = "SELECT * FROM students WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            // Replace the ? placeholder with the ID entered by the user
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                // If a matching record exists, read it
                if (rs.next()) {

                    Student student = new Student();

                    student.setId(rs.getInt("id"));
                    student.setFirstName(rs.getString("first_name"));
                    student.setLastName(rs.getString("last_name"));
                    student.setEmail(rs.getString("email"));
                    student.setMobile(rs.getString("mobile"));
                    student.setGender(rs.getString("gender"));
                    student.setDob(rs.getDate("dob"));
                    student.setAddress(rs.getString("address"));
                    student.setCourse(rs.getString("course"));
                    student.setAdmissionDate(rs.getDate("admission_date"));

                    return student;
                }
            }
        }

        // No matching ID was found
        return null;
    }
}
