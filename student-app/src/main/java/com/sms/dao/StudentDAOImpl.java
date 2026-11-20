
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

            // Set values for each ? placeholder
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

            // Return true if at least one row was inserted
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

            // Read each row returned by the database
            while (rs.next()) {

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

                students.add(student);
            }
        }

        return students;
    }

    // SEARCH STUDENT BY ID
    @Override
    public Student searchStudentById(int id) throws SQLException {

        String sql = "SELECT * FROM students WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            // Pass the student ID into the SQL query
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

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

        return null;
    }

    // UPDATE STUDENT
    @Override
    public boolean updateStudent(Student student) throws SQLException {

        // SQL query to update all student details for the given ID
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

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            // Set the updated student details
            ps.setString(1, student.getFirstName());
            ps.setString(2, student.getLastName());
            ps.setString(3, student.getEmail());
            ps.setString(4, student.getMobile());
            ps.setString(5, student.getGender());
            ps.setDate(6, student.getDob());
            ps.setString(7, student.getAddress());
            ps.setString(8, student.getCourse());
            ps.setDate(9, student.getAdmissionDate());

            // The last placeholder is the ID of the student to update
            ps.setInt(10, student.getId());

            // executeUpdate() returns the number of affected rows
            int rowsUpdated = ps.executeUpdate();

            return rowsUpdated > 0;
        }
    }
}
