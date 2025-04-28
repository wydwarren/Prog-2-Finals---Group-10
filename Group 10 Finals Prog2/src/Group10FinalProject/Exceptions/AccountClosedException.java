package Group10FinalProject.Exceptions;

// Thrown when operations are attempted on a closed account
public class AccountClosedException extends BankingException {
    private final int accountNumber;

    public AccountClosedException(int accountNumber) {
        super("Account #" + accountNumber + " is closed");
        this.accountNumber = accountNumber;
    }

    public int getAccountNumber() {
        return accountNumber;
    }
}
