
// package com.sms.util;

// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.SQLException;

// public class DBConnection {

//     private static final String URL =
//             "jdbc:mysql://localhost:3306/student_management";

//     private static final String USER = "root";

//     private static final String PASSWORD = "MySQL@Student2026";

//     public static Connection getConnection() throws SQLException {
//         return DriverManager.getConnection(URL, USER, PASSWORD);
//     }
// }





package com.sms.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Database URL
    private static final String URL =
            "jdbc:mysql://localhost:3306/student_management";

    // MySQL username
    private static final String USER = "root";

    // Read password from Windows environment variable
    // This prevents the password from being uploaded to GitHub
    private static final String PASSWORD =
            System.getenv("SMS_DB_PASSWORD");

    // Establish connection with MySQL
    public static Connection getConnection() throws SQLException {

        // Check whether password is configured
        if (PASSWORD == null || PASSWORD.isBlank()) {
            throw new SQLException(
                    "Database password not configured. Set SMS_DB_PASSWORD first."
            );
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
