package DBManager;

import java.sql.*;

public class DBManager {
    private static DBManager dbManager = new DBManager();
    private Connection con;
    private DBManager() { }
    public static DBManager getInstance() {
        return dbManager;
    }

    public void Initialization() {
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:../twitDB");
        } catch (Exception e) {e.printStackTrace();}
    }

    public void Close() {
        try {
            con.close();
        } catch (Exception e) {e.printStackTrace();}
    }
}
