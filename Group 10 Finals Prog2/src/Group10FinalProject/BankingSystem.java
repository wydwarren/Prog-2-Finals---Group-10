package Group10FinalProject;

import Group10FinalProject.Exceptions.*;
import Group10FinalProject.Interfaces.Transaction;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class BankingSystem extends ArrayList<BankAccounts> {
    // Collections to store accounts in different ways for efficient access
    private List<BankAccounts> accounts; // Main account storage
    private Map<Integer, BankAccounts> accountMap; // O(1) lookup by account number
    private Set<String> accountNames; // Unique account names
    private Queue<Transaction> pendingTransactions; // Process transactions in order

    public BankingSystem() {
        accounts = new ArrayList<>();
        accountMap = new HashMap<>();
        accountNames = new HashSet<>();
        pendingTransactions = new LinkedList<>();
    }

    // Load accounts from file
    public void loadAccounts() {
        accounts = new ArrayList<>();
        accountMap = new HashMap<>();
        accountNames = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("accounts.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length >= 5) {
                    int accountNo = Integer.parseInt(parts[0]);
                    String accountName = parts[1];
                    String pin = parts[2];
                    double balance = Double.parseDouble(parts[3]);
                    String status = parts[4];

                    // Determine account type and create appropriate account
                    BankAccounts account;
                    if (parts.length > 5 && parts[5].equals("Investment")) {
                        double interestRate = Double.parseDouble(parts[6]);
                        account = new InvestmentAccount(accountNo, accountName, pin, interestRate);
                    } else {
                        account = new BankAccounts(accountNo, accountName, pin);
                    }

                    try {
                        account.deposit(balance);
                        if (!status.equals("Active")) {
                            account.closeAccount(true);
                        }

                        // Add to collections
                        accounts.add(account);
                        accountMap.put(accountNo, account);
                        accountNames.add(accountName);
                    } catch (Exception e) {
                        System.err.println("Error loading account #" + accountNo + ": " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            // File doesn't exist yet, which is fine
            System.out.println("No existing accounts file found. Starting with empty account list.");
        }
    }

    // Save accounts to file
    public void saveAccounts() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("accounts.txt"))) {
            for (BankAccounts acc : accounts) {
                StringBuilder line = new StringBuilder();
                line.append(acc.getAccountNo()).append(",")
                        .append(acc.getAccountName()).append(",")
                        .append(acc.getPin()).append(",")
                        .append(acc.getBalance()).append(",")
                        .append(acc.getStatus());

                // Add investment account details if applicable
                if (acc instanceof InvestmentAccount) {
                    InvestmentAccount invAcc = (InvestmentAccount) acc;
                    line.append(",Investment,")
                            .append(invAcc.getInterestRate());
                }

                writer.write(line.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving accounts: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Add a new account
    public BankAccounts createAccount(String accountName, String pin, boolean isInvestment)
            throws BankingException {
        // Validate input
        if (accountName == null || accountName.trim().isEmpty()) {
            throw new BankingException("Account name cannot be empty");
        }

        if (pin == null || pin.trim().isEmpty()) {
            throw new BankingException("PIN cannot be empty");
        }

        // Generate unique account number
        int accountNo;
        Random random = new Random();
        do {
            accountNo = 100000000 + random.nextInt(900000000); // 9-digit number
        } while (accountMap.containsKey(accountNo));

        // Create appropriate account type
        BankAccounts newAccount;
        if (isInvestment) {
            newAccount = new InvestmentAccount(accountNo, accountName, pin);
        } else {
            newAccount = new BankAccounts(accountNo, accountName, pin);
        }

        // Add to collections
        accounts.add(newAccount);
        accountMap.put(accountNo, newAccount);
        accountNames.add(accountName);

        // Save updated accounts
        saveAccounts();

        return newAccount;
    }

    // Find account by account number
    public BankAccounts findAccount(int accountNo) throws InvalidAccountException {
        BankAccounts account = accountMap.get(accountNo);
        if (account == null) {
            throw new InvalidAccountException(accountNo);
        }
        return account;
    }

    // Authenticate account with PIN
    public BankAccounts authenticateAccount(int accountNo, String pin)
            throws InvalidAccountException, BankingException {
        BankAccounts account = findAccount(accountNo);

        if (!account.verifyPin(pin)) {
            throw new BankingException("Invalid PIN for account #" + accountNo);
        }

        return account;
    }

    // Process deposit
    public void deposit(int accountNo, double amount)
            throws InvalidAccountException, InvalidAmountException, AccountClosedException, AccountClosedException {
        BankAccounts account = findAccount(accountNo);
        account.deposit(amount);
        saveAccounts();
    }

    // Process withdrawal
    public void withdraw(int accountNo, double amount)
            throws InvalidAccountException, InvalidAmountException,
            AccountClosedException, InsufficientFundsException, TransactionLimitException {
        BankAccounts account = findAccount(accountNo);
        account.withdraw(amount);
        saveAccounts();
    }

    // Process transfer
    public void transfer(int fromAccountNo, int toAccountNo, double amount)
            throws InvalidAccountException, InvalidAmountException,
            AccountClosedException, InsufficientFundsException, TransactionLimitException {
        BankAccounts fromAccount = findAccount(fromAccountNo);
        BankAccounts toAccount = findAccount(toAccountNo);

        fromAccount.transfer(toAccount, amount);
        saveAccounts();
    }

    // Process investment (for investment accounts)
    public void invest(int accountNo, double amount, int termDays)
            throws InvalidAccountException, BankingException {
        BankAccounts account = findAccount(accountNo);

        if (!(account instanceof InvestmentAccount)) {
            throw new BankingException("Account #" + accountNo + " is not an investment account");
        }

        InvestmentAccount investmentAccount = (InvestmentAccount) account;
        try {
            investmentAccount.invest(amount, termDays);
            saveAccounts();
        } catch (Exception e) {
            throw new BankingException("Investment failed: " + e.getMessage());
        }
    }

    // Close account
    public void closeAccount(int accountNo)
            throws InvalidAccountException, BankingException {
        BankAccounts account = findAccount(accountNo);

        try {
            account.closeAccount();
            saveAccounts();
        } catch (BankingException e) {
            throw e;
        }
    }

    // Get all accounts (sorted by balance, descending)
    public List<BankAccounts> getAllAccountsSortedByBalance() {
        List<BankAccounts> sortedAccounts = new ArrayList<>(accounts);
        Collections.sort(sortedAccounts, (a1, a2) -> Double.compare(a2.getBalance(), a1.getBalance()));
        return sortedAccounts;
    }

    // Get active accounts
    public List<BankAccounts> getActiveAccounts() {
        return accounts.stream()
                .filter(acc -> acc.isActive())
                .collect(Collectors.toList());
    }

    // Get accounts by type
    public List<BankAccounts> getAccountsByType(Class<?> accountType) {
        return accounts.stream()
                .filter(acc -> accountType.isInstance(acc))
                .collect(Collectors.toList());
    }

    // Queue a transaction for later processing
    public void queueTransaction(Transaction transaction) {
        pendingTransactions.offer(transaction);
    }

    // Process pending transactions
    public void processPendingTransactions() {
        while (!pendingTransactions.isEmpty()) {
            Transaction txn = pendingTransactions.poll();
            try {
                // Process based on transaction type
                BankAccounts account = findAccount(txn.getAccountNumber());
                switch (txn.getType()) {
                    case "DEPOSIT":
                        account.deposit(txn.getAmount());
                        break;
                    case "WITHDRAWAL":
                        account.withdraw(txn.getAmount());
                        break;
                    // Other transaction types...
                    default:
                        System.out.println("Unknown transaction type: " + txn.getType());
                }
            } catch (Exception e) {
                System.err.println("Error processing transaction: " + e.getMessage());
                // Log failed transaction for later recovery
            }
        }
        saveAccounts();
    }

    // Get total bank assets
    public double getTotalAssets() {
        return accounts.stream()
                .mapToDouble(BankAccounts::getBalance)
                .sum();
    }

    // Apply interest to all investment accounts
    public void applyInterestToAllAccounts() {
        accounts.stream()
                .filter(acc -> acc instanceof InvestmentAccount)
                .map(acc -> (InvestmentAccount) acc)
                .forEach(InvestmentAccount::applyInterest);
        saveAccounts();
    }

    @Override
    public List<BankAccounts> reversed() {
        return super.reversed();
    }
}