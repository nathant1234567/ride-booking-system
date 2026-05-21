package classes;

import classes.Booking;
import classes.User;

import repository.BookingRepository;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class BookingRepositoryTest {

    @Test
    void testAddBooking() {

        User user =
                new User(
                        "Nathan",
                        "test@test.com",
                        "password123"
                );

        Booking booking =
                new Booking(
                        user,
                        "London Heathrow (LHR)",
                        "Canterbury",
                        50,
                        2,
                        new Date(),
                        new Date()
                );

        BookingRepository.addBooking(booking);

        assertTrue(
                BookingRepository
                        .getBookings()
                        .contains(booking)
        );
    }

    @Test
    void testRemoveBooking() {

        User user =
                new User(
                        "Nathan",
                        "test@test.com",
                        "password123"
                );

        Booking booking =
                new Booking(
                        user,
                        "London Heathrow (LHR)",
                        "Canterbury",
                        50,
                        2,
                        new Date(),
                        new Date()
                );

        BookingRepository.addBooking(booking);

        BookingRepository.removeBooking(booking);

        assertFalse(
                BookingRepository
                        .getBookings()
                        .contains(booking)
        );
    }
}