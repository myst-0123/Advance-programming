import java.util.*;

import java.sql.*;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import dbmanager.DBManager;
import dbmanager.Twit;
import delete.Delete;
import login.Login;
import signup.Signup;
import search.Search;
import timeline.*;
import post.Post;

@FixMethodOrder (MethodSorters.NAME_ASCENDING) // テストメソッドをメソッド名の辞書順に実行する
public class TwitTest {
    @Test public void test01_signupTest(){ // サインアップに関するテスト
        Signup signup = Signup.getInstance();
        signup.signup(); // サインアップ
        System.out.println();

        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:../db/twitDB.db");
            PreparedStatement pstmt = con.prepareStatement("select * from account");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) { // DB参照
                System.out.println(rs.getString("id") + ":" + rs.getString("name") + ":" + rs.getString("password"));
            }
            rs.close();
            pstmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test public void test02_loginTest(){ // ログインに関するテスト
        Login login = Login.getInstance();
        System.out.println(login.login() + "\n"); // ログインした人のアカウント名を出力
    }

    @Test public void test03_timelineTest(){ //タイムラインに関するテスト
        ShowTimeLineCL showtimelinecl = new ShowTimeLineCL(10, 10);
        showtimelinecl.ShowTimeLine(); // タイムライン出力
        System.out.println();

        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:../db/twitDB.db");
            PreparedStatement pstmt = con.prepareStatement("select * from twit");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) { // DB参照
                System.out.println(rs.getString("id") + ":" + rs.getString("name") + ":" + rs.getString("content") + ":" + rs.getString("created_at"));
            }
            rs.close();
            pstmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    @Test public void test04_postTest(){ // 投稿に関するテスト
        Post post = Post.getInstance();
        System.out.print("(In test)Enter name:");
        Scanner scan = new Scanner(System.in); // 入力用Scannerクラス
        String expected_name = scan.nextLine(); // 投稿者のアカウント名
        post.post(expected_name); // 投稿

        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:../db/twitDB.db");
            PreparedStatement pstmt = con.prepareStatement("select * from twit");
            ResultSet rs = pstmt.executeQuery();
            String actual_name = null;
            while (rs.next()) { // DB参照
                System.out.println(rs.getString("id") + ":" + rs.getString("name") + ":" + rs.getString("content") + ":" + rs.getString("created_at"));
                actual_name = rs.getString("name"); // 最新の投稿を行ったアカウントの名前
            }
            assertEquals(expected_name, actual_name); // 比較
            rs.close();
            pstmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    @Test public void test05_deleteTest(){ // 削除に関するテスト
        Delete delete = Delete.getInstance();
        System.out.print("(In test)Enter name:");
        Scanner scan = new Scanner(System.in); // 入力用Scannerクラス
        String name = scan.nextLine(); // 削除する投稿のアカウント名
        delete.delete(name); // 削除

        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:../db/twitDB.db");
            PreparedStatement pstmt = con.prepareStatement("select * from twit");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) { // DB参照
                System.out.println(rs.getString("id") + ":" + rs.getString("name") + ":" + rs.getString("content") + ":" + rs.getString("created_at"));
            }
            rs.close();
            pstmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    @Test public void test06_searchTest(){ // 検索に関するテスト
        Search search = Search.getInstance();
        search.search(); // 検索

        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:../db/twitDB.db");
            PreparedStatement pstmt = con.prepareStatement("select * from twit");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) { // DB参照
                System.out.println(rs.getString("id") + ":" + rs.getString("name") + ":" + rs.getString("content") + ":" + rs.getString("created_at"));
            }
            rs.close();
            pstmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
    }
}