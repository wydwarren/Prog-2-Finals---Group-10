package Group10FinalProject.Interfaces;

import java.time.LocalDateTime;
import java.util.List;

// Interface for accounts that earn interest
public interface InterestBearing {
    /**
     * Calculate interest for the account
     * @param days Number of days to calculate interest for
     * @return The amount of interest earned
     */
    double calculateInterest(int days);

    /**
     * Apply accrued interest to the account balance
     * @return The amount of interest applied
     */
    double applyInterest();

    /**
     * Get the current interest rate
     * @return The interest rate as a decimal (e.g., 0.05 for 5%)
     */
    double getInterestRate();
}

