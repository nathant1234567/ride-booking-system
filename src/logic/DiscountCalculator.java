package logic;

/**
 * Class to calculate discounts based on the time of day.
 */
public class DiscountCalculator {
    /**
     * This method took in the original price and the current hour and worked as the central hub for figuring
     *  out which time based discounts were active and applying them.
     * @param basePrice
     * @param timeOfDay
     * @return
     */
    public static double calculateDiscount(double basePrice, int timeOfDay) {
        double finalPrice = basePrice;

        // Checked if the hour was between 10:00 and 15:00 using the AND operator and if it applied
        // a 20% discount by multiplying the base price by 0.80
        if (timeOfDay >= 10 && timeOfDay <= 15) {
            finalPrice = basePrice * 0.80;
        }
        // Used the OR operator here because the late night window crossed over midnight so if either hour
        // condition was met it applied a 15% discount by multiplying the price by 0.85
        else if (timeOfDay >= 22 || timeOfDay <= 5) {
            finalPrice = basePrice * 0.85;
        }

        return finalPrice;
    }
}

