package repository;

import classes.Booking;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to set up the booking repo. You can remove and update bookings as well.
 */
public class BookingRepository {
    private static List<Booking> bookings = new ArrayList<>();


    public static void addBooking(Booking booking) {
        bookings.add(booking);
        System.out.println("Confirmation message: Booking added: " + booking);
    }

    public static List<Booking> getBookings() {
        return new ArrayList<>(bookings);
    }

    public static void removeBooking(Booking booking) {

        bookings.remove(booking);

        System.out.println(
                "Confirmation message: Booking cancelled: " + booking
        );
    }

    public static boolean updateBooking(Booking original, Booking updated) {
        int idx = bookings.indexOf(original);
        if (idx >= 0) {
            bookings.set(idx, updated);
            System.out.println("Confirmation message: Booking updated: " + updated);
            return true;
        }
        return false;
    }
}
