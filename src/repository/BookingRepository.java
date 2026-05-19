package repository;

import classes.Booking;

import java.util.ArrayList;
import java.util.List;

public class BookingRepository {
    private static List<Booking> bookings = new ArrayList<>();

    static {

        bookings.add(

                new Booking(
                        null,
                        "London Heathrow",
                        "Canterbury",
                        25,
                        2,
                        new java.util.Date(),
                        new java.util.Date()
                )
        );
    }

    public static void addBooking(Booking booking) {
        bookings.add(booking);
        System.out.println("Booking added: " + booking);
    }

    public static List<Booking> getBookings() {
        return new ArrayList<>(bookings);
    }
}
