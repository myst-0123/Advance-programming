package signup;

import java.util.Scanner;
import dbmanager.DBManager;

public class Signup {
    private static Signup singletonSignup = new Signup();

    private Signup() {
    };

    public static Signup getInstance() {
        return singletonSignup; // デザインパターン:Singleton(インスタンスを唯一に)
    }

    public void signup() { // アカウントを作成するメソッド
        Scanner scan = new Scanner(System.in); // 入力用Scannerクラス

        System.out.print("設定するアカウント名を入力してください：");
        String name = scan.next(); // プログラム実行者のアカウント名
        System.out.print("設定するパスワードを入力してください：");
        String password = scan.next(); // プログラム実行者のパスワード

        DBManager.getInstance().signup(name, password); // DB操作
    }
}
