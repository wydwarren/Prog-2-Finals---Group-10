package Group10FinalProject.Exceptions;

// Thrown when an account number doesn't exist or cannot be found
public class InvalidAccountException extends BankingException {
    private final int accountNumber;

    public InvalidAccountException(int accountNumber) {
        super("Invalid account number: " + accountNumber);
        this.accountNumber = accountNumber;
    }

    public int getAccountNumber() {
        return accountNumber;
    }
}
