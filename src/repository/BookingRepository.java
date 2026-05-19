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
}
