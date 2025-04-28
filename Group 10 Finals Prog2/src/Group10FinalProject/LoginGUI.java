package Group10FinalProject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class LoginGUI extends JFrame {
    private ArrayList<BankAccounts> accounts;
    private JTextField AccounNOField, PINCField;
    private final Color PRIMARY_COLOR = new Color(0, 73, 123); // Dark blue
    private final Color ACCENT_COLOR = new Color(225, 177, 44); // Gold
    private final Color BACKGROUND_COLOR = new Color(240, 240, 245); // Light gray
    private final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private final Font FIELD_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 20);

    public LoginGUI(ArrayList<BankAccounts> accounts) {
        ImageIcon image = new ImageIcon("bankPNG.png");
        if (this.accounts == null) {
            this.accounts = new ArrayList<>();
        }

        this.accounts = accounts;

        // Basic frame setup
        setTitle("Aegis Bank");
        setSize(450, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(image.getImage());

        // Set background color
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Create main panel with padding
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);

        // Add title label
        JLabel titleLabel = new JLabel("Aegis Bank");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Log in to your account");
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        subtitleLabel.setForeground(new Color(100, 100, 100));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 8, 15, 8);
        mainPanel.add(subtitleLabel, gbc);

        // Account Number
        JLabel AccountNO = new JLabel("Account No:");
        AccountNO.setFont(LABEL_FONT);
        AccountNO.setForeground(PRIMARY_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 8, 8, 8);
        mainPanel.add(AccountNO, gbc);

        AccounNOField = new JTextField(15);
        AccounNOField.setFont(FIELD_FONT);
        AccounNOField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        gbc.gridx = 1;
        gbc.gridy = 2;
        mainPanel.add(AccounNOField, gbc);

        // PIN
        JLabel PINC = new JLabel("PIN:");
        PINC.setFont(LABEL_FONT);
        PINC.setForeground(PRIMARY_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(PINC, gbc);

        PINCField = new JPasswordField(15);
        PINCField.setFont(FIELD_FONT);
        PINCField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        gbc.gridx = 1;
        gbc.gridy = 3;
        mainPanel.add(PINCField, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton loginBtn = createStyledButton("Log In", ACCENT_COLOR);
        JButton registerBtn = createStyledButton("Register", PRIMARY_COLOR);

        buttonPanel.add(loginBtn);
        buttonPanel.add(registerBtn);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 8, 8, 8);
        mainPanel.add(buttonPanel, gbc);

        // Add action listeners
        registerBtn.addActionListener(e -> registerAccount());
        loginBtn.addActionListener(e -> loginAccount());

        // Add key listener for Enter key to login
        PINCField.addActionListener(e -> loginAccount());

        pack(); // Adjust size to fit components
        setMinimumSize(new Dimension(400, 280)); // Set minimum window size
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));

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

    private void registerAccount() {
        new RegisterGUI(accounts).setVisible(true);
        dispose();
        JOptionPane.showMessageDialog(
                null,
                "Welcome to Aegis Bank",
                "Banking System",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void loginAccount() {
        try {
            int accNo = Integer.parseInt(AccounNOField.getText());
            String pinInput = PINCField.getText().trim();

            for (BankAccounts acc : accounts) {
                if (acc.getAccountNo() == accNo && acc.getPin().equals(pinInput)) {
                    new AccountsMainGUI(acc, accounts).setVisible(true);
                    dispose();
                    return;
                }
            }
            JOptionPane.showMessageDialog(
                    null,
                    "Invalid account number or PIN.",
                    "Banking System",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Invalid account number format.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static ArrayList<BankAccounts> loadAccountsFromFile() {
        ArrayList<BankAccounts> accounts = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("accounts.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length == 5) {
                    int accountNo = Integer.parseInt(parts[0]);
                    String accountName = parts[1];
                    String pin = parts[2];
                    double balance = Double.parseDouble(parts[3]);
                    String status = parts[4];

                    BankAccounts account = new BankAccounts(accountNo, accountName, pin);
                    account.deposit(balance);
                    if (!status.equals("Active")) {
                        account.closeAccount();
                    }

                    accounts.add(account);
                }
            }
        } catch (IOException e) {
            // File doesn't exist yet, which is fine
        }

        return accounts;
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