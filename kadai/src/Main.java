import java.util.List;
import java.util.Scanner;

import dbmanager.DBManager;
import dbmanager.Twit;

import login.Login;
import signup.Signup;

public class Main {
    public static void main(String args[]) {
        Scanner scan = new Scanner(System.in); // 入力用Scannerクラス
        String name = "";
        System.out.println("Hello, World !");
        DBManager dbManager = DBManager.getInstance();
        boolean isContinue = true; // 継続するかどうか
        boolean isLogin = false; // ログインしているかどうか
        while (isContinue) {
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

                    
            }
        }
        
    }
}