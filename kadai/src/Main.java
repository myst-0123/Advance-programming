import java.util.List;
import java.util.Scanner;

import dbmanager.DBManager;
import dbmanager.Twit;
import delete.Delete;
import login.Login;
import signup.Signup;
import search.Search;
import timeline.*;
import post.Post;

public class Main {
    public static void main(String args[]) {
        Scanner scan = new Scanner(System.in); // 入力用Scannerクラス
        String name = "";
        boolean isContinue = true; // 継続するかどうか
        boolean isLogin = false; // ログインしているかどうか
        while (!isLogin && isContinue) { // ログインしていない場合
            System.out.println("動作を選択肢番号から選んでください(1.アカウント作成 / 2.ログイン / 3.検索 / 4.タイムライン / 5.終了)");
            int number = scan.nextInt(); // 選択肢番号入力
            switch (number) {
                case 1:
                    Signup sign = Signup.getInstance();
                    sign.signup();
                    break;
                case 2:
                    Login log = Login.getInstance();
                    String loginName = log.login();
                    if (loginName != null) {
                        name = loginName;
                        isLogin = true;
                    }
                    break;
                case 3:
                    Search sear = Search.getInstance();
                    sear.search();
                    break;
                case 4:
                    ShowTimeLineCL time = new ShowTimeLineCL(10, 10);
                    time.ShowTimeLine();
                    break;
                case 5:
                    isContinue = false;
            }
        }
        while (isLogin && isContinue) { // ログインしている場合
            System.out.println("アカウント名" + name + "でログイン中");
            System.out.println("動作を選択肢番号から選んでください(1.投稿 / 2.削除 / 3.検索 / 4.タイムライン / 5.終了)");
            int number = scan.nextInt(); // 選択肢番号入力
            switch (number) {
                case 1:
                    Post pos = Post.getInstance();
                    pos.post(name);
                    break;
                case 2:
                    Delete del = Delete.getInstance();
                    del.delete(name);
                    break;
                case 3:
                    Search sear = Search.getInstance();
                    sear.search();
                    break;
                case 4:
                    ShowTimeLineCL time = new ShowTimeLineCL(10, 10);
                    time.ShowTimeLine();
                    break;
                case 5:
                    isContinue = false;
            }
        }

    }
}