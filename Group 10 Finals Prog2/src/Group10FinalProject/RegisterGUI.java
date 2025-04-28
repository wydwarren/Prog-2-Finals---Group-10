package Group10FinalProject;

// Importing necessary Swing components for GUI creation
import javax.swing.*; // Provides JFrame, JButton, JLabel, JTextField, and JOptionPane for UI elements

// Importing AWT for layout and font styling
import java.awt.*; // Provides Font and layout management

// Importing ArrayList to manage and store multiple BankAccounts
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class RegisterGUI extends JFrame {
    private ArrayList<BankAccounts> accounts;
    private JTextField accountname,nameField,AccountNumberField,PINField;


    // List to store registered accounts
    ImageIcon image = new ImageIcon("bankPNG.png");
    public RegisterGUI(ArrayList<BankAccounts>accounts) {

        this.accounts = accounts;
        setTitle("Register New Account");
        setSize(400, 230);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);
        setIconImage(image.getImage());

        JLabel accountname = new JLabel("Account Name:");
        accountname.setFont(new Font("Arial", Font.PLAIN, 16));
        accountname.setBounds(30, 30, 200, 30);
        nameField = new JTextField(15);
        nameField.setBounds(140, 30, 200, 30);

        JLabel Accountnumber = new JLabel("Account no:");
        Accountnumber.setFont(new Font("Arial", Font.PLAIN, 16));
        Accountnumber.setBounds(30, 70, 100, 30);
        // Generate random 9-digit account number
        StringBuilder nineDigitNumber = new StringBuilder();
        Random random = new Random();
        String accountNumber = String.format("%09d", random.nextInt(1000000000));
        AccountNumberField = new JTextField(accountNumber);
        AccountNumberField.setEditable(false);
        AccountNumberField.setBounds(140, 70, 200, 30);



        JLabel PIN = new JLabel("PIN:");
        PIN.setFont(new Font("Arial", Font.PLAIN, 16));
        PIN.setBounds(30, 110, 100, 30);
        PINField = new JTextField(15);
        PINField.setBounds(140, 110, 200, 30);


        JButton submitBtn = new JButton("Submit");
        submitBtn.setBounds(50, 150, 100, 30);
        submitBtn.setBackground(Color.RED);
        submitBtn.setForeground(Color.WHITE);

        JButton  CloseBtn = new JButton("Close");
        CloseBtn.setBounds(200, 150, 100, 30);
        CloseBtn.setBackground(Color.RED);
        CloseBtn.setForeground(Color.WHITE);

        add(accountname);
        add(nameField);
        add(Accountnumber);
        add(AccountNumberField);
        add(PIN);
        add(PINField);
        add(submitBtn);
        add(CloseBtn);

        submitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String accountnme = nameField.getText();
                String accNumber = AccountNumberField.getText();
                String pin = PINField.getText();

                if (!accountnme.isEmpty() && !accNumber.isEmpty() && !pin.isEmpty()) {
                    accounts.add(new BankAccounts(Integer.parseInt(accNumber), accountnme, pin));
                    JOptionPane.showMessageDialog(null, "Account successfully registered!");
                    new LoginGUI(accounts).setVisible(true);
                    dispose();
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter("Accounts.txt", true))) {
                        writer.write(accNumber + "," + accountnme + "," + pin + ",0.0,Active");
                        writer.newLine();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Please fill out all fields.");
                }
            }
        });
        CloseBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginGUI(accounts).setVisible(true);
                dispose();
            }
        });

        setVisible(true);

    }
}

