package Group10FinalProject.Exceptions;

// Thrown when an account has insufficient funds for a transaction
public class InsufficientFundsException extends BankingException {
    private final double requestedAmount;
    private final double availableBalance;

    public InsufficientFundsException(double requestedAmount, double availableBalance) {
        super("Insufficient funds: Requested ₱" + requestedAmount +
                " but only ₱" + availableBalance + " available");
        this.requestedAmount = requestedAmount;
        this.availableBalance = availableBalance;
    }

    public double getRequestedAmount() {
        return requestedAmount;
    }

    public double getAvailableBalance() {
        return availableBalance;
    }
}
