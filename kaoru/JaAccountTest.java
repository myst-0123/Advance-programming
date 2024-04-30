import org.junit.Test;
import static org.junit.Assert.*;

public class JaAccountTest {
    @Test
    public void instantiate() {
        JaAccount a = new JaAccount("ミナト", 30000);
        assertEquals("ミナト", a.owner);
        assertEquals(30000, a.balance);
    }

    @Test
    public void deposit() {
        JaAccount a = new JaAccount("ミナト", 30000);
        a.deposit(10000);
        assertEquals(40000, a.balance);
    }

    @Test
    public void payout() {
        JaAccount a = new JaAccount("ミナト", 30000);
        a.payout(10000);
        assertEquals(20000, a.balance);
    }

    @Test
    public void transfer() {
        JaAccount a = new JaAccount("ミナト", 30000);
        JaAccount b = new JaAccount("ミナト", 30000);
        a.transfer(b, 10000);
        assertEquals(40000, b.balance);
        assertEquals(19900, a.balance);
    }
}
