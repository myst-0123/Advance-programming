package Report.Lecture3;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DBTest {
    public void insert() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection con = null;
            con = DriverManager.getConnection("jdbc:sqlite:test.db");

            PreparedStatement pstmt = con
                    .prepareStatement("insert into message(id,name,content,create_at) VALUES(?,?,?,?)");
            //pstmt.setInt(1, 2);
            pstmt.setString(2, "Ken");
            pstmt.setString(3, "こんにちは");
            // 現在日時を取得
            LocalDateTime nowDate = LocalDateTime.now();
            System.out.println(nowDate); // 2020-12-20T13:32:48

            // 表示形
            DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");// ①
            String formatNowDate = dtf1.format(nowDate);
            System.out.println(formatNowDate); // 2020/12/20 13:32:48.293

            pstmt.setString(4, formatNowDate);
            pstmt.executeUpdate();

            pstmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}