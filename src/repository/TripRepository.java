package repository;

import model.Trip;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Calendar;

/**
 * Repository class to manage trips. Has methods to add, clear, retrieve,
 * and search for trips based on specific criteria such as destination, date, time, and vehicle type.
 */
public class TripRepository {

    private static List<Trip> trips = new ArrayList<>();

    public static void addTrip(Trip trip) {
        trips.add(trip);
    }

    public static void clear() {
        trips.clear();
    }

    public static List<Trip> getTrips() {
        return new ArrayList<>(trips);
    }

    /**
     * Searches for a trip that matches the specified criteria including destination, date, time, and vehicle type.
     * Uses streams to achieve this
     *
     * @param destination the destination of the trip to search for
     * @param date the date of the trip to match
     * @param time the time of the trip to match
     * @param vehicleType the type of vehicle assigned to the trip (e.g., "Van", "Executive", or "Standard")
     * @return an Optional containing the matching trip if found, or an empty Optional if no match is found
     */
    public static Optional<Trip> findMatchingTrip(String destination, Date date, Date time, String vehicleType) {
        return trips.stream()
                .filter(t -> t.getDestination().equalsIgnoreCase(destination))
                .filter(t -> t.getVehicleType().equalsIgnoreCase(vehicleType))
                .filter(t -> isSameDay(t.getDate(), date))
                .filter(t -> isCloseTime(t.getTime(), time))
                .findFirst();
    }

    /**
     * Checks if two given dates correspond to the same calendar day.
     *
     * @param d1 the first date to compare
     * @param d2 the second date to compare
     * @return true if both dates are on the same calendar day, false otherwise
     */
    private static boolean isSameDay(Date d1, Date d2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(d1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(d2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
               cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * Checks if two given times are within 30 minutes of each other.
     * @param t1
     * @param t2
     * @return
     */
    private static boolean isCloseTime(Date t1, Date t2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(t1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(t2);

        int m1 = cal1.get(Calendar.HOUR_OF_DAY) * 60 + cal1.get(Calendar.MINUTE);
        int m2 = cal2.get(Calendar.HOUR_OF_DAY) * 60 + cal2.get(Calendar.MINUTE);

        return Math.abs(m1 - m2) <= 30;
    }
}
