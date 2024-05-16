package twit;

import java.sql.PreparedStatement;
import java.util.Scanner;
import dbmabager.DBManager;

public class Twit {
    private static Twit singletonTwit = new Twit();
    private Twit(){};
    public static Twit getInstance(){
        return singletonTwit; //デザインパターン:Singleton(インスタンスを唯一に)
    }

    public void twit(String name){ //twitを行うメソッド
        Scanner scan = new Scanner(System.in); //入力用Scannerクラス

        // System.out.print("twitするアカウントの名前を入力してください：");
        // String name = scan.next(); //twitする人のアカウント名
        System.out.print("twitの内容を入力してください：");
        String content = scan.next(); //twit内容

        (DBManager.getInstance()).twit(name, content); //DB操作

        scan.close();
    }
}

// public void twit(String name, String content){ //twitを行うメソッド
//     PreparedStatement pstmt = con.prepareStatement("insert into twit(name, content) values(?, ?)");
//     pstmt.setString(1, name); //nameカラムに第1引数をセット
//     pstmt.setString(2, content); //contentカラムに第2引数をセット
//     pstmt.executeUpdate(); //テーブル更新
//     pstmt.close();
// }