public class JaAccount {
    public String owner;
    public int balance;
    final int COMMITION = 100;

    public JaAccount(String owner, int balance) {
        this.owner = owner;
        this.balance = balance;
    }

    public void deposit(int amount) {
        this.balance += amount;
    }

    public void payout(int amount) {
        if (balance >= amount)
            this.balance -= amount;
    }

    public void transfer(JaAccount dest, int amount) {
        if (balance >= amount) {
            dest.balance += amount;
            balance -= amount + COMMITION;
        }

    }
}
