package login;

import java.util.Scanner;
import dbmanager.DBManager;

public class Login {
    private static Login singletonLogin = new Login();

    private Login() {
    };

    public static Login getInstance() {
        return singletonLogin; // デザインパターン:Singleton(インスタンスを唯一に)
    }

    public String login() { // ログインするメソッド
        Scanner scan = new Scanner(System.in); // 入力用Scannerクラス
        System.out.print("あなたのアカウント名を入力してください：");
        String name = scan.next(); // プログラム実行者のアカウント名
        System.out.print("あなたのパスワードを入力してください：");
        String password = scan.next(); // プログラム実行者のパスワード
        if (DBManager.getInstance().login(name, password)) // ログイン成功
            return name;
        else // ログイン失敗
            return null;
    }
}
