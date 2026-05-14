package logic;

import classes.Booking;
import repository.BookingRepository;

public class SaveBooking {
    public static void save(Booking booking) {
        BookingRepository.addBooking(booking);
    }
}
