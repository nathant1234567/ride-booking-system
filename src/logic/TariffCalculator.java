package logic;

public class TariffCalculator {

    public static double calculateEstimate(String vehicleType, int luggageCount, double distance, String dayOfWeek, int timeOfDay) {

        double baseFare = distance * 2.50;
        double luggageFee = luggageCount * 2.00;
        double subtotal = baseFare + luggageFee;

        if (vehicleType.equalsIgnoreCase("Executive")) {
            subtotal *= 1.5;
        } else if (vehicleType.equalsIgnoreCase("Minibus")) {
            subtotal *= 2.0;
        }

        if (dayOfWeek.equalsIgnoreCase("Saturday") || dayOfWeek.equalsIgnoreCase("Sunday")) {
            subtotal *= 1.2;
        }

        if (timeOfDay >= 10 && timeOfDay <= 15) {
            subtotal *= 0.80;
        } else if (timeOfDay >= 22 || timeOfDay <= 5) {
            subtotal *= 0.85;
        }

        return subtotal;
    }
}