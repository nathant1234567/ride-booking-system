package repository;

import classes.Booking;

import java.util.ArrayList;
import java.util.List;

public class BookingRepository {
    private static List<Booking> bookings = new ArrayList<>();

    public static void addBooking(Booking booking) {
        bookings.add(booking);
        System.out.println("Booking added: " + booking);
    }

    public static List<Booking> getBookings() {
        return new ArrayList<>(bookings);
    }

    public static boolean updateBooking(Booking original, Booking updated) {
        int idx = bookings.indexOf(original);
        if (idx >= 0) {
            bookings.set(idx, updated);
            System.out.println("Booking updated: " + updated);
            return true;
        }
        return false;
    }
}
