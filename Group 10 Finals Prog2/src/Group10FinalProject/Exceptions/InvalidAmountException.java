package Group10FinalProject.Exceptions;

// Thrown when an invalid amount is provided (negative, zero, etc.)
public class InvalidAmountException extends BankingException {
    private final double amount;

    public InvalidAmountException(double amount) {
        super("Invalid amount: ₱" + amount + ". Amount must be positive");
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }
}
