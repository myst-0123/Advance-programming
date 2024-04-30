// public class AccountTest {
//     public static void main(String[] args) {
//         testInstantiate();
//         testTransfer();
//     }

//     private static void testInstantiate() {
//         System.out.println("newできるかテスト");
//         Account a = new Account("ミナト", 30000);
//         if (!"ミナト".equals(a.owner)) {
//             System.out.println("失敗！");
//         }
//         if (30000 != a.balance)

//         {
//             System.out.println("失敗！");
//         }
//     }

//     private static void testTransfer() {
//         Account a = new Account("ミナト", 30000);
//         Account b = new Account("ミナト", 30000);
//         a.transfer(b, 10);
//         if (20090 != a.balance && 30010 != b.balance)

//         {
//             System.out.println("失敗！");
//         }
//     }
// }

import org.junit.Test;
import static org.junit.Assert.*;

public class AccountTest {
    @Test
    public void instantiate() {
        Account a = new Account("ミナト", 30000);
        assertEquals("ミナト", a.owner);
        assertEquals(30000, a.balance);
    }

    @Test
    public void transfer() {
    }
}
