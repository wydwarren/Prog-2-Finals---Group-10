package Group10FinalProject.Interfaces;

import java.time.LocalDateTime;

public interface Transaction {
    String getTransactionId();
    int getAccountNumber();
    String getType();
    double getAmount();
    String getDescription();
    LocalDateTime getTimestamp();
}