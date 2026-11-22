
package db;

import com.sms.util.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;

// Authentication and Student modules share the same database connection.
public class DbConnect {

    // Keep the existing method name so the authentication queries
    // can use this class without changing their connection calls.
    public static Connection gConnection() throws SQLException {
        return DBConnection.getConnection();
    }

    // Test the database connection independently.
    public static void main(String[] args) {

        try (Connection connection = gConnection()) {

            System.out.println("Database Connected Successfully!");

        } catch (SQLException e) {

            System.out.println("Database Connection Failed!");
            e.printStackTrace();
        }
    }
}
