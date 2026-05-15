package service;

import classes.Booking;
import repository.BookingRepository;

public class BookingService {

    public static void saveBooking(Booking booking) {
        BookingRepository.addBooking(booking);
    }

    public static void bookingLengthCalculator(Booking booking) {

    }
}

