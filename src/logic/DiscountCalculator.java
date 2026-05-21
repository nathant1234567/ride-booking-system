package logic;

public class DiscountCalculator {

    public static double calculateDiscount(double basePrice, int timeOfDay) {
        double finalPrice = basePrice;

        if (timeOfDay >= 10 && timeOfDay <= 15) {
            finalPrice = basePrice * 0.80;
        }
        else if (timeOfDay >= 22 || timeOfDay <= 5) {
            finalPrice = basePrice * 0.85;
        }

        return finalPrice;
    }
}
