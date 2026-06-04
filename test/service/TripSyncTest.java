package service;

import model.Booking;
import model.Trip;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.BookingRepository;
import repository.TripRepository;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class TripSyncTest {

    @BeforeEach
    public void setUp() {
        BookingRepository.clear();
        TripRepository.clear();
    }

    @Test
    public void testTripUpdatesOnAmendment() {
        User user = new User("Nathan", "test@test.com", "password123");
        Date date = new Date();
        Date time = new Date();
        
        // 1. Create initial booking
        Booking original = new Booking(user, "London", "Canterbury", 50, 2, 0, date, time);
        BookingService.saveBooking(original);
        
        Trip trip = original.getTrip();
        assertNotNull(trip, "Booking should be assigned to a trip");
        assertEquals(2, trip.getPassengerCount(), "Trip should have 2 passengers");
        assertTrue(trip.getBookings().contains(original), "Trip should contain the original booking");

        // 2. Amend the booking (change passengers from 2 to 4)
        Booking updated = new Booking(user, "London", "Canterbury", 50, 4, 0, date, time);
        BookingService.updateBooking(original, updated);

        // 3. Verify synchronization
        assertEquals(4, trip.getPassengerCount(), "Trip should now have 4 passengers");
        assertFalse(trip.getBookings().contains(original), "Trip should no longer contain the original booking object");
        assertTrue(trip.getBookings().contains(updated), "Trip should now contain the updated booking object");
        assertEquals(trip, updated.getTrip(), "Updated booking should be linked to the trip");
    }

    @Test
    public void testTripUpdatesOnCancellation() {
        User user = new User("Nathan", "test@test.com", "password123");
        Date date = new Date();
        
        Booking booking = new Booking(user, "London", "Canterbury", 50, 2, 0, date, date);
        BookingService.saveBooking(booking);
        
        Trip trip = booking.getTrip();
        assertNotNull(trip);
        assertEquals(2, trip.getPassengerCount());

        // Cancel the booking
        BookingService.cancelBooking(booking);

        // Verify trip is empty
        assertEquals(0, trip.getPassengerCount(), "Trip should have 0 passengers after cancellation");
        assertFalse(trip.getBookings().contains(booking), "Trip should not contain the cancelled booking");
    }
}
