package dbmanager;

import java.sql.*;
import java.util.*;
import java.util.Date;
import java.text.SimpleDateFormat;

public class DBManager {
    private static DBManager dbManager = new DBManager();

    private DBManager() {
    }

    public static DBManager getInstance() {
        return dbManager;
    }

    // クエリ結果の整形
    private List<Twit> ParseResult(ResultSet rs) throws SQLException {
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
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return twitList;
    }

    // ツイットの作成
    public void post(String name, String content) {
        Connection con = null;

        // 作成日時の取得とフォーマットの整形
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createdAt = sdf.format(date);

        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:../db/twitDB.db");
            PreparedStatement pstmt = con
                    .prepareStatement("insert into twit(name, content,created_at) values(?, ?, ?)");
            pstmt.setString(1, name); // nameカラムに第1引数をセット
            pstmt.setString(2, content); // contentカラムに第2引数をセット
            pstmt.setString(3, createdAt); // 作成日時をセット
            pstmt.executeUpdate(); // テーブル更新
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // データの取得
    public List<Twit> getTwit() {
        Connection con = null;
        ResultSet rs = null;
        List<Twit> val = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:../db/twitDB.db");
            String sql = "SELECT * FROM twit ORDER BY created_at desc";
            PreparedStatement pStmt = con.prepareStatement(sql);

            rs = pStmt.executeQuery();
            val = ParseResult(rs);
            pStmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return val;
    }

    // 単語検索ありのデータの取得
    public List<Twit> getTwit(String searchWord) {
        Connection con = null;
        ResultSet rs = null;
        List<Twit> val = new ArrayList<>();
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:../db/twitDB.db");

            // 文字列searchWordがcontentカラムまたはnameカラムに含まれているデータを取得
            PreparedStatement pStmt = con
                    .prepareStatement(
                            "SELECT * FROM twit WHERE content LIKE ? OR name LIKE ? ORDER BY created_at desc");
            pStmt.setString(1, "%" + searchWord + "%");
            pStmt.setString(2, "%" + searchWord + "%");
            rs = pStmt.executeQuery();
            val = ParseResult(rs);
            rs.close();
            pStmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return val;
    }

    // ツイットの削除
    public void delete(String name, int deleteId) {
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:sqlite:../db/twitDB.db");
            PreparedStatement pstmt1 = con.prepareStatement("select * from twit where id = ?");
            pstmt1.setInt(1, deleteId);
            ResultSet rs = pstmt1.executeQuery(); // deleteId番目のtwitを参照
            boolean isExist = false; // deleteId番目のtwitが存在するかどうか
            while (rs.next()) {
                isExist = true;
                if ((rs.getString("name")).equals(name)) { // deleteId番目のtwitのnameとプログラム実行者のアカウント名が一致していれば
                    PreparedStatement pstmt2 = con.prepareStatement("delete from twit where id = ?");
                    pstmt2.setInt(1, deleteId); // idがdeleteIdのtwitを
                    pstmt2.executeUpdate(); // 削除
                    pstmt2.close();
                } else {
                    System.out.println("！そのtwitはあなたのものではありません！");
                }
            }
            if(!isExist) System.out.println("そのようなtwitは存在しません。");

            rs.close();
            pstmt1.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // アカウント新規作成
    public void signup(String name, String password) {
        Connection con = null;

        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:../db/twitDB.db");
            PreparedStatement pstmt1 = con.prepareStatement("select * from account where password = ?");
            pstmt1.setString(1, password);
            ResultSet rs = pstmt1.executeQuery(); // passwordが一致するアカウントを参照
            while (rs.next()) {
                if ((rs.getString("name")).equals(name)) { // passwordが一致するアカウントのnameとプログラム実行者のアカウント名が一致していれば
                    System.out.println("！既に同じアカウントが存在します！");
                    pstmt1.close();
                    return;
                }
            }
            pstmt1.close();
            PreparedStatement pstmt2 = con.prepareStatement("insert into account(name, password) values(?, ?)");
            pstmt2.setString(1, name); // nameカラムに第1引数をセット
            pstmt2.setString(2, password); // passwordカラムに第2引数をセット
            pstmt2.executeUpdate(); // テーブル更新
            pstmt2.close();
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ログイン
    public boolean login(String name, String password) {
        Connection con = null;
        ResultSet rs = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:../db/twitDB.db");
            PreparedStatement pstmt1 = con.prepareStatement("select * from account where password = ?");
            pstmt1.setString(1, password);
            rs = pstmt1.executeQuery(); // passwordが一致するアカウントを参照

            while (rs.next()) {
                if (rs.getString("name").equals(name)) {
                    pstmt1.close();
                    return true;
                }
            }
            pstmt1.close();
            con.close();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
