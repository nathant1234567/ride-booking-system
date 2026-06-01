package model;

import repository.BookingRepository;

// --- REMOVE JUPITER AND ADD WORKING JUNIT 4 IMPORTS ---
import org.junit.Test;
import static org.junit.Assert.*;
// ------------------------------------------------------

import java.util.Date;

public class BookingRepositoryTest {

    @Test
    public void testAddBooking() { // Must be public in JUnit 4

        User user = new User("Nathan", "test@test.com", "password123");

        Booking booking = new Booking(
                user,
                "London Heathrow (LHR)",
                "Canterbury",
                50,
                2,
                new Date(),
                new Date()
        );

        BookingRepository.addBooking(booking);

        assertTrue(BookingRepository.getBookings().contains(booking));
    }

    @Test
    public void testRemoveBooking() { // Must be public in JUnit 4

        User user = new User("Nathan", "test@test.com", "password123");

        Booking booking = new Booking(
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

        assertFalse(BookingRepository.getBookings().contains(booking));
    }
}
