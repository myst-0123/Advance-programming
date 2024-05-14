package delete;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;
import dbmabager.DBManager;

public class Delete {
    private static Delete singletonDelete = new Delete();
    private Delete(){};
    public static Delete getInstance(){
        return singletonDelete; //デザインパターン:Singleton(インスタンスを唯一に)
    }

    public void delete(){ //twitを削除するメソッド
        Scanner scan = new Scanner(System.in); //入力用Scannerクラス

        System.out.print("あなたのアカウント名を入力してください。");
        String name = scan.next(); //プログラム実行者のアカウント名
        System.out.print("削除するtwitのidを入力してください：");
        int deleteId = scan.nextInt(); //削除twitのid

        (DBManager.getInstance()).delete(name, deleteId); //DB操作

        scan.close();
    }
}

// public void delete(String name, int deleteId){ //twitを削除するメソッド
//     PreparedStatement pstmt1 = con.prepareStatement("select * from twit where id = ?");
//     pstmt1.setInt(1, deleteId);
//     ResultSet rs = pstmt1.executeQuery(); //deleteId番目のtwitを参照
//     while(rs.next()){
//         if((rs.getString("name")).equals(name)){ //deleteId番目のtwitのnameとプログラム実行者のアカウント名が一致していれば
//             PreparedStatement pstmt2 = con.prepareStatement("delete from twit where id = ?");
//             pstmt2.setInt(1, deleteId); //idがdeleteIdのtwitを
//             pstmt2.executeUpdate(); //削除
//             pstmt2.close();
//         }else{
//             System.out.println("！そのtwitはあなたのものではありません！");
//         }
//     }
//     rs.close();
//     pstmt1.close();
// }