package repository;

import model.Booking;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to set up the booking repo. You can remove and update bookings as well.
 */
public class BookingRepository {
    private static List<Booking> bookings = new ArrayList<>();


    public static void addBooking(Booking booking) {
        bookings.add(booking);
    }

    public static List<Booking> getBookings() {
        return new ArrayList<>(bookings);
    }

    public static void removeBooking(Booking booking) {
        bookings.remove(booking);
    }

    public static boolean updateBooking(Booking original, Booking updated) {
        int idx = bookings.indexOf(original);
        if (idx >= 0) {
            bookings.set(idx, updated);
            return true;
        }
        return false;
    }

    public static void clear() {
        bookings.clear();
    }
}
