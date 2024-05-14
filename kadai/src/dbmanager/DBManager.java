package dbmanager;

import java.sql.*;
import java.util.*;
import java.util.Date;
import java.text.SimpleDateFormat;

public class DBManager {
    private static DBManager dbManager = new DBManager();
    private DBManager() { }
    public static DBManager getInstance() {
        return dbManager;
    }

    private List<Twit> ParseResult(ResultSet rs) {
        List<Twit> twitList = new ArrayList<Twit>();
        try {
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String content = rs.getString("content");
                String createdAt = rs.getString("created_at");
                Twit twit = new Twit(id, name, content, createdAt);
                twitList.add(twit);
            }
        } catch (Exception e) {e.printStackTrace(); return null;}

        return twitList;
    }

    public void Twit(String name, String content) {
        Connection con = null;
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createdAt = sdf.format(date);
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:../db/twitDB");
            PreparedStatement pstmt = con.prepareStatement("insert into twit(name, content) values(?, ?, ?)");
            pstmt.setString(1, name); //nameカラムに第1引数をセット
            pstmt.setString(2, content); //contentカラムに第2引数をセット
            pstmt.setString(3, createdAt);
            pstmt.executeUpdate(); //テーブル更新
            pstmt.close();
        } catch (Exception e) {e.printStackTrace();} 
    } 

    public List<Twit> getTwit() {
        Connection con = null;
        ResultSet rs = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:../db/twitDB");
            String sql = "SELECT * FROM twit ORDER BY created_at desc";
            PreparedStatement pStmt = con.prepareStatement(sql); 

            rs = pStmt.executeQuery();

            pStmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return ParseResult(rs);
    }

    public List<Twit> getTwit(String searchWord) {
        Connection con = null;
        ResultSet rs = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:../db/twitDB");
            
            PreparedStatement pStmt = con.prepareStatement("SELECT * FROM twit WHERE content LIKE '%?%' ORDER BY created_at desc");
            pStmt.setString(1, searchWord);

            rs = pStmt.executeQuery();
            pStmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return ParseResult(rs);
    }

    public void delete(String name, int deleteId) {
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:sqlite:../db/twitDB");
            PreparedStatement pstmt1 = con.prepareStatement("select * from twit where id = ?");
            pstmt1.setInt(1, deleteId);
            ResultSet rs = pstmt1.executeQuery(); //deleteId番目のtwitを参照
            while(rs.next()){
                if((rs.getString("name")).equals(name)){ //deleteId番目のtwitのnameとプログラム実行者のアカウント名が一致していれば
                    PreparedStatement pstmt2 = con.prepareStatement("delete from twit where id = ?");
                    pstmt2.setInt(1, deleteId); //idがdeleteIdのtwitを
                    pstmt2.executeUpdate(); //削除
                    pstmt2.close();
                }else{
                    System.out.println("！そのtwitはあなたのものではありません！");
                }
            }
            
            rs.close();
            pstmt1.close();
        } catch (Exception e) {e.printStackTrace();}
    }
}


