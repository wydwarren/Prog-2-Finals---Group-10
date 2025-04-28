package Group10FinalProject;

// Importing necessary Swing components for GUI creation
import javax.swing.*;// Provides JFrame, JButton, JLabel, JTextField, and JOptionPane for UI elements

public class Main {
    private static BankingSystem bankingSystem;

    public static void main(String[] args) {
        // Initialize banking system
        bankingSystem = new BankingSystem();
        bankingSystem.loadAccounts();

        // Ensures the GUI is created on the Event Dispatch Thread (EDT) for thread safety
        SwingUtilities.invokeLater(() -> new LoginGUI(bankingSystem).setVisible(true));
    }

    // Method to access the banking system from other classes
    public static BankingSystem getBankingSystem() {
        return bankingSystem;
    }
}