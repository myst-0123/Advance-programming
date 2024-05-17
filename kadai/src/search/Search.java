package search;

import java.util.List;
import java.util.Scanner;

import dbmanager.DBManager;
import dbmanager.Twit;

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
        String word = scan.next(); // 検索word
        List<Twit> tl_list = DBManager.getInstance().getTwit(word); // DB操作
        // Twitsを表示
        for (Twit tw : tl_list) {
            System.out.println("------------------------------------------------------------------");
            System.out.println(tw.id + ". " + tw.name);
            System.out.println(tw.createdAt);
            System.out.println(tw.content);
        }
    }
}