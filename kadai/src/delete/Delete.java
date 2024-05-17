package delete;

import java.util.Scanner;
import dbmanager.DBManager;

public class Delete {
    private static Delete singletonDelete = new Delete();
    private Delete(){};
    public static Delete getInstance(){
        return singletonDelete; //デザインパターン:Singleton(インスタンスを唯一に)
    }

    public void delete(String name){ //twitを削除するメソッド
        Scanner scan = new Scanner(System.in); //入力用Scannerクラス

        System.out.print("削除するtwitのidを入力してください：");
        int deleteId = scan.nextInt(); //削除twitのid

        (DBManager.getInstance()).delete(name, deleteId); //DB操作
    }
}