import java.sql.*;

import org.junit.Test;
import static org.junit.Assert.*;

import dbmanager.DBManager;
import dbmanager.Twit;
import delete.Delete;
import login.Login;
import signup.Signup;
import search.Search;
import timeline.*;
import post.Post;

public class TwitTest {
    @Test public void signupTest(){
        Signup signup = Signup.getInstance();
        for (int i = 0; i < 5; i++) {
            signup.signup();   
        }

        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:../db/twitDB.db");
            PreparedStatement pstmt = con.prepareStatement("select * from account");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString("id") + ":" + rs.getString("name") + ":" + rs.getString("password"));
            }
            rs.close();
            pstmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test public void loginTest(){
        Login login = Login.getInstance();
        for (int i = 0; i < 3; i++) {
            System.out.println(login.login());
        }
    }
}