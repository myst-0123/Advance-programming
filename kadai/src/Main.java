import java.util.List;

import dbmanager.DBManager;
import dbmanager.Twit;

public class Main {
    public static void main(String args[]) {
        String name = "";
        System.out.println("Hello, World !");
        DBManager dbManager = DBManager.getInstance();
        dbManager.signin("kaoru", "14037");
        if (dbManager.login("kaoru", "14037"))
            name = "kaoru";
        System.out.println(name);
        dbManager.Twit(name, "テスト");
        List<Twit> rs = dbManager.getTwit();
        for (Twit r : rs) {
            System.out.println(r.id + r.name + r.content + r.createdAt);
        }
    }
}