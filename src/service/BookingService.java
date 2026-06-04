package service;

import model.Booking;
import model.PriceBreakdown;
import model.Trip;
import repository.BookingRepository;
import repository.TripRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.*;

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
        assignToTrip(booking);
        BookingRepository.addBooking(booking);
        notificationService.sendNotification(booking, "Booking confirmation: Your booking has been successfully created.");

        // Notify others if they are on the same trip
        notifyAffectedUsers(booking, "A new passenger has joined your trip. Trip duration may have changed.");
    }

    public static void assignToTrip(Booking booking) {
        Optional<Trip> matchingTrip = TripRepository.findMatchingTrip(
                booking.getDestination(), booking.getDate(), booking.getTime()
        );
        boolean addedToTrip = false;
        if (matchingTrip.isPresent()) {
            Trip existingTrip = matchingTrip.get();
            addedToTrip = existingTrip.addBooking(booking);
            if (addedToTrip) {
                booking.setTrip(existingTrip);
            }
        }
        if (!addedToTrip) {
            String newTripID = "TRP-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
            Trip newTrip = new Trip(newTripID, booking.getDestination(), booking.getDate(), booking.getTime(), "Standard");
            newTrip.addBooking(booking);
            booking.setTrip(newTrip);
            TripRepository.addTrip(newTrip);
        }
    }



    /**
     * Cancels a booking and notifies the user and any affected trip-mates.
     */
    public static void cancelBooking(Booking booking) {
        if (booking.getTrip() != null) {
            booking.getTrip().removeBooking(booking);
        }
        BookingRepository.removeBooking(booking);
        notificationService.sendNotification(booking, "Cancellation confirmation: Your booking has been cancelled.");

        // Notify others on the same trip
        notifyAffectedUsers(booking, "A passenger has left your trip. Trip duration may have changed.");
    }

    /**
     * Updates a booking and notifies the user and any affected trip-mates.
     */
    public static void updateBooking(Booking original, Booking updated) {
        // Remove from old trip if it exists
        if (original.getTrip() != null) {
            original.getTrip().removeBooking(original);
        }

        // Re-assign to a trip based on the updated details
        assignToTrip(updated);

        boolean success = BookingRepository.updateBooking(original, updated);
        if (success) {
            notificationService.sendNotification(updated, "Amendment confirmation: Your booking has been successfully updated.");

            // Notify others on the same trip (affected users)
            notifyAffectedUsers(updated, "The trip schedule or duration has changed due to an amendment by another passenger.");
        }
    }

    /**
     * Identifies and notifies other users on the same trip.
     */
    private static void notifyAffectedUsers(Booking triggerBooking, String message) {
        Trip trip = triggerBooking.getTrip();
        if (trip == null) return;

        for (Booking other : trip.getBookings()) {
            if (!other.equals(triggerBooking)) {
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

    // Added / fixed the price to calculate like the estimate browse tariff
    public static PriceBreakdown calculatePriceWithDiscount(Booking booking, String promoCode) {
        return calculatePriceWithDiscount(booking, promoCode, "Standard");
    }

    public static PriceBreakdown calculatePriceWithDiscount(Booking booking, String promoCode, String vehicleType) {
        if (booking == null) return new PriceBreakdown(0,0,0,0,0,0,0, "");

        double distanceInMiles = booking.getLengthEstimate() * 0.621371;

        Calendar cal = Calendar.getInstance();
        cal.setTime(booking.getTime());
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        cal.setTime(booking.getDate());
        int dayInt = cal.get(Calendar.DAY_OF_WEEK);
        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        String dayOfWeek = days[dayInt - 1];

        double subtotal = logic.TariffCalculator.calculateEstimate(vehicleType, booking.getNumberOfLuggage(), distanceInMiles, dayOfWeek, hour);

        double base = subtotal;
        double passengerFee = 0;
        double luggageFee = booking.getNumberOfLuggage() * 2.00;

        double totalDiscountPercent = 0.0;
        StringBuilder desc = new StringBuilder();

        if (booking.getUser() != null && booking.getUser().getEmail() != null && booking.getUser().getEmail().endsWith("@kent.ac.uk")) {
            totalDiscountPercent += 0.05;
            desc.append("Loyalty 5% ");
        }

        if (booking.getNumberOfPassengers() >= 5) {
            totalDiscountPercent += 0.10;
            desc.append("Group 10% ");
        }

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
        Optional<Trip> matchingTrip = TripRepository.findMatchingTrip(
                targetBooking.getDestination(), newDate, newTime
        );

        if (matchingTrip.isPresent()) {
            Trip trip = matchingTrip.get();
            int currentPassengers = trip.getPassengerCount();

            if (trip.getBookings().contains(targetBooking)) {
                currentPassengers -= targetBooking.getNumberOfPassengers();
            }
            if (currentPassengers + targetBooking.getNumberOfPassengers() > trip.getMaxCapacity()) {
                return false;
            }
        }
        return true;
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
