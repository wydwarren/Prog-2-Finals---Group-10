package Group10FinalProject;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.text.DecimalFormat;

public class AccountsMainGUI extends JFrame {
    private BankAccounts loggedInAccount; // Currently logged-in account
    private ArrayList<BankAccounts> accounts; // List of all bank accounts
    private JTextArea displayArea; // Area to display transaction details and updates
    private JTextField amountField; // Input field for monetary amounts

    // Consistent color scheme across all GUIs
    private final Color PRIMARY_COLOR = new Color(0, 73, 123); // Dark blue
    private final Color ACCENT_COLOR = new Color(225, 177, 44); // Gold
    private final Color BACKGROUND_COLOR = new Color(240, 240, 245); // Light gray
    private final Color PANEL_COLOR = new Color(250, 250, 250); // White-ish
    private final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private final Font FIELD_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private final DecimalFormat MONEY_FORMAT = new DecimalFormat("#,##0.00");

    public AccountsMainGUI(BankAccounts account, ArrayList<BankAccounts> accounts) {
        ImageIcon image = new ImageIcon("bankPNG.png");
        this.loggedInAccount = account;
        this.accounts = accounts;

        // Basic frame setup
        setTitle("Aegis Bank");
        setSize(650, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(image.getImage());


        // Set up content pane with border layout
        Container contentPane = getContentPane();
        contentPane.setBackground(BACKGROUND_COLOR);
        contentPane.setLayout(new BorderLayout(10, 10));

        // Header panel with account information
        JPanel headerPanel = createHeaderPanel();
        contentPane.add(headerPanel, BorderLayout.NORTH);

        // Input panel with amount field and buttons
        JPanel inputPanel = createInputPanel();
        contentPane.add(inputPanel, BorderLayout.CENTER);

        // Transaction display panel
        JPanel displayPanel = createDisplayPanel();
        contentPane.add(displayPanel, BorderLayout.SOUTH);

        // Add initial welcome message to display area
        updateDisplayWithWelcomeMessage();

        // Set minimum size for better appearance
        setMinimumSize(new Dimension(600, 650));
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(PRIMARY_COLOR);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Title section
        JLabel titleLabel = new JLabel("Aegis Bank");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Account info section
        JPanel accountInfoPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        accountInfoPanel.setOpaque(false);

        JLabel accountNameLabel = new JLabel("Welcome, " + loggedInAccount.getAccountName());
        accountNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        accountNameLabel.setForeground(Color.WHITE);

        JLabel accountNumberLabel = new JLabel("Account #: " + loggedInAccount.getAccountNo());
        accountNumberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        accountNumberLabel.setForeground(Color.WHITE);

        accountInfoPanel.add(accountNameLabel);
        accountInfoPanel.add(accountNumberLabel);
        panel.add(accountInfoPanel, BorderLayout.CENTER);

        // Add logout button
        JButton logoutBtn = createStyledButton("Logout", new Color(200, 50, 50));
        logoutBtn.setPreferredSize(new Dimension(100, 30));
        logoutBtn.addActionListener(e -> logout());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(logoutBtn);
        panel.add(buttonPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createInputPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        // Amount input section
        JPanel inputSection = new JPanel(new BorderLayout(10, 0));
        inputSection.setBackground(BACKGROUND_COLOR);
        inputSection.setBorder(new CompoundBorder(
                new TitledBorder(new LineBorder(PRIMARY_COLOR), "Transaction Details"),
                new EmptyBorder(10, 10, 10, 10)));

        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setFont(LABEL_FONT);
        amountLabel.setForeground(PRIMARY_COLOR);

        amountField = new JTextField(20);
        amountField.setFont(FIELD_FONT);
        amountField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));

        inputSection.add(amountLabel, BorderLayout.WEST);
        inputSection.add(amountField, BorderLayout.CENTER);

        // Buttons section
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        // Create and add buttons
        JButton depositBtn = createStyledButton("Deposit", ACCENT_COLOR);
        JButton withdrawBtn = createStyledButton("Withdraw", ACCENT_COLOR);
        JButton inquireBtn = createStyledButton("Check Balance", PRIMARY_COLOR);
        JButton transferBtn = createStyledButton("Transfer Money", PRIMARY_COLOR);
        JButton investBtn = createStyledButton("Invest Money", PRIMARY_COLOR);
        JButton closeBtn = createStyledButton("Close Account", new Color(180, 50, 50));

        // Add action listeners
        depositBtn.addActionListener(e -> deposit());
        withdrawBtn.addActionListener(e -> withdraw());
        inquireBtn.addActionListener(e -> inquireBalance());
        closeBtn.addActionListener(e -> closeAccount());
        transferBtn.addActionListener(e -> transferMoney());
        investBtn.addActionListener(e -> investment());

        // Add buttons to panel
        buttonPanel.add(depositBtn);
        buttonPanel.add(withdrawBtn);
        buttonPanel.add(transferBtn);
        buttonPanel.add(inquireBtn);
        buttonPanel.add(investBtn);
        buttonPanel.add(closeBtn);

        // Add sections to main panel
        mainPanel.add(inputSection, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel createDisplayPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new CompoundBorder(
                new TitledBorder(new LineBorder(PRIMARY_COLOR), "Transaction History"),
                new EmptyBorder(10, 10, 10, 10)));
        panel.setBackground(BACKGROUND_COLOR);

        // Display area with scroll capability
        displayArea = new JTextArea();
        displayArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        displayArea.setEditable(false);
        displayArea.setLineWrap(true);
        displayArea.setWrapStyleWord(true);
        displayArea.setMargin(new Insets(10, 10, 10, 10));
        displayArea.setBackground(PANEL_COLOR);
        displayArea.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });

        return button;
    }

    private void updateDisplayWithWelcomeMessage() {
        displayArea.setText("");
        displayArea.append("Welcome to Banking System Management System\n");
        displayArea.append("--------------------------------------------------\n");
        displayArea.append("Account Name: " + loggedInAccount.getAccountName() + "\n");
        displayArea.append("Account Number: " + loggedInAccount.getAccountNo() + "\n");
        displayArea.append("Current Balance: ₱" + MONEY_FORMAT.format(loggedInAccount.getBalance()) + "\n");
        displayArea.append("Account Status: " + loggedInAccount.getStatus() + "\n");
        displayArea.append("--------------------------------------------------\n");
    }

    private void logout() {
        int option = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to log out?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (option == JOptionPane.YES_OPTION) {
            new LoginGUI(accounts).setVisible(true);
            dispose();
        }
    }

    private void deposit() {
        try {
            double amount = Double.parseDouble(amountField.getText());

            if (!loggedInAccount.getStatus().equals("Active")) {
                displayArea.append("Error: Cannot deposit to a closed account.\n");
                return;
            }

            if (amount > 0) {
                loggedInAccount.deposit(amount);
                displayArea.append("Transaction: Deposit of ₱" + MONEY_FORMAT.format(amount) + " successful\n");
                displayArea.append("New Balance: ₱" + MONEY_FORMAT.format(loggedInAccount.getBalance()) + "\n");
                displayArea.append("--------------------------------------------------\n");
                amountField.setText("");

                saveAccountsToFile(accounts);
            } else {
                displayArea.append("Invalid amount: Please enter a positive number.\n");
                displayArea.append("--------------------------------------------------\n");
            }
        } catch (NumberFormatException e) {
            displayArea.append("Invalid amount entered. Please enter a valid number.\n");
            displayArea.append("--------------------------------------------------\n");
        }
    }

    private void investment() {
        try {
            if (!loggedInAccount.getStatus().equals("Active")) {
                JOptionPane.showMessageDialog(
                        this,
                        "Cannot invest from a closed account.",
                        "Investment Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            double amount = Double.parseDouble(amountField.getText());

            if (amount <= 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter a positive amount.",
                        "Invalid Amount",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            if (loggedInAccount.getBalance() < amount) {
                JOptionPane.showMessageDialog(
                        this,
                        "Insufficient balance for this investment.",
                        "Investment Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            double profit = amount * 0.10; // 10% profit calculation
            loggedInAccount.withdraw(amount); // Deduct invested amount
            loggedInAccount.deposit(amount + profit); // Add back invested amount + profit

            displayArea.append("Transaction: Investment of ₱" + MONEY_FORMAT.format(amount) + "\n");
            displayArea.append("Profit (10%): ₱" + MONEY_FORMAT.format(profit) + "\n");
            displayArea.append("Total Return: ₱" + MONEY_FORMAT.format(amount + profit) + "\n");
            displayArea.append("New Balance: ₱" + MONEY_FORMAT.format(loggedInAccount.getBalance()) + "\n");
            displayArea.append("--------------------------------------------------\n");

            amountField.setText("");

            // Save updated accounts
            saveAccountsToFile(accounts);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Invalid amount. Please enter a valid number.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void withdraw() {
        try {
            if (!loggedInAccount.getStatus().equals("Active")) {
                displayArea.append("Error: Cannot withdraw from a closed account.\n");
                displayArea.append("--------------------------------------------------\n");
                return;
            }

            double amount = Double.parseDouble(amountField.getText());

            if (amount <= 0) {
                displayArea.append("Invalid amount: Please enter a positive number.\n");
                displayArea.append("--------------------------------------------------\n");
                return;
            }

            if (amount > loggedInAccount.getBalance()) {
                displayArea.append("Withdrawal failed: Insufficient funds.\n");
                displayArea.append("Available balance: ₱" + MONEY_FORMAT.format(loggedInAccount.getBalance()) + "\n");
                displayArea.append("--------------------------------------------------\n");
                return;
            }

            loggedInAccount.withdraw(amount);
            displayArea.append("Transaction: Withdrawal of ₱" + MONEY_FORMAT.format(amount) + " successful\n");
            displayArea.append("New Balance: ₱" + MONEY_FORMAT.format(loggedInAccount.getBalance()) + "\n");
            displayArea.append("--------------------------------------------------\n");
            amountField.setText("");

            // Save updated accounts
            saveAccountsToFile(accounts);

        } catch (NumberFormatException e) {
            displayArea.append("Invalid amount entered. Please enter a valid number.\n");
            displayArea.append("--------------------------------------------------\n");
        }
    }

    private void inquireBalance() {
        displayArea.append("Balance Inquiry\n");
        displayArea.append("Account: " + loggedInAccount.getAccountNo() + " - " + loggedInAccount.getAccountName() + "\n");
        displayArea.append("Current Balance: ₱" + MONEY_FORMAT.format(loggedInAccount.getBalance()) + "\n");
        displayArea.append("Account Status: " + loggedInAccount.getStatus() + "\n");
        displayArea.append("--------------------------------------------------\n");
    }

    private void transferMoney() {
        if (!loggedInAccount.getStatus().equals("Active")) {
            displayArea.append("Error: Cannot transfer from a closed account.\n");
            displayArea.append("--------------------------------------------------\n");
            return;
        }

        try {
            // Create custom dialog for transfer
            JDialog transferDialog = new JDialog(this, "Transfer Money", true);
            transferDialog.setSize(400, 200);
            transferDialog.setLocationRelativeTo(this);
            transferDialog.setLayout(new BorderLayout());

            JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
            formPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
            formPanel.setBackground(BACKGROUND_COLOR);

            JLabel recipientLabel = new JLabel("Recipient Account #:");
            recipientLabel.setFont(LABEL_FONT);
            JTextField recipientField = new JTextField();
            recipientField.setFont(FIELD_FONT);

            JLabel amountLabel = new JLabel("Amount to Transfer:");
            amountLabel.setFont(LABEL_FONT);
            JTextField amountFieldDialog = new JTextField();
            amountFieldDialog.setFont(FIELD_FONT);
            if (!amountField.getText().isEmpty()) {
                amountFieldDialog.setText(amountField.getText());
            }

            JLabel noteLabel = new JLabel("Note (optional):");
            noteLabel.setFont(LABEL_FONT);
            JTextField noteField = new JTextField();
            noteField.setFont(FIELD_FONT);

            formPanel.add(recipientLabel);
            formPanel.add(recipientField);
            formPanel.add(amountLabel);
            formPanel.add(amountFieldDialog);
            formPanel.add(noteLabel);
            formPanel.add(noteField);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.setBackground(BACKGROUND_COLOR);

            JButton cancelBtn = createStyledButton("Cancel", new Color(150, 150, 150));
            JButton transferBtn = createStyledButton("Transfer", ACCENT_COLOR);

            buttonPanel.add(cancelBtn);
            buttonPanel.add(transferBtn);

            transferDialog.add(formPanel, BorderLayout.CENTER);
            transferDialog.add(buttonPanel, BorderLayout.SOUTH);

            // Button actions
            cancelBtn.addActionListener(e -> transferDialog.dispose());

            transferBtn.addActionListener(e -> {
                try {
                    int targetAccountNo = Integer.parseInt(recipientField.getText());
                    double amount = Double.parseDouble(amountFieldDialog.getText());
                    String note = noteField.getText();

                    if (amount <= 0) {
                        JOptionPane.showMessageDialog(
                                transferDialog,
                                "Please enter a positive amount.",
                                "Invalid Amount",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }

                    if (amount > loggedInAccount.getBalance()) {
                        JOptionPane.showMessageDialog(
                                transferDialog,
                                "Insufficient funds for this transfer.",
                                "Transfer Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }

                    boolean accountFound = false;

                    for (BankAccounts acc : accounts) {
                        if (acc.getAccountNo() == targetAccountNo) {
                            accountFound = true;

                            if (!acc.getStatus().equals("Active")) {
                                JOptionPane.showMessageDialog(
                                        transferDialog,
                                        "Cannot transfer to a closed account.",
                                        "Transfer Error",
                                        JOptionPane.ERROR_MESSAGE
                                );
                                return;
                            }

                            // Process transfer
                            loggedInAccount.withdraw(amount);
                            acc.deposit(amount);

                            // Update display
                            String noteDisplay = note.isEmpty() ? "" : " (Note: " + note + ")";
                            displayArea.append("Transaction: Transfer to Account #" + targetAccountNo + noteDisplay + "\n");
                            displayArea.append("Amount: ₱" + MONEY_FORMAT.format(amount) + "\n");
                            displayArea.append("New Balance: ₱" + MONEY_FORMAT.format(loggedInAccount.getBalance()) + "\n");
                            displayArea.append("--------------------------------------------------\n");

                            // Clear field and close dialog
                            amountField.setText("");
                            transferDialog.dispose();

                            // Save updated accounts
                            saveAccountsToFile(accounts);
                            break;
                        }
                    }

                    if (!accountFound) {
                        JOptionPane.showMessageDialog(
                                transferDialog,
                                "Recipient account not found.",
                                "Transfer Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                            transferDialog,
                            "Please enter valid account number and amount.",
                            "Input Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            });

            transferDialog.setVisible(true);

        } catch (Exception e) {
            displayArea.append("Transfer failed: " + e.getMessage() + "\n");
            displayArea.append("--------------------------------------------------\n");
        }
    }

    private void closeAccount() {
        if (!loggedInAccount.getStatus().equals("Active")) {
            displayArea.append("This account is already closed.\n");
            displayArea.append("--------------------------------------------------\n");
            return;
        }

        if (loggedInAccount.getBalance() > 0) {
            int option = JOptionPane.showConfirmDialog(
                    this,
                    "You have a remaining balance of ₱" + MONEY_FORMAT.format(loggedInAccount.getBalance()) + ".\n" +
                            "Would you like to withdraw all funds before closing?",
                    "Close Account",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (option == JOptionPane.YES_OPTION) {
                // Withdraw all funds
                double amount = loggedInAccount.getBalance();
                loggedInAccount.withdraw(amount);
                displayArea.append("Withdrew all funds: ₱" + MONEY_FORMAT.format(amount) + "\n");

                // Now close the account
                loggedInAccount.closeAccount();
                displayArea.append("Account closed successfully.\n");
                displayArea.append("--------------------------------------------------\n");

                saveAccountsToFile(accounts);

                JOptionPane.showMessageDialog(
                        this,
                        "Your account has been closed. Thank you for using Banking System.\nHave a nice day!",
                        "Account Closed",
                        JOptionPane.INFORMATION_MESSAGE
                );

                new LoginGUI(accounts).setVisible(true);
                dispose();

            } else if (option == JOptionPane.NO_OPTION) {
                // Just close without withdrawing
                JOptionPane.showMessageDialog(
                        this,
                        "Cannot close account with remaining balance.\nPlease withdraw all funds first.",
                        "Close Account",
                        JOptionPane.WARNING_MESSAGE
                );
            }
            // CANCEL option does nothing

        } else {
            // No balance, can close directly
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to close this account?",
                    "Confirm Account Closure",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                loggedInAccount.closeAccount();
                displayArea.append("Account closed successfully.\n");
                displayArea.append("--------------------------------------------------\n");

                saveAccountsToFile(accounts);

                JOptionPane.showMessageDialog(
                        this,
                        "Your account has been closed. Thank you for using Banking System.\nHave a nice day!",
                        "Account Closed",
                        JOptionPane.INFORMATION_MESSAGE
                );

                new LoginGUI(accounts).setVisible(true);
                dispose();
            }
        }
    }

    public static void saveAccountsToFile(ArrayList<BankAccounts> accounts) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("accounts.txt"))) {
            for (BankAccounts acc : accounts) {
                writer.write(acc.getAccountNo() + "," + acc.getAccountName() + "," + acc.getPin() + "," + acc.getBalance() + "," + acc.getStatus());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}