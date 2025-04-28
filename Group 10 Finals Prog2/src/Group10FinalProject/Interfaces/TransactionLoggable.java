package Group10FinalProject.Interfaces;

import java.time.LocalDateTime;
import java.util.List;

// Interface for logging transactions
public interface TransactionLoggable {
    /**
     * Log a transaction
     * @param type The type of transaction (deposit, withdrawal, transfer, etc.)
     * @param amount The amount involved in the transaction
     * @param description Additional description of the transaction
     * @return A unique identifier for the transaction
     */
    String logTransaction(String type, double amount, String description);

    /**
     * Get transaction history for the account
     * @return List of transactions
     */
    List<Transaction> getTransactionHistory();

    /**
     * Get transactions within a date range
     * @param startDate Beginning of date range
     * @param endDate End of date range
     * @return List of transactions within the specified range
     */
    List<Transaction> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
}
