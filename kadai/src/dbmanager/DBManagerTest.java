package dbmanager;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class DBManagerTest {
    @Test public void twit() {
        DBManager dbManager = DBManager.getInstance();
        dbManager.twit("Hida", "I am a pen.");
    }
    @Test public void getTwit() {
        DBManager dbManager = DBManager.getInstance();
        List<Twit> twitList = dbManager.getTwit();
        assertNotNull(twitList);
        twitList = dbManager.getTwit("I");
        assertNotNull(twitList);
    }
}
