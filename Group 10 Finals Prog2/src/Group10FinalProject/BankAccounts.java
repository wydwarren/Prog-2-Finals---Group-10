package Group10FinalProject;

import Group10FinalProject.Exceptions.*;
import Group10FinalProject.Interfaces.AccountVerifiable;
import Group10FinalProject.Interfaces.Transaction;
import Group10FinalProject.Interfaces.TransactionLoggable;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

// Updated class implementing the required interfaces
public class BankAccounts implements TransactionLoggable, AccountVerifiable {
    // Account details
    private int accountNo;  // Unique account number for identification
    private String accountName; // Name associated with the account
    private double balance; // Current account balance
    private String status; // Status of the account (e.g., Active/Closed)
    private String pin;
    private List<Transaction> transactionHistory;
    private static final double DAILY_WITHDRAWAL_LIMIT = 50000.0; // Example limit

    // Constructor to initialize account details with default balance and active status
    public BankAccounts(int accountNo, String accountName, String pin) {
        this.accountNo = accountNo;
        this.accountName = accountName;
        this.balance = 0.0; // Default starting balance is zero
        this.status = "Active"; // Default status is "Active"
        this.pin = pin;
        this.transactionHistory = new ArrayList<>();
    }

    // Getter for account number
    public int getAccountNo() {
        return accountNo;
    }

    // Getter for account name
    public String getAccountName() {
        return accountName;
    }

    // Getter for account status (e.g., Active/Closed)
    public String getStatus() {
        return status;
    }

    public String getPin() {
        return pin;
    }

    // Updated deposit method with exception handling
    public void deposit(double amount) throws InvalidAmountException, AccountClosedException {
        // Check if account is active
        if (!status.equals("Active")) {
            throw new AccountClosedException(accountNo);
        }

        // Check if amount is valid
        if (amount <= 0) {
            throw new InvalidAmountException(amount);
        }

        balance += amount;
        logTransaction("DEPOSIT", amount, "Deposited ₱" + amount);
    }

    // Updated withdraw method with exception handling
    public void withdraw(double amount) throws InsufficientFundsException, InvalidAmountException,
            AccountClosedException, TransactionLimitException {
        // Check if account is active
        if (!status.equals("Active")) {
            throw new AccountClosedException(accountNo);
        }

        // Check if amount is valid
        if (amount <= 0) {
            throw new InvalidAmountException(amount);
        }

        // Check if there's sufficient balance
        if (amount > balance) {
            throw new InsufficientFundsException(amount, balance);
        }

        // Check transaction limits
        if (amount > DAILY_WITHDRAWAL_LIMIT) {
            throw new TransactionLimitException(amount, DAILY_WITHDRAWAL_LIMIT, "daily withdrawal");
        }

        balance -= amount;
        logTransaction("WITHDRAWAL", amount, "Withdrew ₱" + amount);
    }

    // Backward compatibility wrapper for withdraw
    public String withdraw(double amount, boolean legacyMethod) {
        try {
            withdraw(amount);
            return "Withdrawal successful: " + amount;
        } catch (InsufficientFundsException e) {
            return "Withdrawal failed: Insufficient balance.";
        } catch (AccountClosedException e) {
            return "Withdrawal failed: Account is closed.";
        } catch (InvalidAmountException e) {
            return "Withdrawal failed: Invalid amount.";
        } catch (TransactionLimitException e) {
            return "Withdrawal failed: Transaction limit exceeded.";
        }
    }

    public double inquireBalance() {
        return balance;
    }

    // Updated transfer method with exception handling
    public void transfer(BankAccounts targetAccount, double amount) throws InsufficientFundsException,
            InvalidAmountException,
            AccountClosedException,
            InvalidAccountException,
            TransactionLimitException {
        // Check if source account is active
        if (!status.equals("Active")) {
            throw new AccountClosedException(accountNo);
        }

        // Check if target account is valid
        if (targetAccount == null) {
            throw new InvalidAccountException(0);
        }

        // Check if target account is active
        if (!targetAccount.getStatus().equals("Active")) {
            throw new AccountClosedException(targetAccount.getAccountNo());
        }

        // Withdraw from source account (all necessary checks are done in withdraw method)
        withdraw(amount);

        // Deposit to target account
        try {
            targetAccount.deposit(amount);
            logTransaction("TRANSFER", amount, "Transferred ₱" + amount + " to Account #" + targetAccount.getAccountNo());
            targetAccount.logTransaction("TRANSFER", amount, "Received ₱" + amount + " from Account #" + accountNo);
        } catch (Exception e) {
            // If deposit fails, refund the amount back to source account
            try {
                this.deposit(amount);
                logTransaction("REFUND", amount, "Refunded ₱" + amount + " due to failed transfer");
            } catch (Exception ex) {
                // This should not happen, but just in case, log the error
                System.err.println("Critical error: Failed to refund after failed transfer");
            }
            throw e;
        }
    }

    // Backward compatibility wrapper for transfer
    public String transfer(BankAccounts targetAccount, double amount, boolean legacyMethod) {
        try {
            transfer(targetAccount, amount);
            return "Transfer successful: " + amount;
        } catch (InsufficientFundsException e) {
            return "Transfer failed: Insufficient balance.";
        } catch (AccountClosedException e) {
            return "Transfer failed: Account is closed.";
        } catch (InvalidAmountException e) {
            return "Transfer failed: Invalid amount.";
        } catch (InvalidAccountException e) {
            return "Transfer failed: Invalid target account.";
        } catch (TransactionLimitException e) {
            return "Transfer failed: Transaction limit exceeded.";
        }
    }

    // Updated closeAccount method with exception handling
    public void closeAccount() throws BankingException {
        if (!status.equals("Active")) {
            throw new BankingException("Account is already closed.");
        }

        if (balance > 0) {
            throw new BankingException("Cannot close account: Please withdraw remaining balance of " + balance + " first.");
        }

        status = "Closed";
        logTransaction("ACCOUNT_CLOSED", 0.0, "Account closed");
    }

    // Backward compatibility wrapper for closeAccount
    public String closeAccount(boolean legacyMethod) {
        try {
            closeAccount();
            return "Account closed successfully.";
        } catch (BankingException e) {
            return e.getMessage();
        }
    }

    @Override
    public String toString() {
        return "Account No: " + accountNo + " | Name: " + accountName + " | Balance: " + balance + " | Status: " + status;
    }

    public double getBalance() {
        return balance;
    }

    // Implementation of TransactionLoggable interface methods
    @Override
    public String logTransaction(String type, double amount, String description) {
        String transactionId = UUID.randomUUID().toString();
        Transaction transaction = new Transaction() {
            private final String tid = transactionId;
            private final int accNo = accountNo;
            private final String txType = type;
            private final double txAmount = amount;
            private final String desc = description;
            private final LocalDateTime timestamp = LocalDateTime.now();

            @Override
            public String getTransactionId() {
                return tid;
            }

            @Override
            public int getAccountNumber() {
                return accNo;
            }

            @Override
            public String getType() {
                return txType;
            }

            @Override
            public double getAmount() {
                return txAmount;
            }

            @Override
            public String getDescription() {
                return desc;
            }

            @Override
            public LocalDateTime getTimestamp() {
                return timestamp;
            }
        };
        transactionHistory.add(transaction);
        return transactionId;
    }

    @Override
    public List<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactionHistory); // Return a copy to prevent modification
    }

    @Override
    public List<Transaction> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return transactionHistory.stream()
                .filter(t -> !t.getTimestamp().isBefore(startDate) && !t.getTimestamp().isAfter(endDate))
                .collect(Collectors.toList());
    }

    // Implementation of AccountVerifiable interface methods
    @Override
    public boolean verifyPin(String inputPin) {
        return this.pin.equals(inputPin);
    }

    @Override
    public boolean isActive() {
        return "Active".equals(status);
    }
}