package Group10FinalProject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

// Represents a bank account with basic operations like deposit, withdraw, and balance inquiry
public class BankAccounts {
    // Account details
    private int accountNo;  // Unique account number for identification
    private String accountName; // Name associated with the account
    private double balance; // Current account balance
    private String status; // Status of the account (e.g., Active/Closed)
    private String pin;

    // Constructor to initialize account details with default balance and active status
    public BankAccounts(int accountNo, String accountName, String pin) {
        this.accountNo = accountNo;
        this.accountName = accountName;
        this.balance = 0.0; // Default starting balance is zero
        this.status = "Active"; // Default status is "Active"
        this.pin = pin;
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

    public String getPin(){
        return pin;
    }


    public void deposit(double amount) {

        if (!status.equals("Active")) {
            return;
        }

        if (amount > 0) {
            balance += amount;
        }
    }


    public String withdraw(double amount) {

        if (!status.equals("Active")) {
            return "Withdrawal failed: Account is closed.";
        }

        if (balance == 0) {
            return "Withdrawal failed: Insufficient balance.";
        }
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            return "Withdrawal successful: " + amount;
        } else {
            return "Withdrawal failed: Insufficient balance.";
        }
    }

    public double inquireBalance() {

        return balance;
    }

    public String transfer(BankAccounts targetAccount, double amount) {

        if (!status.equals("Active")) {
            return "Transfer failed: Account is closed.";
        }


        if (!targetAccount.getStatus().equals("Active")) {
            return "Transfer failed: Target account is closed.";
        }

        if (balance == 0) {
            return "Transfer failed: Insufficient balance.";
        }
        String withdrawResult = withdraw(amount);
        if (withdrawResult.startsWith("Withdrawal successful")) {
            targetAccount.deposit(amount);
            return "Transfer successful: " + amount;
        }
        return "Transfer failed: Insufficient balance.";
    }

    public String closeAccount() {
        if (!status.equals("Active")) {
            return "Account is already closed.";
        }

        if (balance > 0) {
            return "Cannot close account: Please withdraw remaining balance of " + balance + " first.";
        }

        status = "Closed";
        return "Account closed successfully.";
    }

    @Override
    public String toString() {
        return "Account No: " + accountNo + " | Name: " + accountName + " | Balance: " + balance + " | Status: " + status;
    }

    public double getBalance() {
        return balance;
    }
}

