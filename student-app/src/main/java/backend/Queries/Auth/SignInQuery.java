
package backend.Queries.Auth;

import db.DbConnect;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

// This class checks whether the login details are correct.
public class SignInQuery {

    public int signIn(String email, String password) {

        // Find the user whose email matches the entered email.
        String sql = "SELECT id, password, failed_attempts, locked_until FROM users WHERE email = ?";

        // Reset failed attempts to 0 and remove the account lock.
        String resetAttempts = "UPDATE users SET failed_attempts = 0, locked_until = NULL WHERE id = ?";

        // Save the updated failed attempts and account lock time.
        String updateAttempts = "UPDATE users SET failed_attempts = ?, locked_until = ? WHERE id = ?";

        // Connect to the database and run the SELECT query.
        try (Connection connection = DbConnect.gConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Put the entered email in place of ?.
            statement.setString(1, email);

            // Run the SELECT query and get the result.
            try (ResultSet resultSet = statement.executeQuery()) {

                // Check if the entered email exists in the database.
                if (resultSet.next()) {

                    // Get the saved password from the database.
                    String storedPassword = resultSet.getString("password");

                    // Get the user's ID.
                    int userId = resultSet.getInt("id");

                    // Get how many times the user entered a wrong password.
                    int failedAttempts = resultSet.getInt("failed_attempts");

                    // Get the time until which the account is locked.
                    Timestamp lockedUntil = resultSet.getTimestamp("locked_until");

                    // Check if the account has a lock.
                    if (lockedUntil != null) {

                        // Check if the lock time is still active.
                        if (lockedUntil.after(new Timestamp(System.currentTimeMillis()))) {
                            return -2; // Account is still locked.
                        }

                        // The 15-minute lock is over.
                        // Reset the failed attempts in the database.
                        try (PreparedStatement reset = connection.prepareStatement(resetAttempts)) {
                            reset.setInt(1, userId);
                            reset.executeUpdate();
                        }

                        // Also reset the count in our Java variable.
                        failedAttempts = 0;
                    }

                    // Check whether the entered password matches the saved password.
                    if (BCrypt.checkpw(password, storedPassword)) {

                        // Password is correct, so reset failed attempts.
                        try (PreparedStatement reset = connection.prepareStatement(resetAttempts)) {
                            reset.setInt(1, userId);
                            reset.executeUpdate();
                        }

                        // Login successful. Return the user's ID.
                        return userId;
                    }

                    // Password is wrong, so increase the count by 1.
                    failedAttempts++;

                    // No lock time is set initially.
                    Timestamp lockTime = null;

                    // If the user enters the wrong password 5 times,
                    // lock the account for 15 minutes.
                    if (failedAttempts >= 5) {
                        lockTime = new Timestamp(System.currentTimeMillis() + (15 * 60 * 1000));
                    }

                    // Save the new failed-attempt count and lock time.
                    try (PreparedStatement update = connection.prepareStatement(updateAttempts)) {

                        // Save the updated failed-attempt count.
                        update.setInt(1, failedAttempts);

                        // Save the lock time (NULL if the account is not locked).
                        update.setTimestamp(2, lockTime);

                        // Tell MySQL which user's record to update.
                        update.setInt(3, userId);

                        // Run the UPDATE query.
                        update.executeUpdate();
                    }

                    // If 5 wrong attempts are reached, return -2.
                    if (failedAttempts >= 5) {
                        return -2;
                    }

                    // Password is wrong. Return -1.
                    return -1;
                }
            }

        } catch (SQLException | IllegalArgumentException e) {

            // Print the error if something goes wrong.
            System.err.println("Login failed: " + e.getMessage());
        }

        // Email was not found or an error occurred.
        return -1;
    }
}
