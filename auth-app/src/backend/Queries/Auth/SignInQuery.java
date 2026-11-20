package backend.Queries.Auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


import db.DbConnect;

public class SignInQuery {

    public boolean signIn(String email, String password) {

        String query = "SELECT * FROM users WHERE email=? AND password=?";

        try {
            Connection con = DbConnect.gConnection();

            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                con.close();
                return true;
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
