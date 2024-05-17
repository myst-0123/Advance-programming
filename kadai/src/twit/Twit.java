package twit;

import java.util.Scanner;
import dbmanager.DBManager;

public class Twit {
    private static Twit singletonTwit = new Twit();
    private Twit(){};
    public static Twit getInstance(){
        return singletonTwit; //デザインパターン:Singleton(インスタンスを唯一に)
    }

    public void twit(String name){ //twitを行うメソッド
        Scanner scan = new Scanner(System.in); //入力用Scannerクラス

        System.out.print("twitの内容を入力してください：");
        String content = scan.next(); //twit内容

        (DBManager.getInstance()).twit(name, content); //DB操作
    }
}