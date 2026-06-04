package logic;


/**
 * A class to calculate fare estimates based on multiple factors such as vehicle type,
 * luggage count, travel distance, day of the week, and time of day.
 */
public class TariffCalculator {

    /**
     * This method took in the 5 customer factors and worked out a dynamic price estimate before the booking was finalised.
     * @param vehicleType
     * @param luggageCount
     * @param distance
     * @param dayOfWeek
     * @param timeOfDay
     * @return
     */
    public static double calculateEstimate(String vehicleType, int luggageCount, double distance, String dayOfWeek, int timeOfDay) {

        // Worked out the base fare first using £2.50 per mile and added a flat £2.00 fee per bag.
        double baseFare = distance * 2.50;
        double luggageFee = luggageCount * 2.00;
        double subtotal = baseFare + luggageFee;

        // Checked the vehicle type and multiplied the subtotal by 1.5 for Executive or 2.0 for Minibus if either applied.
        if (vehicleType.equalsIgnoreCase("Executive")) {
            subtotal *= 1.5;
        } else if (vehicleType.equalsIgnoreCase("Minibus")) {
            subtotal *= 2.0;
        }

        // used the OR operator here to check if it was the weekend so it could apply
        // a 20% surge pricing model for the higher demand days.
        if (dayOfWeek.equalsIgnoreCase("Saturday") || dayOfWeek.equalsIgnoreCase("Sunday")) {
            subtotal *= 1.2;
        }

        // Reused the time-based discount logic from Sprint 1 here. Off-peak (10-15) gets
        // a 20% discount and late-night gets 15% off.
        if (timeOfDay >= 10 && timeOfDay <= 15) {
            subtotal *= 0.80;
        } else if (timeOfDay >= 22 || timeOfDay <= 5) {
            subtotal *= 0.85;
        }

        return subtotal;
    }
}