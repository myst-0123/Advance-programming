package search;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Search {
    private static Search singletonSearch = new Search();

    private Search() {
    };

    public static Search getInstance() {
        return singletonSearch; // デザインパターン:Singleton(インスタンスを唯一に)
    }

    public void search() { // twitを検索するメソッド
        Scanner scan = new Scanner(System.in); // 入力用Scannerクラス

        System.out.print("検索する言葉を入力してください：");
        String word = scan.next(); // 削除twitのid

        DBManager.getTwit(word); // DB操作

        scan.close();
    }
}