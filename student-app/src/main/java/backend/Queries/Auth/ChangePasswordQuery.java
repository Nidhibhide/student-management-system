
package backend.Queries.Auth;

import db.DbConnect;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChangePasswordQuery {

    public String changePassword(
            int userId,
            String currentPassword,
            String newPassword) {

        String selectSql = "SELECT password FROM users WHERE id = ?";
        String updateSql = "UPDATE users SET password = ? WHERE id = ?";

        try (Connection connection = DbConnect.gConnection()) {

            // Verify current password.
            try (PreparedStatement statement =
                    connection.prepareStatement(selectSql)) {

                statement.setInt(1, userId);

                try (ResultSet resultSet = statement.executeQuery()) {

                    if (!resultSet.next()) {
                        return "USER_NOT_FOUND";
                    }

                    String storedPassword =
                            resultSet.getString("password");

                    if (!BCrypt.checkpw(currentPassword, storedPassword)) {
                        return "INVALID_CURRENT_PASSWORD";
                    }
                }
            }

            // Hash the new password.
            String hashedPassword = BCrypt.hashpw(
                    newPassword,
                    BCrypt.gensalt()
            );

            // Update password.
            try (PreparedStatement statement =
                    connection.prepareStatement(updateSql)) {

                statement.setString(1, hashedPassword);
                statement.setInt(2, userId);

                int rowsUpdated = statement.executeUpdate();

                return rowsUpdated > 0 ? "SUCCESS" : "FAILED";
            }

        } catch (SQLException | IllegalArgumentException e) {

            System.err.println("Change password failed: " + e.getMessage());
            return "ERROR";
        }
    }
}
