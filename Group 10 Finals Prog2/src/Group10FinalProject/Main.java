package Group10FinalProject;

// Importing necessary Swing components for GUI creation
import javax.swing.*;// Provides JFrame, JButton, JLabel, JTextField, and JOptionPane for UI elements

// Importing ArrayList to manage and store multiple BankAccounts
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        // List to store all registered bank accounts
        ArrayList<BankAccounts> accounts = LoginGUI.loadAccountsFromFile();
        new LoginGUI(accounts);

        // Ensures the GUI is created on the Event Dispatch Thread (EDT) for thread safety
        SwingUtilities.invokeLater(() -> new LoginGUI(accounts).setVisible(true));
    }
}