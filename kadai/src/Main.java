import java.util.List;
import java.util.Scanner;

import dbmanager.DBManager;
import dbmanager.Twit;

import login.Login;
import signup.Signup;
import search.Search;

public class Main {
    public static void main(String args[]) {
        Scanner scan = new Scanner(System.in); // 入力用Scannerクラス
        String name = "";
        System.out.println("Hello, World !");
        DBManager dbManager = DBManager.getInstance();
        boolean isContinue = true; // 継続するかどうか
        boolean isLogin = false; // ログインしているかどうか
        while (!isLogin && isContinue) {
            System.out.println("動作を選択肢番号から選んでください");
            int number = scan.nextInt();
            switch (number) {
                case 1:
                    Signup sign = Signup.getInstance();
                    sign.signup();
                    break;
                case 2:
                    Login log = Login.getInstance();
                    log.login();
                    break;
                case 3:
                    Search sear = Search.getInstance();
                    sear.search();

                    
         

        
    }
}