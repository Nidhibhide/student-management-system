// package backend.Queries.Auth;

// import java.sql.Connection;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;


// import db.DbConnect;

// public class SignInQuery {

//     public boolean signIn(String email, String password) {

//         String query = "SELECT * FROM users WHERE email=? AND password=?";

//         try {
//             Connection con = DbConnect.gConnection();

//             PreparedStatement ps = con.prepareStatement(query);

//             ps.setString(1, email);
//             ps.setString(2, password);

//             ResultSet rs = ps.executeQuery();

//             if (rs.next()) {
//                 con.close();
//                 return true;
//             }
//             con.close();
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//         return false;
//     }
// }





package backend.Queries.Auth;

import db.DbConnect;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Checks admin login credentials.
public class SignInQuery {

    public boolean signIn(String email, String password) {

        String sql = "SELECT password FROM users WHERE email = ?";

        try (Connection connection = DbConnect.gConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    String storedPassword = resultSet.getString("password");

                    // Compare the entered password with the stored BCrypt hash.
                    return BCrypt.checkpw(password, storedPassword);
                }
            }

        } catch (SQLException | IllegalArgumentException e) {

            System.err.println("Login failed: " + e.getMessage());
        }

        return false;
    }
}
