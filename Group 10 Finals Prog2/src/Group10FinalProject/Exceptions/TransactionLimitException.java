package Group10FinalProject.Exceptions;

// Thrown when a transaction exceeds the daily/monthly limit
public class TransactionLimitException extends BankingException {
    private final double requestedAmount;
    private final double limitAmount;
    private final String limitType;

    public TransactionLimitException(double requestedAmount, double limitAmount, String limitType) {
        super("Transaction limit exceeded: Requested ₱" + requestedAmount +
                " but " + limitType + " limit is ₱" + limitAmount);
        this.requestedAmount = requestedAmount;
        this.limitAmount = limitAmount;
        this.limitType = limitType;
    }

    public double getRequestedAmount() {
        return requestedAmount;
    }

    public double getLimitAmount() {
        return limitAmount;
    }

    public String getLimitType() {
        return limitType;
    }
}
