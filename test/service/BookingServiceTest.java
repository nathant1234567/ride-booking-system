package service;

import classes.Booking;
import classes.User;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Date;

public class BookingServiceTest {

    @Test
    public void testSaveBooking() {
        // Create a dummy user and a standard booking object to test storage
        User user = new User("Nathan", "test@test.com", "password123");
        Booking booking = new Booking(user, "London Heathrow (LHR)", "Canterbury", 50, 2, new Date(), new Date());

        // Call the service method to save it
        BookingService.saveBooking(booking);

        // Make sure it actually exists inside our database arraylist now
        assertTrue(repository.BookingRepository.getBookings().contains(booking));
    }

    @Test
    public void testCalculateAmendmentFee() {
        // Set up a mock booking to test if changes apply our expected flat charge
        Booking booking = new Booking(null, "London Heathrow (LHR)", "Canterbury", 50, 1, 0, new Date(), new Date());
        double fee = BookingService.calculateAmendmentFee(booking);

        // Assert that the fee is exactly 2.50 as required by our project rules
        assertEquals(2.50, fee, 0.001);
    }

    @Test
    public void testCalculateCancellationFee() {
        // Using a 10km distance booking with 1 passenger to test our specific penalty formula
        Booking booking = new Booking(null, "London Heathrow (LHR)", "Canterbury", 10, 1, 0, new Date(), new Date());
        double fee = BookingService.calculateCancellationFee(booking);

        // Formula check: Base fare (£17.00) * 10% penalty + £5 flat fee equals exactly 6.70
        assertEquals(6.70, fee, 0.001);
    }

    @Test
    public void testCheckTripAccommodationFailsOnOverload() {
        // Set up a booking with 9 passengers to trigger our capacity boundary limits
        Booking heavyBooking = new Booking(null, "London Heathrow (LHR)", "Canterbury", 20, 9, 0, new Date(), new Date());
        boolean canAccommodate = BookingService.checkTripAccommodation(heavyBooking, new Date(), new Date());

        // The test passes to see if the system correctly rejects (returns false) the overloaded vehicle
        assertFalse(canAccommodate);
    }
}
