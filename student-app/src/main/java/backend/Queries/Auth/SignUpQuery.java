package backend.Queries.Auth;

import java.sql.SQLException;
import db.DbConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class SignUpQuery {

    public boolean register(String fullname, String email, String password) {
        String sql = "INSERT INTO users(fullname,email,password) VALUES(?,?,?)";

        try {
            Connection con = DbConnect.gConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, fullname);
            ps.setString(2, email);
            ps.setString(3, password);

            int rows = ps.executeUpdate();

            ps.close();
            con.close();

            if (rows > 0)
                return true;
            else
                return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

}
