package Group10FinalProject;

import Group10FinalProject.Interfaces.Transaction;
import java.time.LocalDateTime;

public class TransactionImpl implements Transaction {
    private final String transactionId;
    private final int accountNumber;
    private final String type;
    private final double amount;
    private final String description;
    private final LocalDateTime timestamp;

    public TransactionImpl(String transactionId, int accountNumber, String type, double amount, String description) {
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public String getTransactionId() {
        return transactionId;
    }

    @Override
    public int getAccountNumber() {
        return accountNumber;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}