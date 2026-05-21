package repository;

import classes.Trip;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Calendar;

public class TripRepository {
    private static List<Trip> trips = new ArrayList<>();

    public static void addTrip(Trip trip) {
        trips.add(trip);
    }

    public static List<Trip> getTrips() {
        return new ArrayList<>(trips);
    }

    public static Optional<Trip> findMatchingTrip(String destination, Date date, Date time) {
        return trips.stream()
                .filter(t -> t.getDestination().equals(destination))
                .filter(t -> isSameDay(t.getDate(), date))
                .filter(t -> isCloseTime(t.getTime(), time))
                .findFirst();
    }

    private static boolean isSameDay(Date d1, Date d2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(d1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(d2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
               cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    private static boolean isCloseTime(Date t1, Date t2) {
        // Within 30 minutes of each other
        long diff = Math.abs(t1.getTime() - t2.getTime());
        return diff <= 30 * 60 * 1000;
    }
}
