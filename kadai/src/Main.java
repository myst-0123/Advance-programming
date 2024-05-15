import dbmanager.DBManager;

public class Main {
    public static void main(String args[]) {
        System.out.println("Hello, World !");
        DBManager dbManager = DBManager.getInstance();
        dbManager.signin("kaoru", "14037");
        System.out.println(dbManager.login("kaoru", "14037"));
    }
}