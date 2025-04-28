package Group10FinalProject.Interfaces;

// Interface for account verification
public interface AccountVerifiable {
    /**
     * Verify if the PIN is correct for this account
     * @param pin The PIN to verify
     * @return true if the PIN is correct, false otherwise
     */
    boolean verifyPin(String pin);

    /**
     * Check if the account is active
     * @return true if the account is active, false otherwise
     */
    boolean isActive();

    /**
     * Verify if an account exists
     * @param accountNumber The account number to verify
     * @return true if the account exists, false otherwise
     */
    static boolean verifyAccountExists(int accountNumber) {
        // This would be implemented by classes that use this interface
        return false;
    }
}
