package service;

import classes.Booking;
import classes.User;

// --- REMOVE JUPITER PACKAGES AND ADD JUNIT 4 IMPORTS ---
import org.junit.Test;
import static org.junit.Assert.*;
// ------------------------------------------------------

import repository.BookingRepository;
import java.util.Date;

public class BookingServiceTest {

    @Test
    public void testSaveBooking() { // JUnit 4 tests must be public
        User user = new User("Nathan", "test@test.com", "password123");
        Booking booking = new Booking(user, "London Heathrow (LHR)", "Canterbury", 50, 2, new Date(), new Date());

        BookingService.saveBooking(booking);

        assertTrue(BookingRepository.getBookings().contains(booking));
    }

    @Test
    public void testCalculateAmendmentFee() {
        Booking booking = new Booking(null, "London Heathrow (LHR)", "Canterbury", 50, 1, 0, new Date(), new Date());
        double fee = BookingService.calculateAmendmentFee(booking);

        assertEquals(2.50, fee, 0.001);
    }

    @Test
    public void testCalculateCancellationFee() {
        Booking booking = new Booking(null, "London Heathrow (LHR)", "Canterbury", 10, 1, 0, new Date(), new Date());
        double fee = BookingService.calculateCancellationFee(booking);

        assertEquals(6.70, fee, 0.001);
    }

    @Test
    public void testCheckTripAccommodationFailsOnOverload() {
        Booking heavyBooking = new Booking(null, "London Heathrow (LHR)", "Canterbury", 20, 9, 0, new Date(), new Date());
        boolean canAccommodate = BookingService.checkTripAccommodation(heavyBooking, new Date(), new Date());

        assertFalse(canAccommodate);
    }
}
