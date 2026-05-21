package service;

import classes.Booking;
import classes.PriceBreakdown;
import repository.BookingRepository;

public class BookingService {

    public static void saveBooking(Booking booking) {
        BookingRepository.addBooking(booking);
    }

    /**
     * Calculate a simple price for a booking based on length, number of passengers and luggage.
     * Formula:
     *   basePerKm * lengthEstimate + passengerFeePerPerson * passengers + luggageFeePerItem * luggage
     * Minimum fare enforced at 5.0
     */
    public static double calculatePrice(Booking booking) {
        if (booking == null) return 0.0;

        double basePerKm = 1.5; // £ per km
        double passengerFeePerPerson = 2.0; // flat per passenger
        double luggageFeePerItem = 1.5; // per luggage item

        double base = basePerKm * booking.getLengthEstimate();
        double passengerFee = passengerFeePerPerson * booking.getNumberOfPassengers();
        double luggageFee = luggageFeePerItem * booking.getNumberOfLuggage();

        double total = base + passengerFee + luggageFee;

        // minimum fare
        if (total < 5.0) total = 5.0;

        // round to 2 decimals
        return Math.round(total * 100.0) / 100.0;
    }

    /**
     * Calculate price and apply discounts (promo codes, loyalty, group).
     * Returns a PriceBreakdown containing subtotal, discounts and final total.
     * Promo codes supported: "SAVE10" -> 10% off.
     */
    public static PriceBreakdown calculatePriceWithDiscount(Booking booking, String promoCode) {
        if (booking == null) return new PriceBreakdown(0,0,0,0,0,0,0, "");

        double basePerKm = 1.5;
        double passengerFeePerPerson = 2.0;
        double luggageFeePerItem = 1.5;

        double base = basePerKm * booking.getLengthEstimate();
        double passengerFee = passengerFeePerPerson * booking.getNumberOfPassengers();
        double luggageFee = luggageFeePerItem * booking.getNumberOfLuggage();
        double subtotal = base + passengerFee + luggageFee;
        if (subtotal < 5.0) subtotal = 5.0;

        double totalDiscountPercent = 0.0;
        StringBuilder desc = new StringBuilder();

        // Loyalty discount: users with kent.ac.uk email get 5%
        if (booking.getUser() != null && booking.getUser().getEmail() != null && booking.getUser().getEmail().endsWith("@kent.ac.uk")) {
            totalDiscountPercent += 0.05;
            desc.append("Loyalty 5% ");
        }

        // Group discount for >=5 passengers
        if (booking.getNumberOfPassengers() >= 5) {
            totalDiscountPercent += 0.10;
            desc.append("Group 10% ");
        }

        // Promo codes
        if (promoCode != null) {
            String pc = promoCode.trim().toUpperCase();
            if (pc.equals("SAVE10")) {
                totalDiscountPercent += 0.10;
                desc.append("Promo SAVE10 10% ");
            }
        }

        double discountAmount = Math.round(subtotal * totalDiscountPercent * 100.0) / 100.0;
        double finalTotal = subtotal - discountAmount;
        if (finalTotal < 0) finalTotal = 0;
        finalTotal = Math.round(finalTotal * 100.0) / 100.0;

        return new PriceBreakdown(base, passengerFee, luggageFee, subtotal, totalDiscountPercent, discountAmount, finalTotal, desc.toString().trim());
    }
}

