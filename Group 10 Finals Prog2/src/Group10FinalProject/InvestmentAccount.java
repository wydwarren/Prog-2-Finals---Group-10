package Group10FinalProject;

import Group10FinalProject.Exceptions.AccountClosedException;
import Group10FinalProject.Exceptions.BankingException;
import Group10FinalProject.Exceptions.InsufficientFundsException;
import Group10FinalProject.Exceptions.InvalidAmountException;
import Group10FinalProject.Interfaces.InterestBearing;

public class InvestmentAccount extends BankAccounts implements InterestBearing {
    private double interestRate;
    private double accruedInterest;
    private static final double DEFAULT_RATE = 0.05; // 5% default interest rate

    public InvestmentAccount(int accountNo, String accountName, String pin) {
        super(accountNo, accountName, pin);
        this.interestRate = DEFAULT_RATE;
        this.accruedInterest = 0.0;
    }

    public InvestmentAccount(int accountNo, String accountName, String pin, double interestRate) {
        super(accountNo, accountName, pin);
        this.interestRate = interestRate;
        this.accruedInterest = 0.0;
    }

    @Override
    public double calculateInterest(int days) {
        double dailyRate = interestRate / 365.0;
        return getBalance() * dailyRate * days;
    }

    @Override
    public double applyInterest() {
        double interest = calculateInterest(30); // Default to monthly interest
        accruedInterest = 0.0;

        try {
            deposit(interest);
            logTransaction("INTEREST", interest, "Applied interest payment");
            return interest;
        } catch (Exception e) {
            // If deposit fails, store the interest for later application
            accruedInterest = interest;
            return 0.0;
        }
    }

    @Override
    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double newRate) {
        if (newRate >= 0) {
            this.interestRate = newRate;
            logTransaction("RATE_CHANGE", 0.0, "Interest rate changed to " + (newRate * 100) + "%");
        }
    }

    public double getAccruedInterest() {
        return accruedInterest;
    }

    // Specialized investment method
    public void invest(double amount, int termDays) throws BankingException, InsufficientFundsException,
            InvalidAmountException, AccountClosedException {
        if (amount <= 0) {
            throw new InvalidAmountException(amount);
        }

        if (!isActive()) {
            throw new AccountClosedException(getAccountNo());
        }

        if (amount > getBalance()) {
            throw new InsufficientFundsException(amount, getBalance());
        }

        // Calculate returns based on term
        double returns = amount * (interestRate / 365.0) * termDays;

        try {
            // First withdraw the investment amount
            withdraw(amount);

            // Then deposit the original amount plus returns
            deposit(amount + returns);

            logTransaction("INVESTMENT", amount,
                    "Invested ₱" + amount + " for " + termDays + " days with return of ₱" + returns);
        } catch (Exception e) {
            throw new BankingException("Investment failed: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return super.toString() + " | Type: Investment | Interest Rate: " + (interestRate * 100) + "%";
    }
}