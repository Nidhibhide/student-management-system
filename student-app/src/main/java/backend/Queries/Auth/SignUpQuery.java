package backend.Queries.Auth;

import db.DbConnect;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// Handles admin registration in the users table.
public class SignUpQuery {

    public boolean register(String fullname, String email, String password) {

        String sql = "INSERT INTO users (fullname, email, password) VALUES (?, ?, ?)";

        // Hash the password before storing it in MySQL.
        String hashedPassword = BCrypt.hashpw(
                password,
                BCrypt.gensalt(12)
        );

        try (Connection connection = DbConnect.gConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, fullname);
            statement.setString(2, email);
            statement.setString(3, hashedPassword);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            // A duplicate email will also be rejected by the database.
            System.err.println("Registration failed: " + e.getMessage());
            return false;
        }
    }
}
