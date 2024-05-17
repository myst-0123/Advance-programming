package post;

import java.util.Scanner;
import dbmanager.DBManager;

public class Post {
    private static Post singletonPost = new Post();
    private Post(){};
    public static Post getInstance(){
        return singletonPost; //デザインパターン:Singleton(インスタンスを唯一に)
    }

    public void post(String name){ //twit投稿を行うメソッド
        Scanner scan = new Scanner(System.in); //入力用Scannerクラス

        System.out.print("twitの内容を入力してください：");
        String content = scan.next(); //twit内容

        (DBManager.getInstance()).post(name, content); //DB操作
    }
}