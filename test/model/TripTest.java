package model;

// --- REMOVE JUPITER PACKAGES AND ADD JUNIT 4 IMPORTS ---
import org.junit.Test;
import static org.junit.Assert.*;
// ------------------------------------------------------

import java.util.Date;

public class TripTest { // Class must be public in JUnit 4

    @Test
    public void testTripCreation() { // Must be public void
        Date now = new Date();
        Trip trip = new Trip("trip-123", "Central London", now, now, "Standard");

        // Fixed for JUnit 4: The custom error message string goes FIRST
        assertEquals("ID should match constructor input", "trip-123", trip.getId());
        assertEquals("Destination should match", "Central London", trip.getDestination());
        assertEquals("Vehicle type should match", "Standard", trip.getVehicleType());
        assertTrue("New trip should have no bookings initially", trip.getBookings().isEmpty());
    }

    @Test
    public void testAddAndRemoveBooking() { // Must be public void
        Date now = new Date();
        Trip trip = new Trip("trip-123", "Central London", now, now, "Standard");
        User user = new User("Test", "test@test.com", "123");
        Booking booking = new Booking(user, "Central London", "Pickup", 10, 1, 1, new Date(), new Date());

        trip.addBooking(booking);
        assertEquals("Trip should contain 1 booking", 1, trip.getBookings().size());
        assertTrue("Trip contains the specific booking object", trip.getBookings().contains(booking));

        trip.addBooking(booking);
        assertEquals("Should not add duplicate bookings to the same trip", 1, trip.getBookings().size());

        trip.removeBooking(booking);
        assertTrue("Trip should be empty after removal", trip.getBookings().isEmpty());
    }
}
