package service;

import model.Booking;
import model.PriceBreakdown;
import repository.BookingRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BookingService {
    /**
     * Class to handle the logic to create bookings.
     * It includes booking creation, cancellation and trip management functions. 
     * @param booking
     */

    private static NotificationService notificationService = new ConsoleNotificationService();

    public static void setNotificationService(NotificationService service) {
        notificationService = service;
    }

    public static void saveBooking(Booking booking) {
        BookingRepository.addBooking(booking);
        notificationService.sendNotification(booking, "Booking confirmation: Your booking has been successfully created.");

        // Notify others if they are on the same trip
        notifyAffectedUsers(booking, "A new passenger has joined your trip. Trip duration may have changed.");
    }

    /**
     * Cancels a booking and notifies the user and any affected trip-mates.
     */
    public static void cancelBooking(Booking booking) {
        BookingRepository.removeBooking(booking);
        notificationService.sendNotification(booking, "Cancellation confirmation: Your booking has been cancelled.");

        // Notify others on the same trip
        notifyAffectedUsers(booking, "A passenger has left your trip. Trip duration may have changed.");
    }

    /**
     * Updates a booking and notifies the user and any affected trip-mates.
     */
    public static void updateBooking(Booking original, Booking updated) {
        boolean success = BookingRepository.updateBooking(original, updated);
        if (success) {
            notificationService.sendNotification(updated, "Amendment confirmation: Your booking has been successfully updated.");

            // Notify others on the same trip (affected users)
            notifyAffectedUsers(updated, "The trip schedule or duration has changed due to an amendment by another passenger.");
        }
    }

    /**
     * Identifies and notifies other users on the same trip route and date.
     */
    private static void notifyAffectedUsers(Booking triggerBooking, String message) {
        List<Booking> allBookings = BookingRepository.getBookings();
        for (Booking other : allBookings) {
            if (!other.equals(triggerBooking) &&
                    other.getDestination().equalsIgnoreCase(triggerBooking.getDestination()) &&
                    other.getPickupLocation().equalsIgnoreCase(triggerBooking.getPickupLocation()) &&
                    isSameDay(other.getDate(), triggerBooking.getDate())) {

                notificationService.sendNotification(other, message);
            }
        }
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

    /**
     * Function to calcualte estimate durantion of the trip based on factors hard coded here
     * @param booking
     * @param luggageAmount
     * @return
     */
    public static int bookingLengthCalculator(Booking booking, int luggageAmount) {
        double distance = booking.getLengthEstimate();
        double averageSpeed = 50.0;

        double duration = (distance / averageSpeed) * 60;

        double trafficMultiplier = getTrafficMultiplier(booking.getTime());
        duration *= trafficMultiplier;

        duration += (luggageAmount * 2);

        duration += 5;
        duration += booking.getNumberOfPassengers();

        return (int) Math.round(duration);
    }

    /**
     * A simple traffic simulator based on what time of day the user selects. For eg. rush hour will be longer
     * @param time
     * @return
     */
    private static double getTrafficMultiplier(Date time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        if (hour >= 7 && hour < 10) {
            return 1.5;
        } else if (hour >= 16 && hour < 19) {
            return 1.5;
        } else if (hour >= 23 || hour < 5) {
            return 0.8;
        } else {
            return 1.0;
        }
    }

    // =========================================================================
    // --- METHOD: TRIP ACCOMMODATION AND DURATION RECALCULATION CHECK ---
    // =========================================================================
    public static boolean checkTripAccommodation(Booking targetBooking, Date newDate, Date newTime) {
        List<Booking> allBookings = BookingRepository.getBookings();

        // 1. CAPACITY CONSTRAINT CHECK
        int passengerCountOnThisTrip = targetBooking.getNumberOfPassengers();

        for (Booking other : allBookings) {
            // Skip the booking itself
            if (other.equals(targetBooking)) continue;

            // Check if another user is on the exact same trip route (Destination & Pickup match)
            if (other.getDestination().equalsIgnoreCase(targetBooking.getDestination()) &&
                    other.getPickupLocation().equalsIgnoreCase(targetBooking.getPickupLocation())) {

                // Check if they overlap on the same date
                if (isSameDay(other.getDate(), newDate)) {
                    passengerCountOnThisTrip += other.getNumberOfPassengers();

                    // Business Rule Example: Total passengers on a shared trip cannot exceed 8
                    if (passengerCountOnThisTrip > 8) {
                        return false; // Cannot accommodate the change
                    }
                }
            }
        }

        // 2. DURATION RECALCULATION FOR ALL AFFECTED USERS
        // Temporarily apply the new time variables to the target booking to recalculate its duration
        targetBooking.setTime(newTime);
        targetBooking.setDate(newDate);
        int newTargetDuration = bookingLengthCalculator(targetBooking, targetBooking.getNumberOfLuggage());
//        targetBooking.setLengthEstimate(newTargetDuration);

        // Recalculate duration for any other users riding along on this newly selected trip configuration
        for (Booking other : allBookings) {
            if (!other.equals(targetBooking) &&
                    other.getDestination().equalsIgnoreCase(targetBooking.getDestination()) &&
                    other.getPickupLocation().equalsIgnoreCase(targetBooking.getPickupLocation()) &&
                    isSameDay(other.getDate(), newDate)) {

                // Synchronize their time window to match the updated route group schedule
                other.setTime(newTime);
                int updatedOtherDuration = bookingLengthCalculator(other, other.getNumberOfLuggage());
//                other.setLengthEstimate(updatedOtherDuration);
            }
        }

        return true; // Accommodated and successfully updated duration fields
    }

    private static boolean isSameDay(Date d1, Date d2) {
        if (d1 == null || d2 == null) return false;
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(d1);
        cal2.setTime(d2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    // =========================================================================
    // --- NEW METHODS: AMENDMENT AND CANCELLATION FEE CALCULATIONS ---
    // =========================================================================

    /**
     * Calculates a separate flat handling fee specifically for modifying an existing trip slot.
     * Formula: Flat rate of £2.50 to process booking modifications.
     */
    public static double calculateAmendmentFee(Booking booking) {
        if (booking == null) return 0.0;
        return 2.50;
    }

    /**
     * Calculates the penalty fee charged when a user cancels an active booking.
     * Formula: Flat £5.00 base charge + 10% of the normal trip cost.
     */
    public static double calculateCancellationFee(Booking booking) {
        if (booking == null) return 0.0;

        double baseTripPrice = calculatePrice(booking);
        double fee = 5.00 + (baseTripPrice * 0.10);

        return Math.round(fee * 100.0) / 100.0; // Cleanly round to two decimals
    }
}
