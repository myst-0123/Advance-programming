package login;

import java.util.Scanner;
import dbManager.DBManager;

public class Login {
    private static Login singletonLogin = new Login();

    private Login() {
    };

    public static Login getInstance() {
        return singletonLogin; // デザインパターン:Singleton(インスタンスを唯一に)
    }

    public String login() { // ログインするメソッド
        Scanner scan = new Scanner(System.in); // 入力用Scannerクラス
        String name, password;
        do {
            System.out.print("あなたのアカウント名を入力してください：");
            name = scan.next(); // プログラム実行者のアカウント名
            System.out.print("あなたのパスワードを入力してください：");
            password = scan.next(); // プログラム実行者のパスワード
        } while (DBManager.getInstance().login(name, password)); // ログインが完了するまで

        scan.close();
        return name;
    }
}
