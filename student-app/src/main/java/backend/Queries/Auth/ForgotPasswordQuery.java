package backend.Queries.Auth;

import db.DbConnect;

import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

// Handles forgot password database operations.
public class ForgotPasswordQuery {

    // Check whether the entered email exists.
    public boolean emailExists(String email) {

        // SQL query to find the user by email.
        String sql = "SELECT id FROM users WHERE email = ?";

        // Connect to the database.
        try (Connection connection = DbConnect.gConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            // Replace ? with the entered email.
            statement.setString(1, email);

            // Execute SELECT query.
            try (ResultSet resultSet = statement.executeQuery()) {

                // If a record is found, the email exists.
                return resultSet.next();
            }

        } catch (SQLException e) {

            // Print database error.
            System.err.println("Email check failed: " + e.getMessage());

            return false;
        }
    }

    public boolean saveOTP(String email, String otp) {

        // SQL query to update OTP details for the user.
        String sql = "UPDATE users SET otp=? , otp_expires_at=? WHERE email=?";

        // Convert the OTP into a hashed value.
        String hashedOTP = BCrypt.hashpw(otp, BCrypt.gensalt(12));

        // Set OTP expiry time to 5 minutes from now.
        Timestamp expiryTime = new Timestamp(
                System.currentTimeMillis() + (5 * 60 * 1000));

        // Connect to the database.
        try (Connection connection = DbConnect.gConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            // Store hashed OTP.
            statement.setString(1, hashedOTP);

            // Store OTP expiry time.
            statement.setTimestamp(2, expiryTime);

            // Identify the user by email.
            statement.setString(3, email);

            // Execute UPDATE and check whether a row was updated.
            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            // Print database error.
            System.err.println("OTP saving failed: " + e.getMessage());

            return false;
        }
    }

    public boolean verifyOTP(String email, String enteredOTP) {

        // SQL query to get OTP details for the user.
        String sql = "SELECT otp,otp_expires_at FROM users WHERE  email=? ";

        // Connect to the database.
        try (Connection connection = DbConnect.gConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set the user's email.
            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {

                // If email is not found, return false.
                if (!resultSet.next()) {

                    return false;
                }
                // Get the hashed OTP from the database.
                String storedOTP = resultSet.getString("otp");

                // Get the OTP expiry time.
                Timestamp expiryTime = resultSet.getTimestamp("otp_expires_at");

                // Check whether OTP exists.
                if (storedOTP == null || expiryTime == null) {
                    return false;
                }

                // Check whether OTP has expired.
                if (expiryTime.before(new Timestamp(System.currentTimeMillis()))) {
                    return false;
                }

                // Compare entered OTP with the stored hashed OTP.
                return BCrypt.checkpw(enteredOTP, storedOTP);

            }

        } catch (SQLException | IllegalArgumentException e) {

            // Print database error.
            System.err.println("OTP verification failed: " + e.getMessage());

            return false;
        }

    }

    public boolean updatePassword(String email, String newPassword) {

        // SQL query to update OTP details for the user.
        String sql = "UPDATE users SET password=? , otp = NULL, otp_expires_at = NULL  WHERE email=?";

        // Hash the new password before saving it.
        String hashedPassword = BCrypt.hashpw(
                newPassword,
                BCrypt.gensalt(12));

        // Connect to the database.
        try (Connection connection = DbConnect.gConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set the new hashed password.
            statement.setString(1, hashedPassword);

            // Find the user using their email.
            statement.setString(2, email);

            // Execute UPDATE and check whether a row was updated.
            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            // Print database error.
            System.err.println("Password update failed: " + e.getMessage());

            return false;
        }
    }

}