
package com.sms.dao;

import com.sms.model.Student;
import com.sms.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// This class contains the actual SQL queries for Student operations.
public class StudentDAOImpl implements StudentDAO {

    // Add a new student to the database
    @Override
    public boolean addStudent(Student student) throws SQLException {

        String sql = "INSERT INTO students " +
                "(first_name, last_name, email, mobile, gender, dob, address, course, admission_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, student.getFirstName());
            ps.setString(2, student.getLastName());
            ps.setString(3, student.getEmail());
            ps.setString(4, student.getMobile());
            ps.setString(5, student.getGender());
            ps.setDate(6, student.getDob());
            ps.setString(7, student.getAddress());
            ps.setString(8, student.getCourse());
            ps.setDate(9, student.getAdmissionDate());

            return ps.executeUpdate() > 0;
        }
    }

    // Retrieve all students from the database
    @Override
    public List<Student> getAllStudents() throws SQLException {

        List<Student> students = new ArrayList<>();

        String sql = "SELECT * FROM students";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Student student = mapResultSetToStudent(rs);
                students.add(student);
            }
        }

        return students;
    }

    // Search for a student using their ID
    @Override
    public Student searchStudentById(int id) throws SQLException {

        String sql = "SELECT * FROM students WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapResultSetToStudent(rs);
                }
            }
        }

        // Return null if no student is found
        return null;
    }

    // Update an existing student's details
    @Override
    public boolean updateStudent(Student student) throws SQLException {

        String sql = "UPDATE students SET " +
                "first_name = ?, " +
                "last_name = ?, " +
                "email = ?, " +
                "mobile = ?, " +
                "gender = ?, " +
                "dob = ?, " +
                "address = ?, " +
                "course = ?, " +
                "admission_date = ? " +
                "WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, student.getFirstName());
            ps.setString(2, student.getLastName());
            ps.setString(3, student.getEmail());
            ps.setString(4, student.getMobile());
            ps.setString(5, student.getGender());
            ps.setDate(6, student.getDob());
            ps.setString(7, student.getAddress());
            ps.setString(8, student.getCourse());
            ps.setDate(9, student.getAdmissionDate());
            ps.setInt(10, student.getId());

            return ps.executeUpdate() > 0;
        }
    }

    // Delete a student using their ID
    @Override
    public boolean deleteStudent(int id) throws SQLException {

        String sql = "DELETE FROM students WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Pass the student ID to the SQL query
            ps.setInt(1, id);

            // Returns true if at least one record was deleted
            return ps.executeUpdate() > 0;
        }
    }

    // Convert a database row into a Student object
    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {

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